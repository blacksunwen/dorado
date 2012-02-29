package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.jdbc.sql.SqlConstants.JoinOperator;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
@XmlNode(
	definitionType = "com.bstek.dorado.jdbc.model.autotable.JoinTableDefinition"
)
public class JoinTable {

	private JoinOperator operator = JoinOperator.INNER_JOIN;
	
	private String leftFromTableName;
	private String[] leftColumnNames = new String[0];
	private String rightFromTableName;
	private String[] rightColumnNames = new String[0];
	
	public String getLeftFromTable() {
		return leftFromTableName;
	}

	public void setLeftFromTable(String leftFromTableName) {
		this.leftFromTableName = leftFromTableName;
	}

	public String getRightFromTable() {
		return rightFromTableName;
	}

	public void setRightFromTable(String rightFromTableName) {
		this.rightFromTableName = rightFromTableName;
	}

	public void setOperator(JoinOperator operator) {
		this.operator = operator;
	}

	@ClientProperty(escapeValue = "INNER_JOIN")
	public JoinOperator getOperator() {
		return this.operator;
	}

	public String[] getLeftColumns() {
		return leftColumnNames;
	}

	public void setLeftColumns(String[] leftColumnNames) {
		this.leftColumnNames = leftColumnNames;
	}

	public String[] getRightColumns() {
		return rightColumnNames;
	}

	public void setRightColumns(String[] rightColumnNames) {
		this.rightColumnNames = rightColumnNames;
	}
	
}
