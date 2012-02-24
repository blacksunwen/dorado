package com.bstek.dorado.jdbc.model.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.jdbc.model.AbstractTable;
import com.bstek.dorado.jdbc.model.AbstractColumn;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
@XmlNode(
	parser = "spring:dorado.jdbc.tableParser",
	definitionType = "com.bstek.dorado.jdbc.model.table.TableDefinition", 
	properties = {
		@XmlProperty(
			propertyName = "autoCreateColumns",
			propertyType = "boolean"
		)
	},
	subNodes = {
		@XmlSubNode(
			wrapper = @XmlNodeWrapper(nodeName = "Columns", fixed = true), 
			propertyName="Jdbc_TableColumns",
			propertyType = "List<com.bstek.dorado.jdbc.model.table.AbstractTableColumn>", 
			implTypes="com.bstek.dorado.jdbc.model.table.Table*"
		)
	}
)

public class Table extends AbstractTable {

	public static final String TYPE = "Table";
	
	private String namespace;
	private String tableName;
	private String dynamicClause;
	
	private boolean retrieveAfterInsert = false;
	private boolean retrieveAfterUpdate = false;
	
	private List<TableColumn> tableColumns = new ArrayList<TableColumn>();
	private List<TableKeyColumn> keyColumns = new ArrayList<TableKeyColumn>();
	
	public void addColumn(AbstractColumn column) {
		if (column instanceof TableKeyColumn) {
			keyColumns.add((TableKeyColumn)column);
		} else if (column instanceof TableColumn) {
			tableColumns.add((TableColumn)column);
		} else {
			throw new IllegalArgumentException("unknown column class [" + column.getClass().getName() + "]");
		}
		super.addColumn(column);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}

	@IdeProperty(highlight=1, editor="jdbc:service:list-space")
	@XmlProperty(attributeOnly=true)
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	@XmlProperty(attributeOnly=true)
	@IdeProperty(highlight=1, editor="jdbc:service:list-tables.xml")
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	@IdeProperty(highlight=1, editor = "multiLines")
	public String getDynamicClause() {
		return dynamicClause;
	}

	public boolean isRetrieveAfterInsert() {
		return retrieveAfterInsert;
	}

	public void setRetrieveAfterInsert(boolean retrieveAfterInsert) {
		this.retrieveAfterInsert = retrieveAfterInsert;
	}

	public boolean isRetrieveAfterUpdate() {
		return retrieveAfterUpdate;
	}

	public void setRetrieveAfterUpdate(boolean retrieveAfterUpdate) {
		this.retrieveAfterUpdate = retrieveAfterUpdate;
	}

	public void setDynamicClause(String dynamicToken) {
		this.dynamicClause = dynamicToken;
	}

	public List<TableColumn> getTableColumns() {
		return Collections.unmodifiableList(tableColumns);
	}
	
	public List<TableKeyColumn> getKeyColumns() {
		return Collections.unmodifiableList(keyColumns);
	}

	@Override
	protected String getDefaultSQLGeneratorServiceName() {
		return "spring:dorado.jdbc.tableSqlGenerator";
	}
}
