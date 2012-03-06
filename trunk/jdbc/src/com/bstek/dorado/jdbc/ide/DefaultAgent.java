package com.bstek.dorado.jdbc.ide;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.jdbc.config.XmlConstants;
import com.bstek.dorado.jdbc.support.JdbcConstants;
import com.bstek.dorado.util.xml.DomUtils;

public class DefaultAgent extends AbstractAgent {

	@Override
	protected String doListSpaces() throws SQLException {
		DatabaseMetaData dbmd = getDatabaseMetaData();
		ResultSet rs = null;
		String columnName = null;
		if (dbmd.supportsSchemasInDataManipulation()) {
			rs = dbmd.getSchemas(dbmd.getConnection().getCatalog(), null);
			columnName = "TABLE_SCHEM";
		} else {
			rs = dbmd.getCatalogs();
			columnName = "TABLE_CAT";
		}
		
		String spaces = "";
		int index = 0;
		while (rs.next()) {
			String space = rs.getString(columnName);
			index++;
			if (index > 1) {
				spaces += ",";
			}
			spaces += space;
		}
		
		return spaces;
	}

	@Override
	protected String doListTables() throws Exception {
		DatabaseMetaData dbmd = getDatabaseMetaData();
		ResultSet rs = null;
		String catalog = null;
		String schemaPattern = null;
		String tableNamePattern = null;
		String types[] = null;
		if (dbmd.supportsSchemasInDataManipulation()) {
			catalog = dbmd.getConnection().getCatalog();
			schemaPattern = this.getParamerters().get(IAgent.NAMESPACE);
		} else {
			catalog = this.getParamerters().get(IAgent.NAMESPACE);
			schemaPattern = null;
		}
		
		String tableName = this.getParamerters().get(IAgent.TABLE_NAME);
		if (tableName == null || tableName.length() == 0) {
			tableNamePattern = "%";
		} else {
			tableNamePattern = tableName + "%";
		}
		
		if (!dbmd.supportsMixedCaseIdentifiers()) {
			if (dbmd.storesLowerCaseIdentifiers()) {
				tableNamePattern = tableNamePattern.toLowerCase();
			}
			if (dbmd.storesUpperCaseIdentifiers()) {
				tableNamePattern = tableNamePattern.toUpperCase();
			}
		}
		
		rs = dbmd.getTables(catalog, schemaPattern, tableNamePattern, types);
		Document document = newDocument();
		Element tables = (Element) document.appendChild(document.createElement("Tables"));
		while (rs.next()) {
			Element table = (Element) tables.appendChild(document.createElement("Table"));
			String tn = rs.getString("TABLE_NAME");
			String tc = rs.getString("TABLE_CAT");
			String ts = rs.getString("TABLE_SCHEM");
			
			table.setAttribute("name", tn);
			table.setAttribute(XmlConstants.TABLE_NAME, tn);
			if (dbmd.supportsSchemasInDataManipulation()) {
				table.setAttribute(XmlConstants.NAME_SPACE, ts);
			} else {
				table.setAttribute(XmlConstants.NAME_SPACE, tc);
			}
		}
		
		return toString(document);
	}

	@Override
	protected String[] getJdbcTypeNames() {
		return new String[]{"BIT-Boolean","BOOLEAN-Boolean","CHAR-Boolean","SMALLINT-Short","INTEGER-Integer","BIGINT-Long",
				"REAL-Float","FLOAT-Double","DOUBLE-Float","NUMERIC-BigDecimal","DECIMAL-BigDecimal","TINYINT-Byte",
				"DATE-Date","TIME-Date","TIMESTAMP-Date","CHAR-String","VARCHAR-String","LONGVARCHAR-String","CLOB-String"};
	}

	@Override
	protected String[] getKeyGeneratorNames() {
		return new String[]{JdbcConstants.IDENTITY, "JDK-UUID", "SEQUENCE"};
	}

