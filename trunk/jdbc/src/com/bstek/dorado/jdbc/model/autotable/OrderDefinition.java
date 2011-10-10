package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.definition.Operation;

public class OrderDefinition extends ObjectDefinition implements Operation {

	@Override
	public void execute(Object object, CreationContext context)
			throws Exception {
		AutoTable table = (AutoTable)object;
		Order order = (Order)this.create(context);
		table.addOrder(order);
	}

}
