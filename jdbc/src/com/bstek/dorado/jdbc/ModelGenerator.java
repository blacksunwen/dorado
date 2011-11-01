package com.bstek.dorado.jdbc;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;

import com.bstek.dorado.jdbc.model.ColumnDefinition;
import com.bstek.dorado.jdbc.support.TableGeneratorOption;

public interface ModelGenerator {

	String[] listCatalogs(JdbcEnviroment jdbcEnv);
	
	String[] listTableTypes(JdbcEnviroment jdbcEnv);
	
	String[] defaultTableTypes(JdbcEnviroment jdbcEnv);
	
	/**
	 * Each table description has the following columns:
     * <OL>
	 *  <LI><B>TABLE_CAT</B> String => table catalog (may be <code>null</code>)
     *	<LI><B>TABLE_SCHEM</B> String => table schema (may be <code>null</code>)
     *	<LI><B>TABLE_NAME</B> String => table name
     *	<LI><B>TABLE_TYPE</B> String => table type.  Typical types are "TABLE",
     *			"VIEW",	"SYSTEM TABLE", "GLOBAL TEMPORARY", 
     *			"LOCAL TEMPORARY", "ALIAS", "SYNONYM".
     *	<LI><B>REMARKS</B> String => explanatory comment on the table
     * </OL>
	 * @param jdbcEnv
	 * @param catalogPattern
	 * @param schemaPattern
	 * @param tableNamePattern
	 * @param types
	 * @return
	 */
	List<Map<String,String>> listTables(JdbcEnviroment jdbcEnv, 
			String catalogPattern, String schemaPattern,
            String tableNamePattern, String[] types);
	
	Map<String,String> singleTable(JdbcEnviroment jdbcEnv, 
			String catalog, String schema, String table);
	
	/**
	 * The schema columns are:
     * <OL>
     *	<LI><B>TABLE_SCHEM</B> String => schema name
     *  <LI><B>TABLE_CATALOG</B> String => catalog name (may be <code>null</code>)
     * </OL>
	 * @param jdbcEnv
	 * @return
	 */
	List<Map<String,String>> listSchemas(JdbcEnviroment jdbcEnv);
	
	/**
	 * 根据catalog返回Schema列表
	 * @param jdbcEnv
	 * @param catalog
	 * @return
	 */
	List<Map<String,String>> listSchemas(JdbcEnviroment jdbcEnv, String catalog);
	
	String name(Map<String,String> tableMeta, JdbcEnviroment jdbcEnv);
	
	/**
	 * Each column description has the following columns:
	 * <OL>
	 * <LI><B>COLUMN_NAME</B> String => column name
     * <LI><B>DATA_TYPE</B> int => SQL type from java.sql.Types
     * <LI><B>TYPE_NAME</B> String => Data source dependent type name,for a UDT the type name is fully qualified
     * <LI><B>IS_KEY</B> String  => Indicates whether this column is a pri key column
	 * </OL>
	 * @param jdbcEnv
	 * @param catalog
	 * @param schema
	 * @param tableName
	 * @return
	 */
	List<Map<String,String>> listColumns(JdbcEnviroment jdbcEnv, String catalog, String schema, String tableName);
	
	ColumnDefinition createColumnDefinition(Map<String, String> tableMeta, Map<String, String> columnProperties, JdbcEnviroment jdbcEnv);

	Map<String, String> columnProperties(Map<String, String> tableMeta, Map<String, String> columnMeta, JdbcEnviroment jdbcEnv);
	
	Document createTableDocument(String catalog, String schema, String table, TableGeneratorOption option);
}
