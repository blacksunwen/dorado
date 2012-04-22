package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.definition.Operation;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class JoinTableDefinition extends ObjectDefinition implements Operation {

	public void execute(Object object, CreationContext context)
			throws Exception {
		AutoTable table = (AutoTable)object;
		JoinTable joinTable = (JoinTable)this.create(context);
		table.addJoinTable(joinTable);
	}

}
