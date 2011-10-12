package com.bstek.dorado.jdbc;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.AbstractJdbcOperation;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.JdbcEnviroment;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;
import com.bstek.dorado.jdbc.sql.DeleteSql;
import com.bstek.dorado.jdbc.sql.InsertSql;
import com.bstek.dorado.jdbc.sql.SqlGenerator;
import com.bstek.dorado.jdbc.sql.UpdateSql;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.util.Assert;

/**
 * {@link com.bstek.dorado.jdbc.JdbcDataResolver}对应的数据库操作
 * 
 * @author mark
 * 
 */
public class JdbcDataResolverOperation extends
		AbstractJdbcOperation<JdbcDataResolverContext> {

	private static Log logger = LogFactory.getLog(JdbcDataResolverOperation.class);

	private Record record;

	public JdbcDataResolverOperation(DbElement dbElement, Record record,
			JdbcDataResolverContext jdbcContext) {
		super(dbElement, jdbcContext);
		this.setRecord(record);
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	/*
	 * @see com.bstek.dorado.jdbc.model.AbstractJdbcOperation#execute()
	 */
	@Override
	public void execute() {
		Assert.notNull(record, "record must not null.");

		EntityState state = EntityUtils.getState(record);
		switch (state) {
			case NEW: {
				this.doInsert();
				break;
			}
			case MODIFIED: {
				this.doUpdate();
				break;
			}
			case DELETED: {
				this.doDelete();
				break;
			}
		}
	}

	/**
	 * 执行INSERT动作
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void doInsert() {
		DbElement dbElement = this.getDbElement();
		JdbcDataResolverContext jdbcContext = this.getJdbcContext();
		JdbcEnviroment env = jdbcContext.getJdbcEnviroment();
		Dialect dialect = env.getDialect();
		NamedParameterJdbcTemplate jdbcTemplate = env.getNamedDao().getNamedParameterJdbcTemplate();
		SqlGenerator generator = JdbcUtils.getSqlGenerator(dbElement);
		
		InsertSql insertSql = generator.insertSql(dbElement, record, jdbcContext);
		String sql = insertSql.toSQL(dialect);
		if (logger.isDebugEnabled()) {
			logger.debug("[INSERT-SQL]" + sql);
		}
		
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
	}
	
	/**
	 * 执行UPDATE动作
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void doUpdate() {
		DbElement dbElement = this.getDbElement();
		JdbcDataResolverContext jdbcContext = this.getJdbcContext();
		JdbcEnviroment env = jdbcContext.getJdbcEnviroment();
		Dialect dialect = env.getDialect();
		NamedParameterJdbcTemplate jdbcTemplate = env.getNamedDao().getNamedParameterJdbcTemplate();
		SqlGenerator generator = JdbcUtils.getSqlGenerator(dbElement);
		
		UpdateSql updateSql = generator.updateSql(dbElement, record, jdbcContext);
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void doDelete() {
		DbElement dbElement = this.getDbElement();
		JdbcDataResolverContext jdbcContext = this.getJdbcContext();
		JdbcEnviroment env = jdbcContext.getJdbcEnviroment();
		Dialect dialect = env.getDialect();
		NamedParameterJdbcTemplate jdbcTemplate = env.getNamedDao().getNamedParameterJdbcTemplate();
		SqlGenerator generator = JdbcUtils.getSqlGenerator(dbElement);
		
		DeleteSql deleteSql = generator.deleteSql(dbElement, record, jdbcContext);
		String sql = deleteSql.toSQL(dialect);
		if (logger.isDebugEnabled()) {
			logger.debug("[DELETE-SQL]" + sql);
		}
		JdbcParameterSource parameterSource = deleteSql.getParameterSource();
		jdbcTemplate.update(sql, parameterSource);
	}
}
