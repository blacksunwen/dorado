package com.bstek.dorado.jdbc.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.data.entity.EntityEnhancer;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.model.table.TableColumn;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.sql.UpdateSql;
import com.bstek.dorado.jdbc.type.JdbcType;

public class UpdateCommand {
	
	private static Log logger = LogFactory.getLog(UpdateCommand.class);
	
	private RetrieveCommand retrieveCommand;
	
	public RetrieveCommand getRetrieveCommand() {
		return retrieveCommand;
	}

	public void setRetrieveCommand(RetrieveCommand retrieveCommand) {
		this.retrieveCommand = retrieveCommand;
	}
	
	public void execute(JdbcRecordOperation operation) throws Exception {
		JdbcEnviroment env = operation.getJdbcEnviroment();
		
		Dialect dialect = env.getDialect();
		NamedParameterJdbcTemplate jdbcTemplate = env.getSpringNamedDao().getNamedParameterJdbcTemplate();
		UpdateSql updateSql = this.updateSql(operation);
		String sql = dialect.toSQL(updateSql);
		if (logger.isDebugEnabled()) {
			logger.debug("[UPDATE-SQL]" + sql);
		}
		JdbcParameterSource parameterSource = updateSql.getParameterSource();
		jdbcTemplate.update(sql, parameterSource);
		
		if(updateSql.isRetrieveAfterExecute()) {
			retrieveCommand.execute(operation);
		}
	}
	
	public UpdateSql updateSql(JdbcRecordOperation operation) {
		Dialect dialect = operation.getJdbcEnviroment().getDialect();
		
		Table table = (Table)operation.getDbTable();
		Record record = operation.getRecord();
		
		UpdateSql sql = new UpdateSql();
		JdbcParameterSource parameterSource = SqlUtils.createJdbcParameter(record);
		sql.setParameterSource(parameterSource);
		sql.setTableToken(dialect.token(table));
		
		for (TableKeyColumn keyColumn: table.getKeyColumns()) {
			String propertyName = keyColumn.getPropertyName();
			String columnName = keyColumn.getName();
			Object oldValue = null;
			if (hasOldValues(record) && (oldValue = EntityUtils.getOldValue(record, propertyName)) != null) {
				if (keyColumn.isUpdatable()) {
					sql.addColumnToken(columnName, ":" + propertyName);
					
					String oldPropertyVar = oldPropertyVar(propertyName);
					parameterSource.setValue(oldPropertyVar, oldValue);
					sql.addKeyToken(columnName, ":" + oldPropertyVar);
				} else {
					parameterSource.setValue(propertyName, oldValue);
					sql.addKeyToken(columnName, ":" + propertyName);
				}
			} else {
				sql.addKeyToken(columnName, ":" + propertyName);
			}
		}
		
		for (TableColumn column: table.getTableColumns()) {
			if (column.isUpdatable()) {
				String propertyName = column.getPropertyName();
				//如果record包含propertyName，那么一定更新到数据库，否则（不包含）如果column具有默认值那么也要更新，否则不更新
				if (record.containsKey(propertyName)) {
					Object value = record.get(propertyName);
					if (value == null) {
						value = column.getUpdateDefaultValue();
					}
					
					this.addColumnToken(sql, parameterSource, column, propertyName, value);
				} else {
					Object value = column.getUpdateDefaultValue();
					if (value != null) {
						this.addColumnToken(sql, parameterSource, column, propertyName, value);
					}
				}
			}
		}
		
		sql.setRetrieveAfterExecute(table.isRetrieveAfterUpdate());
		return sql;
	}
	
	protected void addColumnToken(UpdateSql sql, JdbcParameterSource parameterSource, 
			TableColumn c, String propertyName, Object value) {
		String columnName = c.getName();
		JdbcType jdbcType = c.getJdbcType();
		if (jdbcType != null) {
			Object dbValue = jdbcType.toDB(value);
			parameterSource.setValue(propertyName, dbValue, jdbcType.getSqlType());
		}
		sql.addColumnToken(columnName, ":" + propertyName);
	}

	private boolean hasOldValues(Object entity) {
		EntityEnhancer entityEnhancer = EntityUtils.getEntityEnhancer(entity);
		if (entityEnhancer != null) {
			return entityEnhancer.getOldValues() != null;
		} else {
			return false;
		}
	}
	
	protected String oldPropertyVar(String propertyName) {
		return "_OLD_" + propertyName;
	}
	
}
