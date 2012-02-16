package com.bstek.dorado.jdbc.support;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
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

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.config.ColumnDefinition;
import com.bstek.dorado.jdbc.config.DomHelper;
import com.bstek.dorado.jdbc.meta.TableMetaDataGenerator;
import com.bstek.dorado.jdbc.model.table.KeyGenerator;
import com.bstek.dorado.jdbc.model.table.TableColumnDefinition;
import com.bstek.dorado.jdbc.model.table.TableKeyColumnDefinition;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.util.xml.DomUtils;

public class DefaultTableMetaDataGenerator implements TableMetaDataGenerator {

	private static Log logger = LogFactory.getLog(DefaultTableMetaDataGenerator.class);
	
	@Override
	public List<Map<String, String>> listTableMetas(JdbcEnviroment jdbcEnv,
			final String catalogPattern, final String schemaPattern,
			final String tableNamePattern, final String[] types) {
		DataSource dataSource = jdbcEnv.getDataSource();
		try {
			@SuppressWarnings("unchecked")
			List<Map<String, String>> metaData = (List<Map<String, String>>)
				org.springframework.jdbc.support.JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback(){

				@Override
				public Object processMetaData(DatabaseMetaData dbmd)
						throws SQLException, MetaDataAccessException {
					ResultSet rs = dbmd.getTables(catalogPattern, schemaPattern, tableNamePattern, types);
					return Utils.toListMap(rs);
				}
				
			});
			return metaData;
		} catch (MetaDataAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Map<String, String> tableMeta(JdbcEnviroment jdbcEnv,
			String catalog, String schema, String table) {
		List<Map<String,String>> tables = listTableMetas(jdbcEnv, catalog, schema, table, null);
		if (tables.size() == 0) {
			throw new IllegalArgumentException("no table found from JdbcEnviroment [" + jdbcEnv.getName() + "], " +
					"catalog[" + catalog + "]scheme[" + schema + "]table[" + table + "]");
		} else if (tables.size() != 1) {
			throw new IllegalArgumentException("more than one tables found from JdbcEnviroment [" + jdbcEnv.getName() + "], it is " +tables.size() + ", " +
					"catalog[" + catalog + "]scheme[" + schema + "]table[" + table + "]");
		}
		
		
		Map<String, String> meta = tables.get(0);
		if (logger.isDebugEnabled()) {
			logger.debug("TABLE-META:" + meta);
		}
		return meta;
	}

	@Override
	public String tableName(Map<String, String> tableMeta, JdbcEnviroment jdbcEnv) {
		return tableMeta.get(JdbcConstants.TABLE_NAME);
	}

	@Override
	public List<Map<String, String>> listColumnMetas(final JdbcEnviroment jdbcEnv,
			final String catalog, final String schema, final String table) {
		
		this.tableMeta(jdbcEnv, catalog, schema, table);
		
		DataSource dataSource = jdbcEnv.getDataSource();
		try {
			@SuppressWarnings("unchecked")
			List<Map<String, String>> metaData = (List<Map<String, String>>)
				org.springframework.jdbc.support.JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback(){

				@Override
				public Object processMetaData(DatabaseMetaData dbmd)
						throws SQLException, MetaDataAccessException {
					Map<String, Map<String, String>> columnMap = new HashMap<String, Map<String, String>>();
					ResultSet rs = dbmd.getColumns(catalog, schema, table, null);
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					try {
						while (rs.next()) {
							Map<String,String> s = new LinkedCaseInsensitiveMap<String>(columnCount);
							for (int i = 1; i <= columnCount; i++) {
								String key = JdbcUtils.lookupColumnName(rsmd, i);
								String value = rs.getString(i);
								s.put(key, value);
							}
							s.put(JdbcConstants.IS_KEY, JdbcConstants.NO);
							String columnName = s.get(JdbcConstants.COLUMN_NAME);
							columnMap.put(columnName, s);
						}
					} finally {
						rs.close();
					}
					
					rs = dbmd.getPrimaryKeys(catalog, schema, table);
					try {
						while(rs.next()) {
							String columnName = rs.getString(JdbcConstants.COLUMN_NAME);
							Map<String,String> s = columnMap.get(columnName);
							if (s == null) {
								throw new IllegalArgumentException("no column [" + columnName + "] found from table [" + table + "], " +
										"JdbcEnviroment[" + jdbcEnv.getName() + "]" +
										"catalog[" + catalog + "]scheme[" + schema + "]table[" + table + "]");
							}
							
							s.put(JdbcConstants.IS_KEY, JdbcConstants.YES);
						}
					} finally {
						rs.close();
					}
					
					return new ArrayList<Map<String, String>>(columnMap.values());
				}
				
			});
			if (logger.isDebugEnabled()) {
				for (Map<String,String> meta: metaData) {
					logger.debug("COLUMN-META:" + meta);
				}
			}
			return metaData;
		} catch (MetaDataAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Map<String, String> columnProperties(Map<String, String> columnMeta,
			JdbcEnviroment jdbcEnv) {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(JdbcConstants.IS_KEY, columnMeta.get(JdbcConstants.IS_KEY));
		
		String columnName = this.columnName(columnMeta, jdbcEnv);
		properties.put("columnName", columnName);
		
		String propertyName = this.propertyName(columnMeta, jdbcEnv);
		properties.put("propertyName", propertyName);
		
		String jdbcType = this.jdbcType(columnMeta, jdbcEnv);
		properties.put("jdbcType", jdbcType);
		
		if (JdbcConstants.YES.equalsIgnoreCase(columnMeta.get(JdbcConstants.IS_KEY))) {
			String keyGenerator = this.keyGenerator(columnMeta, jdbcEnv);
			properties.put("keyGenerator", keyGenerator);
		}
		
		return properties;
	}

	@Override
	public ColumnDefinition createColumnDefinition(
			Map<String, String> tableMeta,
			Map<String, String> columnProperties, JdbcEnviroment jdbcEnv) {
		ColumnDefinition columnDef = null;
		if (JdbcConstants.YES.equalsIgnoreCase(columnProperties.get(JdbcConstants.IS_KEY))) {
			columnDef = new TableKeyColumnDefinition();
		} else {
			columnDef = new TableColumnDefinition();
		}
		
		String columnName = columnProperties.get("columnName");
		columnDef.getProperties().put("columnName", columnName);
		
		String propertyName = columnProperties.get("propertyName");
		if (StringUtils.isNotEmpty(propertyName)) {
			columnDef.getProperties().put("propertyName", propertyName);
		}
		
		String jdbcType = columnProperties.get("jdbcType");
		if (StringUtils.isNotEmpty(jdbcType)) {
			JdbcType jdbcTypeObj = jdbcEnv.getDialect().getJdbcType(jdbcType);
			columnDef.getProperties().put("jdbcType", jdbcTypeObj);
		}
		
		if (JdbcConstants.YES.equalsIgnoreCase(columnProperties.get(JdbcConstants.IS_KEY))) {
			String keyGenerator = columnProperties.get("keyGenerator");
			if (StringUtils.isNotEmpty(keyGenerator)) {
				KeyGenerator<Object> kg = jdbcEnv.getDialect().getKeyGenerator(keyGenerator);
				columnDef.getProperties().put("keyGenerator", kg);
			}
		}
		
		return columnDef;
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
		return jdbcEnv.getDialect().propertyName(column);
	}
	
	protected String keyGenerator(Map<String,String> column, JdbcEnviroment jdbcEnv) {
		if (JdbcConstants.YES.equalsIgnoreCase(column.get(JdbcConstants.IS_AUTOINCREMENT))) {
			return JdbcConstants.IDENTITY;
		} else {
			return null;
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
	
	private static class UsedTable {
		String catalog; String schema; String table;
		String usedCatalog; String usedSchema;String usedTable;
		
		public UsedTable(String catalog, String schema, String table) {
			this.catalog = catalog;
			this.schema = schema;
			this.table = table;
			
			this.usedCatalog = catalog;
			this.usedSchema = schema;
			this.usedTable = table;
		}
		
		public void init(JdbcEnviroment jdbcEnv) {
			DataSource dataSource = jdbcEnv.getDataSource();
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				DatabaseMetaData dbmd = conn.getMetaData();
				if (dbmd.supportsCatalogsInDataManipulation()) {
					if (StringUtils.isEmpty(usedCatalog)) {
						usedCatalog = jdbcEnv.getDialect().defaultCatalog(jdbcEnv.getDataSource(), dbmd);
					}
					
					if (!dbmd.supportsMixedCaseIdentifiers()) {
						if (dbmd.storesLowerCaseIdentifiers()) {
							if (StringUtils.isNotEmpty(usedCatalog)){
								usedCatalog = usedCatalog.toLowerCase();
							} 
						}
						if (dbmd.storesUpperCaseIdentifiers()) {
							if (StringUtils.isNotEmpty(usedCatalog)){
								usedCatalog = usedCatalog.toUpperCase();
							}
						}
					}
				}
				
				if (dbmd.supportsSchemasInDataManipulation()) {
					if (StringUtils.isEmpty(usedSchema)) {
						usedSchema = jdbcEnv.getDialect().defaultSchema(jdbcEnv.getDataSource(), dbmd);
					}
					
					if (!dbmd.supportsMixedCaseIdentifiers()) {
						if (dbmd.storesLowerCaseIdentifiers()) {
							if (StringUtils.isNotEmpty(usedSchema)){
								usedSchema = usedSchema.toLowerCase();
							} 
						}
						if (dbmd.storesUpperCaseIdentifiers()) {
							if (StringUtils.isNotEmpty(usedSchema)){
								usedSchema = usedSchema.toUpperCase();
							}
						}
					}
				}
				
				if (!dbmd.supportsMixedCaseIdentifiers()) {
					if (dbmd.storesLowerCaseIdentifiers()) {
						if (StringUtils.isNotEmpty(usedTable)){
							usedTable = usedTable.toLowerCase();
						} 
					}
					if (dbmd.storesUpperCaseIdentifiers()) {
						if (StringUtils.isNotEmpty(usedTable)){
							usedTable = usedTable.toUpperCase();
						}
					}
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
					}
				}
			}
		}
	}
	@Override
	public Document createDocument(String catalog, String schema, String table,
			JdbcEnviroment jdbcEnv) {
		UsedTable usedTable = new UsedTable(catalog, schema, table);
		usedTable.init(jdbcEnv);
		
		Document document = DomHelper.newDocument();
		Element tableElement = this.createTableElement(usedTable, jdbcEnv, document);
		document.appendChild(tableElement);
		
		List<Element> keyColumnElementList = new ArrayList<Element>();
		List<Element> columnElementList = new ArrayList<Element>();

		List<Map<String,String>> columnMetaList = this.listColumnMetas(jdbcEnv, usedTable.usedCatalog, usedTable.usedSchema, usedTable.usedTable);
		for (Map<String,String> columnMeta: columnMetaList) {
			Element element = createColumnElement(columnMeta, jdbcEnv, document);
			if (element.getNodeName().equals("KeyColumn")) {
				keyColumnElementList.add(element);
			} else {
				columnElementList.add(element);
			}
		}
		
		Element columnsElement = DomHelper.addElement(tableElement, "Columns");
		for (Element element: keyColumnElementList) {
			columnsElement.appendChild(element);
		}
		for (Element element: columnElementList) {
			columnsElement.appendChild(element);
		}
		
		return document;
	}
	
	protected Element createTableElement(UsedTable usedTable, JdbcEnviroment jdbcEnv, Document document) {
		Map<String,String> tableMeta = this.tableMeta(jdbcEnv, usedTable.usedCatalog, usedTable.usedSchema, usedTable.usedTable);
		
		String name = tableName(tableMeta, jdbcEnv);
		Element tableElement = document.createElement("Table");
		tableElement.setAttribute("name", name);
		tableElement.setAttribute("tableName", usedTable.table);

		if (StringUtils.isNotEmpty(usedTable.catalog)) {
			tableElement.setAttribute("catalog", usedTable.catalog);
		}
		if (StringUtils.isNotEmpty(usedTable.schema)) {
			tableElement.setAttribute("schema", usedTable.schema);
		}
		
		return tableElement;
	}
	
	protected Element createColumnElement(Map<String,String> columnMeta, JdbcEnviroment jdbcEnv, Document document) {
		Map<String,String> columnProperties = this.columnProperties(columnMeta, jdbcEnv);
		Element element = null;
		if (JdbcConstants.YES.equalsIgnoreCase(columnProperties.get(JdbcConstants.IS_KEY))) {
			String elementName = "KeyColumn";
			element = document.createElement(elementName);
		} else {
			String elementName = "Column";
			element = document.createElement(elementName);
		}
		columnProperties.remove(JdbcConstants.IS_KEY);
		
		for (Iterator<String> keyItr = columnProperties.keySet().iterator(); keyItr.hasNext();){
			String key = keyItr.next();
			String value = columnProperties.get(key);
			if (StringUtils.isNotEmpty(value)) {
				element.setAttribute(key, value);
			}
		}
		
		return element;
	}

	@Override
	public Document mergeDocument(String catalog, String schema, String table,
			JdbcEnviroment jdbcEnv, Document document) {
		UsedTable usedTable = new UsedTable(catalog, schema, table);
		usedTable.init(jdbcEnv);
		
		Element tableElement = document.getDocumentElement();
		Element columnsElement = DomUtils.getChildByTagName(tableElement, "Columns");
		if (columnsElement == null) {
			columnsElement = document.createElement("Columns");
			tableElement.appendChild(columnsElement);
		}
		
		Set<String> columnNameSet = new HashSet<String>();
		List<Element> columnElements = DomUtils.getChildElements(columnsElement);
		for (Element columnElement: columnElements) {
			String columnName = columnElement.getAttribute("columnName");
			columnNameSet.add(columnName);
		}
		
		List<Map<String,String>> columnMetaList = this.listColumnMetas(jdbcEnv, usedTable.usedCatalog, usedTable.usedSchema, usedTable.usedTable);
		for (Map<String,String> columnMeta: columnMetaList) {
			Element columnElement = createColumnElement(columnMeta, jdbcEnv, document);
			String columnName = columnElement.getAttribute("columnName");
			if (!columnNameSet.contains(columnName)) {
				columnsElement.appendChild(columnElement);
			}
		}
		
		return document;
	}

}
