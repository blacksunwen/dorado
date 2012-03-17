package com.bstek.dorado.jdbc.model.autotable;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.sql.SqlConstants.JunctionOperator;
import com.bstek.dorado.util.Assert;

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

	@Override
	public String token(AutoTable autoTable, JdbcParameterSource parameterSource) {
		if (!this.isAvailable()) {
			return null;
		}
		
		List<AbstractMatchRule> matchRules = this.getMatchRules();
		if (matchRules.size() > 0) {
			JunctionOperator operator = this.getOperator();
			Assert.notNull(operator , "Operator must not be null.");
			
			List<String> tokens = new ArrayList<String>(matchRules.size());
			for (AbstractMatchRule rule: matchRules) {
				String token = rule.token(autoTable, parameterSource);
				if (StringUtils.isNotEmpty(token)) {
					if (rule instanceof JunctionMatchRule) {
						tokens.add("(" + token + ")");
					} else {
						tokens.add(token);
					}
				}
			}
			
			if (tokens.size() == 1) {
				return tokens.get(0);
			} if (tokens.size() > 1) {
				return StringUtils.join(tokens," " + operator.toString() + " ");
			}
		}
		
		return null;
	}
}
