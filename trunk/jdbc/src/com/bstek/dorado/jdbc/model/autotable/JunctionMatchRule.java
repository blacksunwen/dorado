package com.bstek.dorado.jdbc.model.autotable;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.jdbc.sql.SqlConstants.JunctionModel;

public class JunctionMatchRule extends AbstractMatchRule {

	private JunctionModel model = JunctionModel.AND;
	
	private List<MatchRule> matchRules = new ArrayList<MatchRule>();

	public JunctionModel getModel() {
		return model;
	}

	public void setModel(JunctionModel model) {
		this.model = model;
	}
	
	public List<MatchRule> getMatchRules() {
		return matchRules;
	}

	public void addMatchRule(MatchRule matchRule) {
		this.matchRules.add(matchRule);
		if (this.getAutoTable() != null) {
			matchRule.setAutoTable(this.getAutoTable());
		}
	}

}
