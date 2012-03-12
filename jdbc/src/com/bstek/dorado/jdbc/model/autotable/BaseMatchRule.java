package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;

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

	public void setFromTable(String tableAlias) {
		this.fromTableName = tableAlias;
	}
	
	public String getFromTable() {
		return this.fromTableName;
	}

	public void setColumn(String columnName) {
		this.columnName = columnName;
	}
	
	public String getColumn() {
		return this.columnName;
	}

	@IdeProperty(enumValues="=,<>,>,<,>=,<=,in,like,like%,%like,%like%,is null")
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
