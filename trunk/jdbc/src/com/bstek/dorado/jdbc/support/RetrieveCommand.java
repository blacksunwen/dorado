package com.bstek.dorado.jdbc.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
import com.bstek.dorado.jdbc.model.Table;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;
import com.bstek.dorado.jdbc.sql.RecordRowMapper;
import com.bstek.dorado.jdbc.sql.RetrieveSql;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.util.Assert;

public class RetrieveCommand {

	private static Log logger = LogFactory.getLog(RetrieveCommand.class);
	
	public void execute(JdbcRecordOperation operation) throws Exception {
		Table table = operation.getDbTable();
		JdbcEnviroment jdbcEnv = operation.getJdbcEnviroment();
		Record record = operation.getRecord();
		
		Dialect dialect = jdbcEnv.getDialect();
		RetrieveSql retrieveSql = new RetrieveSql();
		retrieveSql.setTableToken(dialect.token(table));
		
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

		String sql = dialect.toSQL(retrieveSql);
		if (logger.isDebugEnabled()) {
			logger.debug("[RETRIEVE-SQL]" + sql);
		}
		NamedParameterJdbcTemplate jdbcTemplate = jdbcEnv.getSpringNamedDao().getNamedParameterJdbcTemplate();
		List<Record> rs = jdbcTemplate.query(sql, parameterSource, new RecordRowMapper(columnList));
		Assert.isTrue(rs.size() == 1, "[" + rs.size() +"] record(s) retrieved, only 1 excepted.");
		
		Record rRecord = rs.get(0);
		record.putAll(rRecord);
	}
	
}
