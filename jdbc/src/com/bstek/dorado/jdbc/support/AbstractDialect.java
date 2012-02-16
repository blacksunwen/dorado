package com.bstek.dorado.jdbc.support;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcDataProviderContext;
import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.jdbc.model.AbstractColumn;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.model.autotable.AutoTableColumn;
import com.bstek.dorado.jdbc.model.autotable.FromTable;
import com.bstek.dorado.jdbc.model.autotable.Order;
import com.bstek.dorado.jdbc.model.table.KeyGenerator;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;
import com.bstek.dorado.jdbc.sql.CurdSqlGenerator;
import com.bstek.dorado.jdbc.sql.DeleteSql;
import com.bstek.dorado.jdbc.sql.InsertSql;
import com.bstek.dorado.jdbc.sql.RecordRowMapper;
import com.bstek.dorado.jdbc.sql.RetrieveSql;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.ShiftRowMapperResultSetExtractor;
import com.bstek.dorado.jdbc.sql.SqlConstants.JoinModel;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.jdbc.sql.SqlConstants.NullsModel;
import com.bstek.dorado.jdbc.sql.SqlConstants.OrderModel;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.sql.UpdateSql;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.util.Assert;

public abstract class AbstractDialect implements Dialect {

	private static Log logger = LogFactory.getLog(AbstractDialect.class);
	
	private Map<String, JdbcType> jdbcTypeMap = new LinkedHashMap<String, JdbcType>();
	private LinkedHashMap<String, KeyGenerator<Object>> keyGeneratorMap = new LinkedHashMap<String, KeyGenerator<Object>>();
	
	public String token(Table table) {
		Assert.notNull(table, "Table must not be null.");
		
		String name = table.getTableName();
		String catalog = table.getCatalog();
		String schema = table.getSchema();
		if (StringUtils.isEmpty(catalog) && StringUtils.isEmpty(schema)) {
			return name;
		} else if(StringUtils.isNotEmpty(catalog)){
			return catalog + "." + name;
		} else if(StringUtils.isNotEmpty(schema)){
			return schema + "." + name;
		} else {
			return catalog + "." + schema + "." + name;
		}
	}
	
	public String token(Table table, String alias) {
		String token = token(table);
		if (StringUtils.isEmpty(alias)) {
			return token;
		} else {
			return token + " " + KeyWord.AS + " " + alias;
		}
	}
	
	public JdbcType getJdbcType(String name) {
		JdbcType jdbcType = jdbcTypeMap.get(name);
		Assert.notNull(jdbcType, "could not look up for JdbcType named [" + name + "].");
		return jdbcType;
	}
	
	public void setJdbcTypes(List<JdbcType> jdbcTypes) {
		jdbcTypeMap.clear();
		for (JdbcType jdbcType: jdbcTypes) {
			jdbcTypeMap.put(jdbcType.getName(), jdbcType);
		}
	}
	
	public List<JdbcType> getJdbcTypes() {
		return new ArrayList<JdbcType>(jdbcTypeMap.values());
	}
	
	public KeyGenerator<Object> getKeyGenerator(String name) {
		KeyGenerator<Object> kg = keyGeneratorMap.get(name);
		Assert.notNull(kg, "could not look up for KeyGenerator named [" + name + "].");
		return kg;
	}
	
	public JdbcType jdbcType(Map<String,String> columnMeta) {
		List<JdbcType> jdbcTypes = this.getJdbcTypes();
		for (JdbcType jdbcType: jdbcTypes) {
			if (jdbcType.is(columnMeta)) {
				return jdbcType;
			}
		}
		
		return null;
	}
	
