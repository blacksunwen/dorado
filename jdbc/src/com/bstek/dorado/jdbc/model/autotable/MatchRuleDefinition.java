package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.definition.Operation;
import com.bstek.dorado.jdbc.config.JdbcCreationContext;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class MatchRuleDefinition extends ObjectDefinition implements Operation {

	@Override
	public void execute(Object object, CreationContext context)
			throws Exception {
		MatchRule rule = (MatchRule)this.create(context);
		JdbcCreationContext jdbcCreatetionContext = (JdbcCreationContext)context;
		AutoTable table = (AutoTable)jdbcCreatetionContext.getDbElement();
		rule.setAutoTable(table);
		
		if (object instanceof JunctionMatchRule) {
			JunctionMatchRule amr = (JunctionMatchRule)object;
			amr.addMatchRule(rule);
		} else {
			throw new IllegalStateException("Error Oject class [" + object.getClass() + "]");
		}
	}

}
