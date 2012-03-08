package com.bstek.dorado.jdbc.model.table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.Operation;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.config.AbstractDbTableParser;
import com.bstek.dorado.jdbc.config.ColumnDefinition;
import com.bstek.dorado.jdbc.config.JdbcParseContext;
import com.bstek.dorado.jdbc.config.XmlConstants;
import com.bstek.dorado.jdbc.meta.TableMetaDataGenerator;
import com.bstek.dorado.jdbc.support.JdbcConstants;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class TableParser extends AbstractDbTableParser {
	
	ModelGeneratorSuit generatorSuit;
	
	public ModelGeneratorSuit getGeneratorSuit() {
		return generatorSuit;
	}

	public void setGeneratorSuit(ModelGeneratorSuit generatorSuit) {
		this.generatorSuit = generatorSuit;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		TableDefinition tableDef = (TableDefinition)super.doParse(node, context);
		JdbcParseContext jdbcContext = (JdbcParseContext) context;
		
		boolean autoCreateColumns = tableDef.getVirtualPropertyBoolean(XmlConstants.AUTO_CREATE_COLUMNS, false);
		tableDef.getProperties().remove(XmlConstants.AUTO_CREATE_COLUMNS);
		tableDef.setAutoCreateColumns(autoCreateColumns);
		
		this.doAutoCreate(tableDef, jdbcContext);
		
		return tableDef;
	}
	
	protected void doAutoCreate(TableDefinition tableDef, JdbcParseContext jdbcContext) throws Exception {
		if (tableDef.isAutoCreateColumns()) {
			this.createColumns(tableDef, jdbcContext);
		}
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
		
		String tableName = tableDef.getTableName();
		String namespace = tableDef.getNamespace();
		
		TableMetaDataGenerator tg = generatorSuit.getTableMetaDataGenerator();
		Map<String,String> tableMeta = tg.tableMeta(jdbcEnv, namespace, tableName);
		
		List<Map<String,String>> columnList = tg.listColumnMetas(jdbcEnv, namespace, tableName);
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
