package com.bstek.dorado.jdbc.model.table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.Operation;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcSpace;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.config.AbstractDbTableDefinition;
import com.bstek.dorado.jdbc.config.AbstractDbTableParser;
import com.bstek.dorado.jdbc.config.ColumnDefinition;
import com.bstek.dorado.jdbc.config.JdbcParseContext;
import com.bstek.dorado.jdbc.meta.TableMetaDataGenerator;
import com.bstek.dorado.jdbc.support.JdbcConstants;

public class TableParser extends AbstractDbTableParser {
	
	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		TableDefinition tableDef = (TableDefinition)super.doParse(node, context);
		
		JdbcParseContext jdbcContext = (JdbcParseContext) context;
		if (tableDef.isAutoCreateColumns()) {
			this.createColumns(tableDef, jdbcContext);
		}
		if (tableDef.isAutoCreateDataType()) {
			this.createDataType(tableDef);
		}
		if (tableDef.isAutoCreateDataProvider()) {
			this.createDataProvider(tableDef);
		}
		return tableDef;
	}
	
	@Override
	protected void doAutoCreate(AbstractDbTableDefinition tableDef,
			JdbcParseContext jdbcContext) throws Exception {
		TableDefinition def = (TableDefinition)tableDef;
		if (def.isAutoCreateColumns()) {
			this.createColumns(def, jdbcContext);
		}
		super.doAutoCreate(tableDef, jdbcContext);
	}

	protected void createColumns(TableDefinition tableDef, JdbcParseContext jdbcContext) {
		List<Operation> ops = tableDef.getInitOperations();
		Map<String,ColumnDefinition> columnMap = new HashMap<String,ColumnDefinition>(ops.size());
		for (Operation o: ops) {
			if (o instanceof ColumnDefinition) {
				ColumnDefinition column = (ColumnDefinition)o;
				columnMap.put(column.getName(), column);
			}
		}
		
		JdbcEnviroment jdbcEnv = jdbcContext.getJdbcEnviroment();
		Dialect dialect = jdbcEnv.getDialect();
		
		ModelGeneratorSuit generator = JdbcUtils.getModelGeneratorSuit();
		
		String tableName = tableDef.getTableName();
		String namespace = tableDef.getNamespace();
		String catalog = null;
		String schema = null;
		if (dialect.getTableJdbcSpace() == JdbcSpace.CATALOG) {
			catalog = namespace;
		} else if (dialect.getTableJdbcSpace() == JdbcSpace.SCHEMA) {
			schema = namespace;
		}
		
		TableMetaDataGenerator tg = generator.getTableMetaDataGenerator();
		Map<String,String> tableMeta = tg.tableMeta(jdbcEnv, catalog, schema, tableName);
		
		List<Map<String,String>> columnList = tg.listColumnMetas(jdbcEnv, catalog, schema, tableName);
		for (Map<String,String> columnMeta: columnList) {
			String columnName = columnMeta.get(JdbcConstants.COLUMN_NAME);
			ColumnDefinition columnDef = columnMap.get(columnName);
			if (columnDef == null) {
				Map<String,String> columnProperties = tg.columnProperties(columnMeta, jdbcEnv);
				columnDef = tg.createColumnDefinition(tableMeta, columnProperties, jdbcEnv);
				if (columnDef != null) {
					tableDef.addInitOperation(columnDef);
				}
			}
		}
	}

}
