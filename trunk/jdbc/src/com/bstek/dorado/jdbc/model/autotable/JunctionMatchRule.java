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
	definitionType = "com.bstek.dorado.jdbc.model.autotable.MatchRuleDefinition"
)
public class JunctionMatchRule extends AbstractMatchRule {

	private JunctionOperator operator = JunctionOperator.AND;
	
	private List<AbstractMatchRule> matchRules = new ArrayList<AbstractMatchRule>();

	@ClientProperty(escapeValue = "AND")
	public JunctionOperator getOperator() {
		return operator;
	}

	public void setOperator(JunctionOperator o) {
		this.operator = o;
	}

	@XmlSubNode
	public List<AbstractMatchRule> getMatchRules() {
		return matchRules;
	}
	
	public void setMatchRules(List<AbstractMatchRule> rules) {
		this.matchRules = rules;
	}

	public void addMatchRule(AbstractMatchRule matchRule) {
		this.matchRules.add(matchRule);
	}

}
