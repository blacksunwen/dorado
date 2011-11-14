package com.bstek.dorado.jdbc.support;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcConstants;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.meta.SqlTableMetaDataGenerator;
import com.bstek.dorado.jdbc.type.JdbcType;

public class DefaultSqlTableMetaDataGenerator implements
		SqlTableMetaDataGenerator {

	@Override
	public Map<String, String> columnProperties(Map<String, String> columnMeta,
			JdbcEnviroment jdbcEnv) {
		Map<String, String> properties = new HashMap<String, String>(3);
		
		String columnName = this.columnName(columnMeta, jdbcEnv);
		properties.put("columnName", columnName);
		
		String propertyName = this.propertyName(columnMeta, jdbcEnv);
		properties.put("propertyName", propertyName);
		
		String jdbcType = this.jdbcType(columnMeta, jdbcEnv);
		properties.put("jdbcType", jdbcType);

		return properties;
	}
	
	protected String columnName(Map<String,String> column, JdbcEnviroment jdbcEnv) {
		String label = column.get(JdbcConstants.COLUMN_LABEL);
		if (StringUtils.isEmpty(label)) {
			return column.get(JdbcConstants.COLUMN_NAME);
		} else {
			return label;
		}
	}
	
	protected String propertyName(Map<String,String> column, JdbcEnviroment jdbcEnv) {
		return columnName(column, jdbcEnv);
	}
	
	protected String jdbcType(Map<String,String> column, JdbcEnviroment jdbcEnv) {
		String dataType = column.get(JdbcConstants.DATA_TYPE);
		if (StringUtils.isNotEmpty(dataType)) {
			int code = Integer.valueOf(dataType);
			Dialect dialect = jdbcEnv.getDialect();
			List<JdbcType> jdbcTypes = dialect.getJdbcTypes();
			for (JdbcType jdbcType: jdbcTypes) {
				if (code == jdbcType.getSqlType()) {
					return jdbcType.getName();
				}
			}
		}
		
		return null;
	}
	
	public List<Map<String,String>> listColumnMetas(JdbcEnviroment jdbcEnv, String sql) {
		JdbcTemplate jdbcTemplate = jdbcEnv.getNamedDao().getJdbcTemplate();
		return jdbcTemplate.query(sql, new ResultSetExtractor<List<Map<String,String>>>(){

			@Override
			public List<Map<String,String>> extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				ResultSetMetaData rsmd = rs.getMetaData();
				
				List<Map<String,String>> metas = new ArrayList<Map<String,String>>(rsmd.getColumnCount());
				for(int i=1; i<=rsmd.getColumnCount(); i++){
					Map<String,String> columnMeta = columnMeta(rsmd, i);
					metas.add(columnMeta);
				}
				
				return metas;
			}
			
		});
	}
	
	protected Map<String, String> columnMeta(ResultSetMetaData rsmd, int i) throws SQLException {
		Map<String,String> columnMeta = new HashMap<String,String>(4);
		
		columnMeta.put(JdbcConstants.DATA_TYPE,String.valueOf(rsmd.getColumnType(i)));
		columnMeta.put(JdbcConstants.TYPE_NAME, rsmd.getColumnTypeName(i));
		columnMeta.put(JdbcConstants.COLUMN_LABEL, rsmd.getColumnLabel(i));
		columnMeta.put(JdbcConstants.COLUMN_NAME, rsmd.getColumnName(i));
		
		return columnMeta;
	}
	
	@Override
	public Document createDocument(final JdbcEnviroment jdbcEnv, String sql) {
		Document document = DocumentHelper.createDocument();
		Element tableElement = document.addElement("SqlTable");
		Element columnsElement = tableElement.addElement("Columns");
		
		List<Map<String,String>> columnMetas = this.listColumnMetas(jdbcEnv, sql);
		for (Map<String,String> columnMeta: columnMetas) {
			Map<String,String> columnProperties = columnProperties(columnMeta, jdbcEnv);
			columnProperties.put("nativeColumnName", columnMeta.get(JdbcConstants.COLUMN_NAME));
			Element columnElement = DocumentHelper.createElement("Column");
			for (Iterator<String> keyItr = columnProperties.keySet().iterator(); keyItr.hasNext();){
				String key = keyItr.next();
				String value = columnProperties.get(key);
				if (StringUtils.isNotEmpty(value)) {
					columnElement.addAttribute(key, value);
				}
			}
			columnsElement.add(columnElement);
		}
		
		return document;
	}

	@Override
	public Document mergeDocument(JdbcEnviroment jdbcEnv, String sql,
			Document document) {
		
		return null;
	}

}
