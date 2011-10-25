package com.bstek.dorado.jdbc.support;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcConstants;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.model.ColumnDefinition;
import com.bstek.dorado.jdbc.model.table.TableDefinition;
import com.bstek.dorado.jdbc.type.JdbcType;

public class DefaultModelGenerator extends AbstractModelGenerator {

	@Override
	protected void init(TableDefinition def, Map<String,String> tableObj, 
			String catalog, String schema, String table, TableGeneratorOption option) {
		String name = tableName(tableObj, option.getJdbcEnviroment());
		this.setName(def, name);
		
		String tableName = tableObj.get("TABLE_NAME");
		def.getProperties().put("tableName", tableName);
		
		if (option.isGenerateCatalog() && StringUtils.isNotEmpty(catalog)) {
			def.getProperties().put("catalog", catalog);
		}
		if (option.isGenerateSchema() && StringUtils.isNotEmpty(schema)) {
			def.getProperties().put("schema", schema);
		}
	}
	
	@Override
	protected void init(TableDefinition def, Map<String,String> table, 
			List<Map<String,String>> columnList, TableGeneratorOption option) {
		JdbcEnviroment jdbcEnv = option.getJdbcEnviroment();
		for (Map<String,String> column: columnList) {
			ColumnDefinition columnDef = column(table, column, jdbcEnv);
			if (columnDef != null) {
				def.addInitOperation(columnDef);
			}
		}
	}

	@Override
	protected String columnName(Map<String,String> column, Map<String,String> table, JdbcEnviroment jdbcEnv) {
		return column.get(JdbcConstants.COLUMN_NAME);
	}
	
	@Override
	protected String tableName(Map<String, String> table, JdbcEnviroment jdbcEnv) {
		return table.get("TABLE_NAME");
	}

	@Override
	protected String propertyName(Map<String,String> column, Map<String,String> table, JdbcEnviroment jdbcEnv) {
		return columnName(column, table, jdbcEnv);
	}
	
	@Override
	protected String keyGenerator(Map<String,String> column, Map<String,String> table, JdbcEnviroment jdbcEnv) {
		if (JdbcConstants.YES.equalsIgnoreCase(column.get(JdbcConstants.IS_AUTOINCREMENT))) {
			return JdbcConstants.IDENTITY;
		} else {
			return null;
		}
	}
	
	@Override
	protected String jdbcType(Map<String,String> column, Map<String,String> table, JdbcEnviroment jdbcEnv) {
		String dataType = column.get(JdbcConstants.DATA_TYPE);
		if (StringUtils.isNotEmpty(dataType)) {
			int code = Integer.valueOf(dataType);
			Dialect dialect = jdbcEnv.getDialect();
			List<JdbcType> jdbcTypes = dialect.getJdbcTypes();
			for (JdbcType jdbcType: jdbcTypes) {
				if (code == jdbcType.getJdbcCode()) {
					return jdbcType.getName();
				}
			}
		}
		
		return null;
	}
}
