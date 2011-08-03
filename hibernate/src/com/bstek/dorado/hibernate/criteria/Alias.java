package com.bstek.dorado.hibernate.criteria;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.hibernate.criteria.criterion.BaseCriterion;

@XmlNode(nodeName="Alias")
public class Alias {

	private boolean available = true;
	private String associationPath;
	private String alias;
	private JoinType joinType;
	
	private List<BaseCriterion> criterions = new ArrayList<BaseCriterion>();
	
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
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public JoinType getJoinType() {
		return joinType;
	}
	public void setJoinType(JoinType joinType) {
		this.joinType = joinType;
	}
	public void setJoinType(String joinType) {
		if (StringUtils.isNotEmpty(joinType)) {
			JoinType j = JoinType.valueOf(joinType);
			if (j != null) {
				this.joinType = j;
			} else {
				throw new IllegalArgumentException("unknown joinType '" + joinType + "'.");
			}
		} else {
			this.joinType = null;	
		}
	}
	
	// ********** Criterion **********
	@XmlSubNode(parser = "dorado.criterionsParser")
	public List<BaseCriterion> getCriterions() {
		return criterions;
	}

	public void addCriterion(BaseCriterion criterion) {
		criterions.add(criterion);
	}
}
