package com.bstek.dorado.jdbc.model.table;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.Operation;
import com.bstek.dorado.jdbc.config.AbstractDbTableParser;
import com.bstek.dorado.jdbc.config.ColumnDefinition;
import com.bstek.dorado.jdbc.config.JdbcParseContext;
import com.bstek.dorado.jdbc.config.XmlConstants;
import com.bstek.dorado.jdbc.ide.IAgent;
import com.bstek.dorado.jdbc.model.Table;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class TableParser extends AbstractDbTableParser {
	private IAgent agent;
	
	public IAgent getAgent() {
		return agent;
	}

	public void setAgent(IAgent agent) {
		this.agent = agent;
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

	protected void createColumns(TableDefinition tableDef, JdbcParseContext jdbcContext) throws Exception {
		Map<String,ColumnDefinition> columnMap = new HashMap<String,ColumnDefinition>();
		for (Operation o: tableDef.getInitOperations()) {
			if (o instanceof ColumnDefinition) {
				ColumnDefinition column = (ColumnDefinition)o;
				columnMap.put(column.getName(), column);
			}
		}
		
		TableDefinition tableDef2 = this.createDefinition(tableDef, jdbcContext);
		for (Operation o: tableDef2.getInitOperations()) {
			if (o instanceof ColumnDefinition) {
				if (!columnMap.containsKey(((ColumnDefinition)o).getName())) {
					tableDef.addInitOperation(o);
				}
			}
		}
	}
	
	protected TableDefinition createDefinition(TableDefinition tableDef, JdbcParseContext jdbcContext) throws Exception {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IAgent.NAMESPACE, tableDef.getNamespace());
		parameters.put(IAgent.TABLE_NAME, tableDef.getTableName());
		parameters.put(IAgent.TABLE_TYPE, Table.TYPE);
		
		Document document = agent.createColumns(parameters, jdbcContext.getJdbcEnviroment().getDataSource());
		TableDefinition tableDef2 = (TableDefinition)doParse(document.getDocumentElement(), jdbcContext);
		
		return tableDef2;
	}

}
