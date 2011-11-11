package com.bstek.dorado.jdbc.support;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.bstek.dorado.jdbc.JdbcConstants;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.ModelGenerator;
import com.bstek.dorado.jdbc.key.KeyGenerator;
import com.bstek.dorado.jdbc.model.ColumnDefinition;
import com.bstek.dorado.jdbc.model.table.TableColumnDefinition;
import com.bstek.dorado.jdbc.model.table.TableKeyColumnDefinition;
import com.bstek.dorado.jdbc.type.JdbcType;

public abstract class AbstractModelGenerator implements ModelGenerator {

	@Override
	public String[] listCatalogs(JdbcEnviroment jdbcEnv) {
		DataSource dataSource = jdbcEnv.getDataSource();
		try {
			return (String[])org.springframework.jdbc.support.JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback(){

				@Override
				public Object processMetaData(DatabaseMetaData dbmd)
						throws SQLException, MetaDataAccessException {
					List<String> catalogList = new ArrayList<String>();
					ResultSet rs = dbmd.getCatalogs();
					try {
						while (rs.next()) {
							String catalogName = rs.getString(JdbcConstants.TABLE_CAT);
							catalogList.add(catalogName);
						}
					} finally {
						rs.close();
					}
					
					return catalogList.toArray(new String[catalogList.size()]);
				}
				
			});
		} catch (MetaDataAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String[] listTableTypes(JdbcEnviroment jdbcEnv) {
		DataSource dataSource = jdbcEnv.getDataSource();
		try {
			return (String[])org.springframework.jdbc.support.JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback(){

				@Override
				public Object processMetaData(DatabaseMetaData dbmd)
						throws SQLException, MetaDataAccessException {
					List<String> tableTypeList = new ArrayList<String>();
					ResultSet rs = dbmd.getTableTypes();
					try {
						while (rs.next()) {
							String tableType = rs.getString(JdbcConstants.TABLE_TYPE);
							tableTypeList.add(tableType);
						}
					} finally {
						rs.close();
					}
					
					return tableTypeList.toArray(new String[tableTypeList.size()]);
				}
				
			});
		} catch (MetaDataAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	private String[] defaultTableTypes = new String[]{"TABLE", "VIEW"}; 
	
	@Override
	public String[] defaultTableTypes(JdbcEnviroment jdbcEnv) {
		return defaultTableTypes;
	}

	public String[] getDefaultTableTypes() {
		return defaultTableTypes;
	}

	public void setDefaultTableTypes(String[] defaultTableTypes) {
		this.defaultTableTypes = defaultTableTypes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> listTables(JdbcEnviroment jdbcEnv,
			final String catalogPattern, final String schemaPattern,
			final String tableNamePattern, final String[] types) {
		DataSource dataSource = jdbcEnv.getDataSource();
		try {
			return (List<Map<String, String>>)
				org.springframework.jdbc.support.JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback(){

				@Override
				public Object processMetaData(DatabaseMetaData dbmd)
						throws SQLException, MetaDataAccessException {
					List<Map<String, String>> tableList = new ArrayList<Map<String, String>>();
					ResultSet rs = dbmd.getTables(catalogPattern, schemaPattern, tableNamePattern, types);
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
							tableList.add(s);
						}
					} finally {
						rs.close();
					}
					
					return tableList;
				}
				
			});
		} catch (MetaDataAccessException e) {
			throw new RuntimeException(e);
		}

	}
	