	@Override
	protected Document createTableColumns(Document document) throws Exception {
		DatabaseMetaData dbmd = getDatabaseMetaData();
		
		String namespace = this.getParamerters().get(IAgent.NAMESPACE);
		if (StringUtils.isNotEmpty(namespace)){
			if (dbmd.storesLowerCaseIdentifiers()) {
				namespace = namespace.toLowerCase();
			}
			if (dbmd.storesUpperCaseIdentifiers()) {
				namespace = namespace.toUpperCase();
			}
		}
		
		String catalog = null;
		String schema = null;
		if (dbmd.supportsSchemasInDataManipulation()) {
			schema = namespace;
		} else {
			catalog = namespace;
		}
		
		String tableName = this.getParamerters().get(IAgent.TABLE_NAME);
		if (StringUtils.isNotEmpty(tableName)){
			if (dbmd.storesLowerCaseIdentifiers()) {
				tableName = tableName.toLowerCase();
			}
			if (dbmd.storesUpperCaseIdentifiers()) {
				tableName = tableName.toUpperCase();
			}
		}
		
		ResultSet tableRs = dbmd.getTables(catalog, schema, tableName, null);
		if (!tableRs.next()) {
			throw new IllegalArgumentException("No table be found. [" + catalog +"," + schema + "," + tableName);
		}
		if (tableRs.next()) {
			throw new IllegalArgumentException("More than one table be found. [" + catalog +"," + schema + "," + tableName);
		}
		
		Element tableElement = document.getDocumentElement();
		Element columnsElement = DomUtils.getChildByTagName(tableElement, "Columns");
		if (columnsElement == null) {
			columnsElement = document.createElement("Columns");
			tableElement.appendChild(columnsElement);
		}
		
		List<Element> columnElements = DomUtils.getChildElements(columnsElement);
		Set<String> columnNameSet = new HashSet<String>();
		for (Element element: columnElements) {
			String columnName = element.getAttribute("name");
			columnNameSet.add(columnName);
		}
		
		ResultSet columnRs = dbmd.getPrimaryKeys(catalog, schema, tableName);
		List<String> primaryKeys = new ArrayList<String>();
		try {
			while(columnRs.next()) {
				primaryKeys.add(columnRs.getString("COLUMN_NAME"));
			}
		} finally {
			columnRs.close();
		}
		
		columnRs = dbmd.getColumns(catalog, schema, tableName, null);
		ResultSetMetaData rsmd = columnRs.getMetaData();
		List<Map<String, String>> keyColumns = new ArrayList<Map<String, String>>(primaryKeys.size());
		List<Map<String, String>> commonColumns = new ArrayList<Map<String, String>>();
		
		try {
			while (columnRs.next()) {
				Map<String,String> columnMeta = new LinkedCaseInsensitiveMap<String>();
				for (int i=1,j=rsmd.getColumnCount(); i<=j; i++) {
					String key = JdbcUtils.lookupColumnName(rsmd, i);
					String value = columnRs.getString(i);
					columnMeta.put(key, value);
				}
				
				String columnName = columnMeta.get("COLUMN_NAME");
				String jdbcTypeName = getJdbcTypeName(columnMeta);
				
				Map<String,String> column = new HashMap<String,String>();
				column.put("name", columnName);
				if (StringUtils.isNotEmpty(jdbcTypeName)) {
					column.put("jdbcType", jdbcTypeName);
				}
				
				if (primaryKeys.contains(columnName)) {
					keyColumns.add(column);
				} else {
					commonColumns.add(column);
				}
			}
		} finally {
			columnRs.close();
		}
		
		for (Map<String, String> keyColumn: keyColumns) {
			String columnName = keyColumn.get("name");
			if (!columnNameSet.contains(columnName)) {
				Element element = document.createElement("KeyColumn");
				element.setAttribute("name", columnName);
				String jdbcTypeName = keyColumn.get("jdbcType");
				if (StringUtils.isNotEmpty(jdbcTypeName)) {
					element.setAttribute("jdbcType", jdbcTypeName);
				}
				
				columnsElement.appendChild(element);
			}
		}
		
		for (Map<String, String> commonColumn: commonColumns) {
			String columnName = commonColumn.get("name");
			if (!columnNameSet.contains(columnName)) {
				Element element = document.createElement("Column");
				element.setAttribute("name", columnName);
				String jdbcTypeName = commonColumn.get("jdbcType");
				if (StringUtils.isNotEmpty(jdbcTypeName)) {
					element.setAttribute("jdbcType", jdbcTypeName);
				}
				
				columnsElement.appendChild(element);
			}
		}
		
		return document;
	}
	
