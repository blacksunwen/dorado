package com.bstek.dorado.jdbc.model.autotable;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.jdbc.sql.SqlConstants.JunctionOperator;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
@XmlNode(
	nodeName = "Junction",
	definitionType = "com.bstek.dorado.jdbc.model.autotable.MatchRuleDefinition",
	subNodes = {
		@XmlSubNode(
			propertyName = "matchRules", 
			propertyType = "List<com.bstek.dorado.jdbc.model.autotable.AbstractMatchRule>",
			implTypes = {
				"com.bstek.dorado.jdbc.model.autotable.JunctionMatchRule", 
				"com.bstek.dorado.jdbc.model.autotable.BaseMatchRule"
			}
		)
	}
)
public class JunctionMatchRule extends AbstractMatchRule {

	private JunctionOperator operator = JunctionOperator.AND;
	
	private List<MatchRule> matchRules = new ArrayList<MatchRule>();

	@ClientProperty(escapeValue = "AND")
	public JunctionOperator getOperator() {
		return operator;
	}

	public void setOperator(JunctionOperator o) {
		this.operator = o;
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
