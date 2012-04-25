package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.definition.Operation;
import com.bstek.dorado.jdbc.model.AutoTable;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class FromTableDefinition extends ObjectDefinition implements Operation {

	public void execute(Object object, CreationContext context)
			throws Exception {
		AutoTable table = (AutoTable)object;
		FromTable fromTable = (FromTable)this.create(context);
		table.addFromTable(fromTable);
	}

}
