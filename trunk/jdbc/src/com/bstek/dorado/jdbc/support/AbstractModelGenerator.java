package com.bstek.dorado.jdbc.support;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.bstek.dorado.jdbc.JdbcConstants;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.ModelGenerator;
import com.bstek.dorado.jdbc.key.KeyGenerator;
import com.bstek.dorado.jdbc.model.ColumnDefinition;
import com.bstek.dorado.jdbc.model.DbElementDefinition;
import com.bstek.dorado.jdbc.model.sqltable.SqlTableDefinition;
import com.bstek.dorado.jdbc.model.table.TableColumnDefinition;
import com.bstek.dorado.jdbc.model.table.TableDefinition;
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
							String catalogName = rs.getString("TABLE_CAT");
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
		return this.defaultTableTypes(jdbcEnv);
	}
	
	protected String[] listTableTypesFromDb(JdbcEnviroment jdbcEnv) {
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
							String tableType = rs.getString("TABLE_TYPE");
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
					List<Map<String, String>> catalogList = new ArrayList<Map<String, String>>();
					ResultSet rs = dbmd.getSchemas();
					try {
						while (rs.next()) {
							Map<String,String> s = new HashMap<String,String>(2);
							String catalogName = rs.getString("TABLE_CATALOG");
							String schemaName = rs.getString("TABLE_SCHEM");
							s.put("TABLE_CATALOG", catalogName);
							s.put("TABLE_SCHEM", schemaName);
							catalogList.add(s);
						}
					} finally {
						rs.close();
					}
					
					return catalogList;
				}
				
			});
		} catch (MetaDataAccessException e) {
			throw new RuntimeException(e);
		}

	}

	public Map<String,String> singleTable(JdbcEnviroment jdbcEnv, 
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
					singleTable(jdbcEnv, catalog, schema, table);
					
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

	public TableDefinition createTable(String catalog, String schema, String table, TableGeneratorOption option) {
		TableDefinition def = new TableDefinition();
		JdbcEnviroment jdbcEnv = option.getJdbcEnviroment();
		
		List<Map<String,String>> tables = listTables(jdbcEnv, catalog, schema, table, null);
		if (tables.size() == 0) {
			throw new IllegalArgumentException("no table found from JdbcEnviroment [" + jdbcEnv.getName() + "], " +
					"catalog[" + catalog + "]scheme[" + schema + "]table[" + table + "]");
		} else if (tables.size() != 1) {
			throw new IllegalArgumentException("more than one tables found from JdbcEnviroment [" + jdbcEnv.getName() + "], it is " +tables.size() + ", " +
					"catalog[" + catalog + "]scheme[" + schema + "]table[" + table + "]");
		}
		
		Map<String,String> tableObj = tables.get(0);
		this.init(def, tableObj, catalog, schema, table, option);
		
		List<Map<String,String>> columnList = this.listColumns(jdbcEnv, catalog, schema, table);
		this.init(def, tableObj, columnList, option);
		
		return def;
	}
	
	protected abstract void init(TableDefinition def, Map<String,String> tableObj, 
			String catalog, String schema, String table, TableGeneratorOption option);
	
	protected abstract void init(TableDefinition def, Map<String,String> tableObj, 
			List<Map<String,String>> columnList, TableGeneratorOption option);
	
	protected void setName(DbElementDefinition def, String name) {
		def.getProperties().put("name", name);
	}
	
	public ColumnDefinition column(Map<String, String> table, Map<String, String> column, JdbcEnviroment jdbcEnv) {
		ColumnDefinition columnDef = null;
		if (JdbcConstants.YES.equalsIgnoreCase(column.get(JdbcConstants.IS_KEY))) {
			columnDef = new TableKeyColumnDefinition();
		} else {
			columnDef = new TableColumnDefinition();
		}
		
		String columnName = this.columnName(column, table, jdbcEnv);
		columnDef.getProperties().put("columnName", columnName);
		
		String propertyName = this.propertyName(column, table, jdbcEnv);
		if (StringUtils.isNotEmpty(propertyName)) {
			columnDef.getProperties().put("propertyName", propertyName);
		}
		
		String jdbcType = this.jdbcType(column, table, jdbcEnv);
		if (StringUtils.isNotEmpty(jdbcType)) {
			JdbcType jdbcTypeObj = jdbcEnv.getDialect().getJdbcType(jdbcType);
			columnDef.getProperties().put("jdbcType", jdbcTypeObj);
		}
		
		if (JdbcConstants.YES.equalsIgnoreCase(column.get(JdbcConstants.IS_KEY))) {
			String keyGenerator = this.keyGenerator(column, table, jdbcEnv);
			if (StringUtils.isNotEmpty(keyGenerator)) {
				KeyGenerator<Object> kg = jdbcEnv.getDialect().getKeyGenerator(keyGenerator);
				columnDef.getProperties().put("keyGenerator", kg);
			}
		}
		return columnDef;
	}
	
	protected abstract String tableName(Map<String,String> table, JdbcEnviroment jdbcEnv);
	protected abstract String columnName(Map<String,String> column, Map<String,String> table, JdbcEnviroment jdbcEnv);
	protected abstract String propertyName(Map<String,String> column, Map<String,String> table, JdbcEnviroment jdbcEnv);
	protected abstract String keyGenerator(Map<String,String> column, Map<String,String> table, JdbcEnviroment jdbcEnv);
	protected abstract String jdbcType(Map<String,String> column, Map<String,String> table, JdbcEnviroment jdbcEnv);
	
	@Override
	public SqlTableDefinition createSqlTable(JdbcEnviroment jdbcEnv, String sql) {
		// TODO Auto-generated method stub
		return null;
	}
}
