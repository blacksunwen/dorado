package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.jdbc.model.AbstractColumn;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.util.Assert;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
@XmlNode(
	nodeName = "Rule",
	definitionType = "com.bstek.dorado.jdbc.model.autotable.MatchRuleDefinition"
)
public class BaseMatchRule extends AbstractMatchRule {

	private String tableAlias;
	private String columnName;
	private String operator;
	private Object value;

	public FromTable getFromTable() {
		Assert.notNull(this.getAutoTable());
		Assert.notEmpty(tableAlias);
		
		return this.getAutoTable().getFromTable(tableAlias);
	}

	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}
	
	public String getTableAlias() {
		return this.tableAlias;
	}

	public AbstractColumn getColumn() {
		FromTable fromTable = this.getFromTable();
		Assert.notEmpty(columnName);
		
		Table table = fromTable.getTable();
		return table.getColumn(columnName);
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public String getColumnName() {
		return this.columnName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
