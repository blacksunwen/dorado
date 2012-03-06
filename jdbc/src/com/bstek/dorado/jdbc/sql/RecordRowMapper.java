package com.bstek.dorado.jdbc.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.util.Assert;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class RecordRowMapper implements RowMapper<Record> {

	public List<AbstractDbColumn> columns;
	
	public RecordRowMapper(List<AbstractDbColumn> columns) {
		this.columns = columns;
		Assert.notEmpty(this.columns, "columns must not be empty.");
	}
	
	@Override
	public Record mapRow(ResultSet rs, int rowNum) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>(columns.size());
		for (AbstractDbColumn c: columns) {
			String columnName = c.getName();
			String propertyName = c.getPropertyName();
			JdbcType jdbcType = c.getJdbcType();
			Object dbValue = rs.getObject(columnName);
			
			if (jdbcType != null) {
				Object value = jdbcType.fromDB(dbValue);
				map.put(propertyName, value);	
			} else {
				map.put(propertyName, dbValue);
			}
		}
		
		return new Record(map);
	}

}
