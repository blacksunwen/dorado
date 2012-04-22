package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.definition.Operation;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class MatchRuleDefinition extends ObjectDefinition implements Operation {

	public void execute(Object object, CreationContext context)
			throws Exception {
		AbstractMatchRule rule = (AbstractMatchRule)this.create(context);
		if (object instanceof JunctionMatchRule) {
			JunctionMatchRule jmr = (JunctionMatchRule)object;
			jmr.addMatchRule(rule);
		} else if (object instanceof AutoTable) {
			((AutoTable)object).setWhere((JunctionMatchRule)rule);
		} else {
			throw new IllegalStateException("Error Oject class [" + object.getClass() + "]");
		}
	}

}