	public String propertyName(Map<String,String> columnMeta) {
		String label = columnMeta.get(JdbcConstants.COLUMN_LABEL);
		if (StringUtils.isEmpty(label)) {
			return columnMeta.get(JdbcConstants.COLUMN_NAME);
		} else {
			return label;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void setKeyGenerators(List<KeyGenerator> kgs) {
		keyGeneratorMap.clear();
		for (KeyGenerator<Object> kg : kgs) {
			keyGeneratorMap.put(kg.getName(), kg);
		}
	}
	
	public List<KeyGenerator<Object>> getKeyGenerators() {
		return new ArrayList<KeyGenerator<Object>>(keyGeneratorMap.values());
	}
	
	public String toCountSQL(String sql) {
		return "SELECT COUNT(1) FROM ( " + sql + " )";
	}
	
	public String toSQL(SelectSql selectSql) {
		return selectSql.toSQL(this);
	}
	
	public String toCountSQL(SelectSql selectSql) {
		return selectSql.toCountSQL(this);
	}
	
	@SuppressWarnings("unchecked")
	public void execute(JdbcDataProviderOperation operation) {
		Page<Record> page = operation.getJdbcContext().getPage();
		if (page.getPageSize() > 0) {
			this.loadPageRecord(operation);
		} else {
			this.loadAllRecords(operation);
		}
	}
	
	/**
	 * 加载全部的记录
	 */
	@SuppressWarnings({ "unchecked" })
	protected void loadAllRecords(JdbcDataProviderOperation operation) {
		JdbcDataProviderContext jdbcContext = operation.getJdbcContext();
		JdbcEnviroment env = operation.getJdbcEnviroment();
		DbTable dbTable = operation.getDbTable();
		
		CurdSqlGenerator generator = dbTable.getCurdSqlGenerator();
		SelectSql selectSql = generator.selectSql(operation);
		JdbcParameterSource jps = selectSql.getParameterSource();
		
		RecordRowMapper rowMapper = new RecordRowMapper(dbTable.getAllColumns());

		NamedParameterJdbcTemplate jdbcTemplate = env.getNamedDao().getNamedParameterJdbcTemplate();

		String sql = env.getDialect().toSQL(selectSql);
		if (logger.isDebugEnabled()) {
			logger.debug("[SELECT-SQL]" + sql);
		}
		List<Record> rs = jdbcTemplate.query(sql, jps, rowMapper);
		jdbcContext.getPage().setEntities(rs);
	}

	/**
	 * 加载当前分页的记录
	 */
	@SuppressWarnings({ "unchecked" })
	protected void loadPageRecord(JdbcDataProviderOperation operation) {
		JdbcDataProviderContext jdbcContext = operation.getJdbcContext();
		Page<Record> page = jdbcContext.getPage();
		JdbcEnviroment env = operation.getJdbcEnviroment();
		Dialect dialect = env.getDialect();

		DbTable dbTable = operation.getDbTable();
		CurdSqlGenerator generator = dbTable.getCurdSqlGenerator();
		SelectSql selectSql = generator.selectSql(operation);

		RecordRowMapper rowMapper = new RecordRowMapper(dbTable.getAllColumns());

		NamedParameterJdbcTemplate jdbcTemplate = env.getNamedDao().getNamedParameterJdbcTemplate();

		int pageSize = page.getPageSize();
		int firstIndex = page.getFirstEntityIndex();

		if (dialect.isNarrowSupport()) {
			JdbcParameterSource jps = selectSql.getParameterSource();
			String sql = dialect.narrowSql(selectSql, pageSize, firstIndex);
			if (logger.isDebugEnabled()) {
				logger.debug("[SELECT-SQL]" + sql);
			}
			List<Record> rs = jdbcTemplate.query(sql, jps, rowMapper);
			page.setEntities(rs);
		} else {
			ShiftRowMapperResultSetExtractor rse = new ShiftRowMapperResultSetExtractor(
					rowMapper, pageSize, firstIndex);
			String sql = dialect.toSQL(selectSql);
			if (logger.isDebugEnabled()) {
				logger.debug("[SELECT-SQL]" + sql);
			}
			JdbcParameterSource jps = selectSql.getParameterSource();
			List<Record> rs = jdbcTemplate.query(sql, jps, rse);
			page.setEntities(rs);
		}

		String countSql = dialect.toCountSQL(selectSql);
		if (logger.isDebugEnabled()) {
			logger.debug("[COUNT-SQL]" + countSql);
		}
		int count = jdbcTemplate.queryForInt(countSql, selectSql.getParameterSource());
		page.setEntityCount(count);
	}
	
	public void execute(JdbcRecordOperation operation) {
		Record record = operation.getRecord();
		Assert.notNull(record, "record must not null.");

		EntityState state = EntityUtils.getState(record);
		switch (state) {
			case NEW: {
				this.doInsert(operation);
				break;
			}
			case MODIFIED: {
				this.doUpdate(operation);
				break;
			}
			case DELETED: {
				this.doDelete(operation);
				break;
			}
		}
	}
	
	/**
	 * 执行INSERT动作
	 */
	protected void doInsert(JdbcRecordOperation operation) {
		JdbcEnviroment env = operation.getJdbcEnviroment();
		DbTable dbTable = operation.getDbTable();
		CurdSqlGenerator generator = dbTable.getCurdSqlGenerator();
		
		InsertSql insertSql = generator.insertSql(operation);
		Dialect dialect = env.getDialect();
		String sql = insertSql.toSQL(dialect);
		if (logger.isDebugEnabled()) {
			logger.debug("[INSERT-SQL]" + sql);
		}
		
		NamedParameterJdbcTemplate jdbcTemplate = env.getNamedDao().getNamedParameterJdbcTemplate();
		TableKeyColumn identityColumn = insertSql.getIdentityColumn();
		if (identityColumn == null) {
			JdbcParameterSource parameterSource = insertSql.getParameterSource();
			jdbcTemplate.update(sql, parameterSource);
		} else {
			JdbcParameterSource parameterSource = insertSql.getParameterSource();
			GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(sql, parameterSource, keyHolder);
			String propertyName = identityColumn.getPropertyName();
			if (StringUtils.isNotEmpty(propertyName)) {
				Object value = keyHolder.getKey();
				JdbcType jdbcType = identityColumn.getJdbcType();
				if (jdbcType != null) {
					value = jdbcType.fromDB(value);
				}
				
				JdbcRecordOperation o = (operation.getSubstitute() == null)? operation: operation.getSubstitute();
				Record record = o.getRecord();
				record.put(propertyName, value);
			}
		}
		
		if(insertSql.isRetrieveAfterExecute()) {
			JdbcRecordOperation o = (operation.getSubstitute() == null)? operation: operation.getSubstitute();
			this.retrieve(o.getRecord(), o.getDbTable(), operation.getJdbcEnviroment());
		}
		
		this.sync(operation);
	}
	
	/**
	 * 执行UPDATE动作
	 */
	protected void doUpdate(JdbcRecordOperation operation) {
		DbTable dbTable = operation.getDbTable();
		JdbcEnviroment env = operation.getJdbcEnviroment();
		Dialect dialect = env.getDialect();
		NamedParameterJdbcTemplate jdbcTemplate = env.getNamedDao().getNamedParameterJdbcTemplate();
		CurdSqlGenerator generator = dbTable.getCurdSqlGenerator();
		UpdateSql updateSql = generator.updateSql(operation);
		String sql = updateSql.toSQL(dialect);
		if (logger.isDebugEnabled()) {
			logger.debug("[UPDATE-SQL]" + sql);
		}
		JdbcParameterSource parameterSource = updateSql.getParameterSource();
		jdbcTemplate.update(sql, parameterSource);
		
		if(updateSql.isRetrieveAfterExecute()) {
			JdbcRecordOperation o = (operation.getSubstitute() == null)? operation: operation.getSubstitute();
			this.retrieve(o.getRecord(), o.getDbTable(), operation.getJdbcEnviroment());
		}
		
		this.sync(operation);
	}
	
	/**
	 * 执行DELETE动作
	 */
	protected void doDelete(JdbcRecordOperation operation) {
		DbTable dbTable = operation.getDbTable();
		JdbcEnviroment env = operation.getJdbcEnviroment();
		Dialect dialect = env.getDialect();
		NamedParameterJdbcTemplate jdbcTemplate = env.getNamedDao().getNamedParameterJdbcTemplate();
		CurdSqlGenerator generator = dbTable.getCurdSqlGenerator();
		
		DeleteSql deleteSql = generator.deleteSql(operation);
		String sql = deleteSql.toSQL(dialect);
		if (logger.isDebugEnabled()) {
			logger.debug("[DELETE-SQL]" + sql);
		}
		JdbcParameterSource parameterSource = deleteSql.getParameterSource();
		jdbcTemplate.update(sql, parameterSource);
		
		Assert.isTrue(deleteSql.isRetrieveAfterExecute() == false, "delete sql does not support retrieve operation.");
		this.sync(operation);
	}
	
	protected void retrieve(Record record, DbTable dbTable, JdbcEnviroment jdbcEnv) {
		Dialect dialect = jdbcEnv.getDialect();
		if (dbTable instanceof Table) {
			Table table = (Table)dbTable;
			RetrieveSql retrieveSql = new RetrieveSql();
			retrieveSql.setTableToken(token(table));
			
			List<AbstractColumn> columnList = new ArrayList<AbstractColumn>();
			for(AbstractColumn column: table.getAllColumns()) {
				String columnName = column.getColumnName();
				String propertyName = column.getPropertyName();
				if (column.isSelectable() && record.containsKey(propertyName)) {
					columnList.add(column);
					retrieveSql.addColumnToken(columnName + " " + KeyWord.AS + " " + propertyName);
				}
			}
			JdbcParameterSource parameterSource = SqlUtils.createJdbcParameter(null);
			for (TableKeyColumn column: table.getKeyColumns()) {
				String propertyName = column.getPropertyName();
				if (column.isSelectable() && record.containsKey(propertyName)) {
					String columnName = column.getColumnName();
					Object columnValue = record.get(propertyName);
					JdbcType jdbcType = column.getJdbcType();
					if (jdbcType != null) {
						parameterSource.setValue(propertyName, columnValue, jdbcType.getSqlType());
					} else {
						parameterSource.setValue(propertyName, columnValue);
					}
					
					retrieveSql.addKeyToken(columnName, ":" + propertyName);
				}
			}

			String sql = retrieveSql.toSQL(dialect);
			if (logger.isDebugEnabled()) {
				logger.debug("[RETRIEVE-SQL]" + sql);
			}
			NamedParameterJdbcTemplate jdbcTemplate = jdbcEnv.getNamedDao().getNamedParameterJdbcTemplate();
			List<Record> rs = jdbcTemplate.query(sql, parameterSource, new RecordRowMapper(columnList));
			Assert.isTrue(rs.size() == 1, "[" + rs.size() +"] records retrieved, only 1 excepted.");
			
			Record rRecord = rs.get(0);
			record.putAll(rRecord);
		} else {
			throw new UnsupportedOperationException("[" + dbTable.getName() + "] does not support retrieve operation.");
		}
	}
	
	protected void sync(JdbcRecordOperation operation) {
		JdbcRecordOperation sOperation = operation.getSubstitute();
		if (sOperation != null) {
			Map<String, String> propertyMap = operation.getPropertyMap();
			if (propertyMap != null) {
				Record record = operation.getRecord();
				Record sRecord = sOperation.getRecord();
				
				Iterator<String> keyItr = propertyMap.keySet().iterator();
				while (keyItr.hasNext()) {
					String propertyName = keyItr.next();
					String sPropertyName = propertyMap.get(propertyName);
					Object sValue = sRecord.get(sPropertyName);
					record.put(propertyName, sValue);
				}
			}
		}
	}
	
	public String token(JoinModel joinModel) {
		switch (joinModel) {
		case INNER_JOIN:
			return "INNER JOIN";
		case LEFT_JOIN:
			return "LEFT JOIN";
		case RIGHT_JOIN:
			return "RIGHT JOIN";
		}
		
		throw new IllegalArgumentException("unknown JoinModel '" + joinModel + "'");
	}
	
	@Override
	public String token(Order order) {
		OrderModel model = order.getOrderModel();
		NullsModel nullsModel = order.getNullsModel(); 
		
		StringBuilder token = new StringBuilder();
		FromTable fromTable = order.getFromTable();
		if (fromTable == null) {
			AutoTableColumn ac = order.getSelfColumn();
			token.append(ac.getPropertyName());
		} else {
			String cn = order.getColumnName();
			AbstractColumn fromColumn = fromTable.getTable().getColumn(cn);
			token.append(fromTable.getTableAlias() + '.' + fromColumn.getColumnName());
		}
		
		if (model != null) {
			token.append(' ');
			token.append(model.toString());
		}
		if (nullsModel != null) {
			String nullsModelToken = token(nullsModel);
			if (StringUtils.isNotEmpty(nullsModelToken)) {
				token.append(' ');
				token.append(nullsModelToken);
			}
		}
		
		return token.toString();
	}
	
	protected String token(NullsModel nullsModel) {
		switch(nullsModel) {
		case NULLS_FIRST:
			return "NULLS FIRST";
		case NULLS_LAST:
			return "NULLS LAST";
		}
		
		return null;
	}
	
	private String defaultCatalog;
	public String defaultCatalog(DataSource dataSource, DatabaseMetaData databaseMetaData) {
		return defaultCatalog;
	}
	
	private String defaultSchema;
	public String defaultSchema(DataSource dataSource, DatabaseMetaData databaseMetaData) {
		return defaultSchema;
	}

	public String getDefaultCatalog() {
		return defaultCatalog;
	}

	public void setDefaultCatalog(String defaultCatalog) {
		this.defaultCatalog = defaultCatalog;
	}

	public String getDefaultSchema() {
		return defaultSchema;
	}

	public void setDefaultSchema(String defaultSchema) {
		this.defaultSchema = defaultSchema;
	}
	
}
