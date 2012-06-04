package com.bstek.dorado.jdbc.test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcEnviroment;

public class TestTable extends AbstractDbElement {

	private String tableName;
	private List<Column> columns = new ArrayList<Column>();
	
	public TestTable(String tableName) {
		this.tableName = tableName;
	}
	
	public String getName() {
		return tableName;
	}
	
	public TestTable addColumn(String name, String dataType, String other) {
		Column c = new Column();
		c.name = name;
		c.dataType = dataType;
		c.other = other;
		
		columns.add(c);
		return this;
	}
	
	public TestTable addColumn(String name, String dataType) {
		return this.addColumn(name, dataType, null);
	}
	
	public Record get(String idColumnName, Object id) {
		JdbcEnviroment env = this.getEnviromentManager().getDefault();
		NamedParameterJdbcTemplate tpl = env.getSpringNamedDao().getNamedParameterJdbcTemplate();
		
		String sql = "select * from " + this.getName() + " where " + idColumnName + "=:id";
		return tpl.queryForObject(sql, Collections.singletonMap("id", id), new RowMapper<Record>(){

			public Record mapRow(ResultSet rs, int rowNum) throws SQLException {
				ResultSetMetaData metaData = rs.getMetaData();
				Record record = new Record();
				int columnCount = metaData.getColumnCount();
				for (int i=1; i<=columnCount; i++) {
					String columnName = metaData.getColumnName(i);
					Object columnValue = rs.getObject(i);
					
					record.put(columnName, columnValue);
				}
				return record;
			}
			
		});
	}
	
	public boolean has(String idColumnName, Object id) {
		JdbcEnviroment env = this.getEnviromentManager().getDefault();
		NamedParameterJdbcTemplate tpl = env.getSpringNamedDao().getNamedParameterJdbcTemplate();
		
		String sql = "select * from " + this.getName() + " where " + idColumnName + "=:id";
		
		List<Map<String,Object>> list = tpl.queryForList(sql, Collections.singletonMap("id", id));
		return list.size() == 1;
	}
	
	public TestTable insert(Record record){
		Set<String> keySet = record.keySet();
		String sql = "insert into " + this.getName() + "(";
		String[] keyNames = keySet.toArray(new String[0]);
		sql += StringUtils.join(keyNames, ',');
		sql += ")";
		sql += "values(";
		for (int i=0; i<keyNames.length; i++) {
			String keyName = keyNames[i];
			keyNames[i] = ":" + keyName;
		}
		sql += StringUtils.join(keyNames, ',');
		sql += ")";
		
		JdbcEnviroment env = this.getEnviromentManager().getDefault();
		NamedParameterJdbcTemplate tpl = env.getSpringNamedDao().getNamedParameterJdbcTemplate();
		
		tpl.update(sql, record);
		return this;
	}
	
	@Override
	protected String toCreateSQL() {
		String script = "CREATE TABLE " + tableName + " (";
		
		for (int i=0; i<columns.size(); i++) {
			script += "\n";
			script += "    ";
			String columnSql = columns.get(i).toSQL();
			script += columnSql;
			
			if (i<columns.size()-1) {
				script += ",";
			}
			script += "\n";
		}
		script += ")";
				
		return script;
	}
	
	@Override
	protected String toDropSQL() {
		String script = "DROP TABLE " + tableName;
		return script;
	}
	
	private class Column {
		private String name;
		private String dataType;
		private String other;
		
		String toSQL() {
			return name + " " + dataType + (other == null ? "": " " + other );
		}
	}
	
}
