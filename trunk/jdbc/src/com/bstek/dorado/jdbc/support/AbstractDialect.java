package com.bstek.dorado.jdbc.support;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
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
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.model.autotable.AutoTable;
import com.bstek.dorado.jdbc.model.autotable.AutoTableColumn;
import com.bstek.dorado.jdbc.model.autotable.FromTable;
import com.bstek.dorado.jdbc.model.autotable.Order;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;
import com.bstek.dorado.jdbc.sql.DeleteSql;
import com.bstek.dorado.jdbc.sql.InsertSql;
import com.bstek.dorado.jdbc.sql.RecordRowMapper;
import com.bstek.dorado.jdbc.sql.RetrieveSql;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.ShiftRowMapperResultSetExtractor;
import com.bstek.dorado.jdbc.sql.SqlConstants.JoinOperator;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.jdbc.sql.SqlConstants.NullsDirection;
import com.bstek.dorado.jdbc.sql.SqlConstants.OrderDirection;
import com.bstek.dorado.jdbc.sql.SqlGenerator;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.sql.UpdateSql;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.util.Assert;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractDialect implements Dialect {

	private static Log logger = LogFactory.getLog(AbstractDialect.class);
	private SqlGenerator sqlGenerator;
	
	public SqlGenerator getSqlGenerator() {
		return sqlGenerator;
	}

	public void setSqlGenerator(SqlGenerator sqlGenerator) {
		this.sqlGenerator = sqlGenerator;
	}

	public String token(Table table) {
		Assert.notNull(table, "Table must not be null.");
		
		String tableName = table.getTableName();
		String namespace = table.getNamespace();
		
		if (StringUtils.isNotEmpty(namespace)) {
			return namespace + "." + tableName;
		} else {
			return tableName;
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
	
	public String propertyName(Map<String,String> columnMeta) {
		String label = columnMeta.get(JdbcConstants.COLUMN_LABEL);
		if (StringUtils.isEmpty(label)) {
			return columnMeta.get(JdbcConstants.COLUMN_NAME);
		} else {
			return label;
		}
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
	
	public boolean execute(JdbcDataProviderOperation operation) {
		Page<Record> page = operation.getJdbcContext().getPage();
		if (page.getPageSize() > 0) {
			this.loadPageRecord(operation);
		} else {
			this.loadAllRecords(operation);
		}
		return true;
	}
	
	/**
	 * 加载全部的记录
	 */
	protected void loadAllRecords(JdbcDataProviderOperation operation) {
		JdbcDataProviderContext jdbcContext = operation.getJdbcContext();
		JdbcEnviroment env = operation.getJdbcEnviroment();
		DbTable dbTable = operation.getDbTable();
		
		SelectSql selectSql = dbTable.selectSql(operation);
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
	protected void loadPageRecord(JdbcDataProviderOperation operation) {
		JdbcDataProviderContext jdbcContext = operation.getJdbcContext();
		Page<Record> page = jdbcContext.getPage();
		JdbcEnviroment env = operation.getJdbcEnviroment();
		Dialect dialect = env.getDialect();

		DbTable dbTable = operation.getDbTable();
		SelectSql selectSql = dbTable.selectSql(operation);

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
	
	public boolean execute(JdbcRecordOperation operation) {
		Record record = operation.getRecord();
		Assert.notNull(record, "record must not null.");

		EntityState state = EntityUtils.getState(record);
		switch (state) {
			case NEW: {
				this.doInsert(operation);
				return true;
			}
			case MODIFIED: 
			case MOVED:{
				this.doUpdate(operation);
				return true;
			}
			case DELETED: {
				this.doDelete(operation);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 执行INSERT动作
	 */
	protected void doInsert(JdbcRecordOperation operation) {
		JdbcEnviroment env = operation.getJdbcEnviroment();
		Table table = operation.getTable();
		Record record = operation.getRecord();
		
		InsertSql insertSql = this.getSqlGenerator().insertSql(operation);
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
				
				record.put(propertyName, value);
			}
		}
		
		if(insertSql.isRetrieveAfterExecute()) {
			this.retrieve(record, table, env);
		}
	}
	
	/**
	 * 执行UPDATE动作
	 */
	protected void doUpdate(JdbcRecordOperation operation) {
		Table table = operation.getTable();
		JdbcEnviroment env = operation.getJdbcEnviroment();
		Record record = operation.getRecord();
		
		Dialect dialect = env.getDialect();
		NamedParameterJdbcTemplate jdbcTemplate = env.getNamedDao().getNamedParameterJdbcTemplate();
		UpdateSql updateSql = this.getSqlGenerator().updateSql(operation);
		String sql = updateSql.toSQL(dialect);
		if (logger.isDebugEnabled()) {
			logger.debug("[UPDATE-SQL]" + sql);
		}
		JdbcParameterSource parameterSource = updateSql.getParameterSource();
		jdbcTemplate.update(sql, parameterSource);
		
		if(updateSql.isRetrieveAfterExecute()) {
			this.retrieve(record, table, env);
		}
	}
	
	/**
	 * 执行DELETE动作
	 */
	protected void doDelete(JdbcRecordOperation operation) {
		JdbcEnviroment env = operation.getJdbcEnviroment();
		Dialect dialect = env.getDialect();
		NamedParameterJdbcTemplate jdbcTemplate = env.getNamedDao().getNamedParameterJdbcTemplate();
		
		DeleteSql deleteSql = this.getSqlGenerator().deleteSql(operation);
		String sql = deleteSql.toSQL(dialect);
		if (logger.isDebugEnabled()) {
			logger.debug("[DELETE-SQL]" + sql);
		}
		JdbcParameterSource parameterSource = deleteSql.getParameterSource();
		jdbcTemplate.update(sql, parameterSource);
	}
	
	protected void retrieve(Record record, Table table, JdbcEnviroment jdbcEnv) {
		Dialect dialect = jdbcEnv.getDialect();
		RetrieveSql retrieveSql = new RetrieveSql();
		retrieveSql.setTableToken(token(table));
		
		List<AbstractDbColumn> columnList = new ArrayList<AbstractDbColumn>();
		for(AbstractDbColumn column: table.getAllColumns()) {
			String columnName = column.getName();
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
				String columnName = column.getName();
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
	}
	
	public String token(AutoTable autoTable, JoinOperator joinModel) {
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
	public String token(AutoTable autoTable, Order order) {
		OrderDirection model = order.getDirection();
		NullsDirection nullsModel = order.getNullsDirection(); 
		String columnName = order.getColumn();
		Assert.notEmpty(columnName, "[" + autoTable.getName() + "] columnName must not be null in order");
		
		StringBuilder token = new StringBuilder();
		String tableAlias = order.getFromTable();
		if (StringUtils.isEmpty(tableAlias)) {
			AutoTableColumn column = (AutoTableColumn)autoTable.getColumn(columnName);
			token.append(column.getName());
		} else {
			FromTable fromTable = autoTable.getFromTable(tableAlias);
			AbstractDbColumn fromColumn = fromTable.getTableObject().getColumn(columnName);
			token.append(fromTable.getName() + '.' + fromColumn.getName());
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
	
	protected String token(NullsDirection nullsModel) {
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
