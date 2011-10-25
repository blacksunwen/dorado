package com.bstek.dorado.jdbc.support;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.key.KeyGenerator;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;
import com.bstek.dorado.jdbc.sql.DeleteSql;
import com.bstek.dorado.jdbc.sql.InsertSql;
import com.bstek.dorado.jdbc.sql.RecordRowMapper;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.ShiftRowMapperResultSetExtractor;
import com.bstek.dorado.jdbc.sql.SqlBuilder;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.jdbc.sql.SqlGenerator;
import com.bstek.dorado.jdbc.sql.UpdateSql;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.util.Assert;

public abstract class AbstractDialect implements Dialect {

	private static Log logger = LogFactory.getLog(AbstractDialect.class);
	
	private Map<String, JdbcType> jdbcTypeMap = new LinkedHashMap<String, JdbcType>();
	private LinkedHashMap<String, KeyGenerator<Object>> keyGeneratorMap = new LinkedHashMap<String, KeyGenerator<Object>>();
	
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
		SqlBuilder sqlBuilder = new SqlBuilder();
		sqlBuilder.rightSpace(KeyWord.SELECT, "COUNT(*)", KeyWord.FROM).brackets(sql);
		
		return sqlBuilder.build();
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

		DbElement dbElement = operation.getDbElement();
		SqlGenerator generator = JdbcUtils.getSqlGenerator(dbElement);
		SelectSql selectSql = generator.selectSql(operation);
		JdbcParameterSource jps = selectSql.getParameterSource();
		RecordRowMapper rowMapper = new RecordRowMapper(dbElement.getAllColumns());

		NamedParameterJdbcTemplate jdbcTemplate = env.getNamedDao().getNamedParameterJdbcTemplate();

		String sql = env.getDialect().toSQL(selectSql);
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

		DbElement dbElement = operation.getDbElement();
		SqlGenerator generator = JdbcUtils.getSqlGenerator(dbElement);
		SelectSql selectSql = generator.selectSql(operation);
		RecordRowMapper rowMapper = new RecordRowMapper(dbElement.getAllColumns());

		NamedParameterJdbcTemplate jdbcTemplate = env.getNamedDao().getNamedParameterJdbcTemplate();

		int pageSize = page.getPageSize();
		int firstIndex = page.getFirstEntityIndex();

		if (dialect.isNarrowSupport()) {
			JdbcParameterSource jps = selectSql.getParameterSource();
			String sql = dialect.narrowSql(selectSql, pageSize, firstIndex);
			List<Record> rs = jdbcTemplate.query(sql, jps, rowMapper);
			page.setEntities(rs);
		} else {
			ShiftRowMapperResultSetExtractor rse = new ShiftRowMapperResultSetExtractor(
					rowMapper, pageSize, firstIndex);
			String sql = dialect.toSQL(selectSql);
			JdbcParameterSource jps = selectSql.getParameterSource();
			List<Record> rs = jdbcTemplate.query(sql, jps, rse);
			page.setEntities(rs);
		}

		String countSql = dialect.toCountSQL(selectSql);
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
		
		Dialect dialect = env.getDialect();
		DbElement dbElement = operation.getDbElement();
		SqlGenerator generator = JdbcUtils.getSqlGenerator(dbElement);
		InsertSql insertSql = generator.insertSql(operation);
		String sql = insertSql.toSQL(dialect);
		if (logger.isDebugEnabled()) {
			logger.debug("[INSERT-SQL]" + sql);
		}
		JdbcRecordOperation substitute = operation.getSubstitute();
		
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
				
				if (substitute != null) {
					Record record = substitute.getRecord();
					record.put(propertyName, value);
				} else {
					Record record = operation.getRecord();
					record.put(propertyName, value);
				}
			}
		}
		
		if (substitute != null) {
			Record record = operation.getRecord();
			Record sRecord = substitute.getRecord();
			record.putAll(sRecord);
		}
	}
	
	/**
	 * 执行UPDATE动作
	 */
	protected void doUpdate(JdbcRecordOperation operation) {
		DbElement dbElement = operation.getDbElement();
		JdbcEnviroment env = operation.getJdbcEnviroment();
		Dialect dialect = env.getDialect();
		NamedParameterJdbcTemplate jdbcTemplate = env.getNamedDao().getNamedParameterJdbcTemplate();
		SqlGenerator generator = JdbcUtils.getSqlGenerator(dbElement);
		UpdateSql updateSql = generator.updateSql(operation);
		String sql = updateSql.toSQL(dialect);
		if (logger.isDebugEnabled()) {
			logger.debug("[UPDATE-SQL]" + sql);
		}
		JdbcParameterSource parameterSource = updateSql.getParameterSource();
		jdbcTemplate.update(sql, parameterSource);
	}
	
	/**
	 * 执行DELETE动作
	 */
	protected void doDelete(JdbcRecordOperation operation) {
		DbElement dbElement = operation.getDbElement();
		JdbcEnviroment env = operation.getJdbcEnviroment();
		Dialect dialect = env.getDialect();
		NamedParameterJdbcTemplate jdbcTemplate = env.getNamedDao().getNamedParameterJdbcTemplate();
		SqlGenerator generator = JdbcUtils.getSqlGenerator(dbElement);
		
		DeleteSql deleteSql = generator.deleteSql(operation);
		String sql = deleteSql.toSQL(dialect);
		if (logger.isDebugEnabled()) {
			logger.debug("[DELETE-SQL]" + sql);
		}
		JdbcParameterSource parameterSource = deleteSql.getParameterSource();
		jdbcTemplate.update(sql, parameterSource);
	}
}
