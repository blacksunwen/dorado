package com.bstek.dorado.hibernate.criteria;

public class SubCriteria extends BaseCriteria {

	private boolean available = true;
	private String associationPath;
	private JoinType joinType;

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getAssociationPath() {
		return associationPath;
	}

	public void setAssociationPath(String associationPath) {
		this.associationPath = associationPath;
	}

	public JoinType getJoinType() {
		return joinType;
	}

	public void setJoinType(JoinType joinType) {
		this.joinType = joinType;
	}
}
