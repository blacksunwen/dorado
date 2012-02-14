package com.bstek.dorado.jdbc.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.Column;
import com.bstek.dorado.jdbc.model.autotable.AutoTableColumn;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.util.Assert;

public class RecordRowMapper implements RowMapper<Record> {

	public List<Column> columns;
	
	public RecordRowMapper(List<Column> columns) {
		this.columns = columns;
		Assert.notEmpty(this.columns, "columns must not be empty.");
	}
	
	@Override
	public Record mapRow(ResultSet rs, int rowNum) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>(columns.size());
		for (Column c: columns) {
			String name = c.getColumnName();
			if (c instanceof AutoTableColumn) {
				AutoTableColumn atc = (AutoTableColumn)c;
				String columnAlias = atc.getColumnAlias();
				if (StringUtils.isNotEmpty(columnAlias)) {
					name = columnAlias;
				}
			}
			
			String propertyName = c.getPropertyName();
			if (StringUtils.isNotEmpty(propertyName)) {
				JdbcType jdbcType = c.getJdbcType();
				Object dbValue = rs.getObject(name);
				
				if (jdbcType != null) {
					Object value = jdbcType.fromDB(dbValue);
					map.put(propertyName, value);	
				} else {
					map.put(propertyName, dbValue);
				}
			}
		}
		
		return new Record(map);
	}

}
