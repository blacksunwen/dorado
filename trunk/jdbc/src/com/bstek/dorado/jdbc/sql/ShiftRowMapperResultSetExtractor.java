package com.bstek.dorado.jdbc.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.bstek.dorado.data.variant.Record;

public class ShiftRowMapperResultSetExtractor implements ResultSetExtractor<List<Record>>{
	private RowMapper<Record> rowMapper;
	private int maxResults;
	private int firstResult;
	
	public ShiftRowMapperResultSetExtractor(RowMapper<Record> rowMapper,
			int maxResults, int firstResult) {
		this.rowMapper = rowMapper;
		this.maxResults = maxResults;
		this.firstResult = firstResult;
	}

	public List<Record> extractData(ResultSet rs) throws SQLException {
		rs.absolute(firstResult);
		List<Record> records = new ArrayList<Record>(maxResults);
		
		for (int i=1; i<=maxResults; i++) {
			if (rs.next()) {
				Record r = this.rowMapper.mapRow(rs, i);
				records.add(r);
			} else {
				break;
			}
		}
		
		return records;
	}
	
}