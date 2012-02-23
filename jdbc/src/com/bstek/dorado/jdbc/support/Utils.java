package com.bstek.dorado.jdbc.support;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
abstract class Utils {
	static String[] toArray(ResultSet rs, String columnName) throws SQLException {
		List<String> list = new ArrayList<String>();
		try {
			while (rs.next()) {
				String tableType = rs.getString(columnName);
				list.add(tableType);
			}
		} finally {
			rs.close();
		}
		
		return list.toArray(new String[list.size()]);
	}
	
	static List<Map<String,String>> toListMap(ResultSet rs) throws SQLException {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				Map<String,String> s = new LinkedCaseInsensitiveMap<String>(columnCount);
				for (int i = 1; i <= columnCount; i++) {
					String key = JdbcUtils.lookupColumnName(rsmd, i);
					String value = rs.getString(i);
					s.put(key, value);
				}
				list.add(s);
			}
		} finally {
			rs.close();
		}
		
		return list;
	}
}