	public String name(Map<String, String> tableMeta, JdbcEnviroment jdbcEnv) {
		return tableMeta.get(JdbcConstants.TABLE_NAME);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> listSchemas(JdbcEnviroment jdbcEnv) {
		DataSource dataSource = jdbcEnv.getDataSource();
		try {
			return (List<Map<String, String>>)
				org.springframework.jdbc.support.JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback(){

				@Override
				public Object processMetaData(DatabaseMetaData dbmd)
						throws SQLException, MetaDataAccessException {
					List<Map<String, String>> list = new ArrayList<Map<String, String>>();
					ResultSet rs = dbmd.getSchemas();
					try {
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
								list.add(s);
							}
						} finally {
							rs.close();
						}
					} finally {
						rs.close();
					}
					
					return list;
				}
				
			});
		} catch (MetaDataAccessException e) {
			throw new RuntimeException(e);
		}

	}
	
	public List<Map<String,String>> listSchemas(JdbcEnviroment jdbcEnv, String catalog) {
		List<Map<String, String>> schemaList = listSchemas(jdbcEnv);
		if (StringUtils.isNotEmpty(catalog)) {
			List<Map<String,String>> schemaList2 = new ArrayList<Map<String, String>>(schemaList.size());
			for (Map<String,String> schema: schemaList) {
				if (catalog.equals(schema.get(JdbcConstants.TABLE_CATALOG))) {
					schemaList2.add(schema);
				}
			}
			schemaList = schemaList2;
		}
		return schemaList; 
	}

	public Map<String,String> singleTableMeta(JdbcEnviroment jdbcEnv, 
			String catalog, String schema, String table) {
		List<Map<String,String>> tables = listTables(jdbcEnv, catalog, schema, table, null);
		if (tables.size() == 0) {
			throw new IllegalArgumentException("no table found from JdbcEnviroment [" + jdbcEnv.getName() + "], " +
					"catalog[" + catalog + "]scheme[" + schema + "]table[" + table + "]");
		} else if (tables.size() != 1) {
			throw new IllegalArgumentException("more than one tables found from JdbcEnviroment [" + jdbcEnv.getName() + "], it is " +tables.size() + ", " +
					"catalog[" + catalog + "]scheme[" + schema + "]table[" + table + "]");
		}
		
		return tables.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> listColumns(final JdbcEnviroment jdbcEnv,
			final String catalog,final String schema, final String table) {
		DataSource dataSource = jdbcEnv.getDataSource();
		try {
			return (List<Map<String, String>>)
				org.springframework.jdbc.support.JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback(){

				@Override
				public Object processMetaData(DatabaseMetaData dbmd)
						throws SQLException, MetaDataAccessException {
					singleTableMeta(jdbcEnv, catalog, schema, table);
					
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
							String columnName = s.get("COLUMN_NAME");
							columnMap.put(columnName, s);
						}
					} finally {
						rs.close();
					}
					
					rs = dbmd.getPrimaryKeys(catalog, schema, table);
					try {
						while(rs.next()) {
							String columnName = rs.getString("COLUMN_NAME");
							Map<String,String> s = columnMap.get(columnName);
							if (s == null) {
								throw new IllegalArgumentException("no column [" + columnName + "] found from table [" + table + "]"+", JdbcEnviroment[" + jdbcEnv.getName() + "]" +
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
		} catch (MetaDataAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public Map<String, String> tableProperties(Map<String, String> tableMeta, JdbcEnviroment jdbcEnv) {
		Map<String,String> st = new HashMap<String,String>();
		String name = name(tableMeta, jdbcEnv);
		st.put("name", name);
		st.put("tableName", tableMeta.get(JdbcConstants.TABLE_NAME));
		st.put("catalog", tableMeta.get(JdbcConstants.TABLE_CAT));
		st.put("schema", tableMeta.get(JdbcConstants.TABLE_SCHEM));
		
		return st;
	}
	
	public ColumnDefinition createColumnDefinition(Map<String, String> tableMeta, Map<String, String> columnProperties, JdbcEnviroment jdbcEnv) {
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
	
	public Map<String, String> columnProperties(Map<String, String> columnMeta, JdbcEnviroment jdbcEnv) {
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
	
	protected abstract String columnName(Map<String,String> columnMeta, JdbcEnviroment jdbcEnv);
	protected abstract String propertyName(Map<String,String> columnMeta, JdbcEnviroment jdbcEnv);
	protected abstract String keyGenerator(Map<String,String> columnMeta, JdbcEnviroment jdbcEnv);
	protected abstract String jdbcType(Map<String,String> columnMeta, JdbcEnviroment jdbcEnv);
	
	public Document createTableDocument(String catalog, String schema, String table, TableGeneratorOption option) {
		Document document = DocumentHelper.createDocument();
		JdbcEnviroment jdbcEnv = option.getJdbcEnviroment();
		Map<String,String> tableMeta = this.singleTableMeta(jdbcEnv, catalog, schema, table);
		List<Map<String,String>> columnMetaList = this.listColumns(jdbcEnv, catalog, schema, table);
		
		String name = name(tableMeta, option.getJdbcEnviroment());
		Element tableElement = document.addElement("Table");
		tableElement.addAttribute("name", name);
		tableElement.addAttribute("tableName", table);

		if (option.isGenerateCatalog() && StringUtils.isNotEmpty(catalog)) {
			tableElement.addAttribute("catalog", catalog);
		}
		if (option.isGenerateSchema() && StringUtils.isNotEmpty(schema)) {
			tableElement.addAttribute("schema", schema);
		}
		
		Element columnsElement = tableElement.addElement("Columns");
		List<Element> keyColumnElementList = new ArrayList<Element>();
		List<Element> columnElementList = new ArrayList<Element>();
		
		for (Map<String,String> columnMeta: columnMetaList) {
			Map<String,String> columnProperties = this.columnProperties(columnMeta, jdbcEnv);
			String elementName = "Column";
			if (JdbcConstants.YES.equalsIgnoreCase(columnProperties.get(JdbcConstants.IS_KEY))) {
				elementName = "KeyColumn";
			}
			columnProperties.remove(JdbcConstants.IS_KEY);
			
			Element element = DocumentHelper.createElement(elementName);
			for (Iterator<String> keyItr = columnProperties.keySet().iterator(); keyItr.hasNext();){
				String key = keyItr.next();
				String value = columnProperties.get(key);
				if (StringUtils.isNotEmpty(value)) {
					element.addAttribute(key, value);
				}
			}
			
			if ("KeyColumn".equals(elementName)) {
				keyColumnElementList.add(element);
			} else {
				columnElementList.add(element);
			}
		}
		
		for (Element element: keyColumnElementList) {
			columnsElement.add(element);
		}
		for (Element element: columnElementList) {
			columnsElement.add(element);
		}
		
		return document;
	}
	
	public Document createSqlTableDocument(final JdbcEnviroment jdbcEnv, String sql) {
		Document document = DocumentHelper.createDocument();
		final Element rootElement = document.addElement("SqlTable");
		
		JdbcTemplate jdbcTemplate = jdbcEnv.getNamedDao().getJdbcTemplate();
		jdbcTemplate.query(sql, new ResultSetExtractor<Object>(){

			@Override
			public Object extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				ResultSetMetaData rsmd = rs.getMetaData();
				 
				for(int i=1; i<=rsmd.getColumnCount(); i++){
					Map<String,String> columnMeta = new HashMap<String,String>();
					
					columnMeta.put(JdbcConstants.DATA_TYPE,String.valueOf(rsmd.getColumnType(i)));
					columnMeta.put(JdbcConstants.TYPE_NAME, rsmd.getColumnTypeName(i));
					columnMeta.put(JdbcConstants.COLUMN_LABEL, rsmd.getColumnLabel(i));
					columnMeta.put(JdbcConstants.COLUMN_NAME, rsmd.getColumnName(i));
					
					Map<String,String> columnProperties = columnProperties(columnMeta, jdbcEnv);
					columnProperties.put("nativeColumnName", columnMeta.get(JdbcConstants.COLUMN_NAME));
					Element element = DocumentHelper.createElement("Column");
					for (Iterator<String> keyItr = columnProperties.keySet().iterator(); keyItr.hasNext();){
						String key = keyItr.next();
						String value = columnProperties.get(key);
						if (StringUtils.isNotEmpty(value)) {
							element.addAttribute(key, value);
						}
					}
					rootElement.add(element);
				}
				
				return null;
			}
			
		});
		return document;
	}
}
