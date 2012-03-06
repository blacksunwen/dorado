package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
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

	private String fromTableName;
	private String columnName;
	private String operator;
	private Object value;

	public FromTable getFromTableObject() {
		Assert.notNull(this.getAutoTable());
		Assert.notEmpty(fromTableName);
		
		return this.getAutoTable().getFromTable(fromTableName);
	}

	public void setFromTable(String tableAlias) {
		this.fromTableName = tableAlias;
	}
	
	public String getFromTable() {
		return this.fromTableName;
	}

	public AbstractDbColumn getColumnObject() {
		FromTable fromTable = this.getFromTableObject();
		Assert.notEmpty(columnName);
		
		Table table = fromTable.getTableObject();
		return table.getColumn(columnName);
	}

	public void setColumn(String columnName) {
		this.columnName = columnName;
	}
	
	public String getColumn() {
		return this.columnName;
	}

	@IdeProperty(enumValues="=,<>,>,<,>=,<=,in,like,like%,%like,%like%,is null,is not null")
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