	@Override
	protected Document createSqlTableColumns(Document document)
			throws Exception {
		Element tableElement = document.getDocumentElement();
		Element columnsElement = DomUtils.getChildByTagName(tableElement, "Columns");
		if (columnsElement == null) {
			columnsElement = document.createElement("Columns");
			tableElement.appendChild(columnsElement);
		}
		
		List<Element> columnElements = DomUtils.getChildElements(columnsElement);
		Set<String> columnNameSet = new HashSet<String>();
		for (Element element: columnElements) {
			String columnName = element.getAttribute("name");
			columnNameSet.add(columnName);
		}
		
		String querySql = this.getParamerters().get(IAgent.QUERY_SQL);
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		
		List<Map<String, String>> commonColumns = new ArrayList<Map<String, String>>();
		try {
			conn = this.getDataSource().getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(querySql);
			rsmd = rs.getMetaData();
			
			for (int i=1,j=rsmd.getColumnCount(); i<=j; i++) {
				Map<String,String> columnMeta = new HashMap<String,String>(4);
				
				columnMeta.put(JdbcConstants.DATA_TYPE,    String.valueOf(rsmd.getColumnType(i)));
				columnMeta.put(JdbcConstants.TYPE_NAME,    rsmd.getColumnTypeName(i));
				columnMeta.put(JdbcConstants.COLUMN_LABEL, rsmd.getColumnLabel(i));
				columnMeta.put(JdbcConstants.COLUMN_NAME,  rsmd.getColumnName(i));
				columnMeta.put(JdbcConstants.TABLE_NAME,   rsmd.getTableName(i));
				
				commonColumns.add(columnMeta);
			}
		} finally {
			close(rs);
			close(stmt);
			close(conn);
		}
		
		for (Map<String, String> commonColumn: commonColumns) {
			String columnName = commonColumn.get(JdbcConstants.COLUMN_NAME);
			if (StringUtils.isEmpty(columnName)) {
				columnName = commonColumn.get(JdbcConstants.COLUMN_LABEL);
			}
			
			if (StringUtils.isNotEmpty(columnName) && !columnNameSet.contains(columnName)) {
				Element element = document.createElement("Column");
				element.setAttribute("name", columnName);
				String jdbcTypeName = getJdbcTypeName(commonColumn);
				
				if (StringUtils.isNotEmpty(jdbcTypeName)) {
					element.setAttribute("jdbcType", jdbcTypeName);
				}
				
				columnsElement.appendChild(element);
			}
		}
		
		return document;
	}
	
	/**
	 * @see java.sql.DatabaseMetaData#getColumns(String, String, String, String)
	 * @param columnMeta
	 * @return
	 */
	protected String getJdbcTypeName(Map<String,String> columnMeta) {
		String typeStr = columnMeta.get(JdbcConstants.DATA_TYPE);
		int type = Integer.valueOf(typeStr);
		switch (type) {
		case Types.BIT:
			return "BIT-Boolean";
		case Types.BOOLEAN:
			return "BOOLEAN-Boolean";
		case Types.CHAR:
			return "CHAR-Boolean";
		case Types.SMALLINT:
			return "SMALLINT-Short";
		case Types.INTEGER:
			return "INTEGER-Integer";
		case Types.BIGINT:
			return "BIGINT-Long";
		case Types.REAL:
			return "REAL-Float";
		case Types.FLOAT:
			return "FLOAT-Double";
		case Types.DOUBLE:
			return "DOUBLE-Float";
		case Types.NUMERIC:
			return "NUMERIC-BigDecimal";
		case Types.DECIMAL:
			return "DECIMAL-BigDecimal";
		case Types.TINYINT:
			return "TINYINT-Byte";
		case Types.DATE:
			return "DATE-Date";
		case Types.TIME:
			return "TIME-Date";
		case Types.TIMESTAMP:
			return "TIMESTAMP-Date";
		case Types.VARCHAR:
			return "VARCHAR-String";
		case Types.LONGVARCHAR:
			return "LONGVARCHAR-String";
		case Types.CLOB:
			return "CLOB-String";
		default:
			return null;
		}
	}
}
