package com.bstek.dorado.jdbc.support;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.config.DomHelper;
import com.bstek.dorado.jdbc.meta.SqlTableMetaDataGenerator;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.util.xml.DomUtils;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class DefaultSqlTableMetaDataGenerator implements
		SqlTableMetaDataGenerator {

	protected Map<String, String> columnProperties(Map<String, String> columnMeta,
			JdbcEnviroment jdbcEnv) {
		Map<String, String> properties = new HashMap<String, String>(3);
		
		String columnName = this.columnName(columnMeta, jdbcEnv);
		properties.put("columnName", columnName);
		
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

	protected String jdbcType(Map<String,String> column, JdbcEnviroment jdbcEnv) {
		JdbcType jdbcType = jdbcEnv.getDialect().jdbcType(column);
		if (jdbcType != null) {
			return jdbcType.getName();
		} else {
			return null;
		}
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
	public Document create(JdbcEnviroment jdbcEnv, String sql) {
		Document document = DomHelper.newDocument();
		Element tableElement = DomHelper.addElement(document, "SqlTable");
		Element columnsElement = DomHelper.addElement(tableElement, "Columns");
		
		List<Map<String,String>> columnMetas = this.listColumnMetas(jdbcEnv, sql);
		for (Map<String,String> columnMeta: columnMetas) {
			Element columnElement = createColumnElement(columnMeta, jdbcEnv, document);
			columnsElement.appendChild(columnElement);
		}
		
		return document;
	}

	protected Element createColumnElement(Map<String, String> columnMeta, JdbcEnviroment jdbcEnv, Document document) {
		Map<String,String> columnProperties = columnProperties(columnMeta, jdbcEnv);
		columnProperties.put("nativeColumnName", columnMeta.get(JdbcConstants.COLUMN_NAME));
		Element columnElement = document.createElement("Column");
		for (Iterator<String> keyItr = columnProperties.keySet().iterator(); keyItr.hasNext();){
			String key = keyItr.next();
			String value = columnProperties.get(key);
			if (StringUtils.isNotEmpty(value)) {
				columnElement.setAttribute(key, value);
			}
		}
		return columnElement;
	}

	@Override
	public Document merge(JdbcEnviroment jdbcEnv, String sql,
			Document document) {
		Element tableElement = document.getDocumentElement();
		Element columnsElement = DomUtils.getChildByTagName(tableElement, "Columns");
		if (columnsElement == null) {
			columnsElement = DomHelper.addElement(tableElement, "Columns");
		}
		
		Set<String> columnNameSet = new HashSet<String>();
		List<Element> columnElements = DomUtils.getChildElements(columnsElement);
		for (Element columnElement: columnElements) {
			String columnName = columnElement.getAttribute("columnName");
			columnNameSet.add(columnName);
		}
		
		List<Map<String,String>> columnMetas = this.listColumnMetas(jdbcEnv, sql);
		for (Map<String,String> columnMeta: columnMetas) {
			Element columnElement = createColumnElement(columnMeta, jdbcEnv, document);
			String columnName = columnElement.getAttribute("columnName");
			if (!columnNameSet.contains(columnName)) {
				columnsElement.appendChild(columnElement);
			}
		}
		
		return document;
	}

}
