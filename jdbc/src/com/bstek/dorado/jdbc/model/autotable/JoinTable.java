package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.jdbc.sql.SqlConstants.JoinModel;

@XmlNode(
	definitionType = "com.bstek.dorado.jdbc.model.autotable.JoinTableDefinition"
)
public class JoinTable {

	private JoinModel joinModel = JoinModel.INNER_JOIN;
	
	private String leftFromTableAlias;
	private String[] leftColumnNames = new String[0];
	private String rightFromTableAlias;
	private String[] rightColumnNames = new String[0];
	
	public void setJoinModel(JoinModel joinModel) {
		this.joinModel = joinModel;
	}

	public JoinModel getJoinModel() {
		return this.joinModel;
	}

	public String getLeftFromTableAlias() {
		return leftFromTableAlias;
	}

	public void setLeftFromTableAlias(String leftFromTableAlias) {
		this.leftFromTableAlias = leftFromTableAlias;
	}

	public String[] getLeftColumnNames() {
		return leftColumnNames;
	}

	public void setLeftColumnNames(String[] leftColumnNames) {
		this.leftColumnNames = leftColumnNames;
	}

	public String getRightFromTableAlias() {
		return rightFromTableAlias;
	}

	public void setRightFromTableAlias(String rightFromTableAlias) {
		this.rightFromTableAlias = rightFromTableAlias;
	}

	public String[] getRightColumnNames() {
		return rightColumnNames;
	}

	public void setRightColumnNames(String[] rightColumnNames) {
		this.rightColumnNames = rightColumnNames;
	}
	
}
