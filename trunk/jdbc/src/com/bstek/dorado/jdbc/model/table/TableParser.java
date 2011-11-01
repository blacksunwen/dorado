package com.bstek.dorado.jdbc.model.table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.Operation;
import com.bstek.dorado.jdbc.JdbcConstants;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.ModelGenerator;
import com.bstek.dorado.jdbc.config.xml.DbElementParser;
import com.bstek.dorado.jdbc.config.xml.JdbcParseContext;
import com.bstek.dorado.jdbc.model.ColumnDefinition;

public class TableParser extends DbElementParser {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		TableDefinition tableDef = (TableDefinition)super.doParse(node, context);
		if (tableDef.isAutoCreateColumns()) {
			List<Operation> ops = tableDef.getInitOperations();
			Map<String,ColumnDefinition> columnMap = new HashMap<String,ColumnDefinition>(ops.size());
			for (Operation o: ops) {
				if (o instanceof ColumnDefinition) {
					ColumnDefinition column = (ColumnDefinition)o;
					columnMap.put(column.getName(), column);
				}
			}
			
			JdbcParseContext jdbcContext = (JdbcParseContext) context;
			JdbcEnviroment jdbcEnv = jdbcContext.getJdbcEnviroment();
			
			ModelGenerator generator = jdbcEnv.getModelGenerator();
			String catalog = tableDef.getCatalog();
			String schema = tableDef.getSchema();
			String tableName = tableDef.getTableName();
			Map<String,String> tableMeta = generator.singleTableMeta(jdbcEnv, catalog, schema, tableName);
			
			List<Map<String,String>> columnList = generator.listColumns(jdbcEnv, catalog, schema, tableName);
			for (Map<String,String> columnMeta: columnList) {
				String columnName = columnMeta.get(JdbcConstants.COLUMN_NAME);
				ColumnDefinition columnDef = columnMap.get(columnName);
				if (columnDef == null) {
					Map<String,String> columnProperties = generator.columnProperties(columnMeta, jdbcEnv);
					columnDef = generator.createColumnDefinition(tableMeta, columnProperties, jdbcEnv);
					if (columnDef != null) {
						tableDef.addInitOperation(columnDef);
					}
				}
			}
		}
		
		return tableDef;
	}

}
