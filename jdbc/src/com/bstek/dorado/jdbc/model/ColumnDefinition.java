package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.definition.Operation;

public class ColumnDefinition extends ObjectDefinition implements Operation {

	@Override
	public void execute(Object object, CreationContext context)
			throws Exception {
		Column column = (Column)this.create(context);
		
		AbstractDbElement dbElement = (AbstractDbElement)object;
		dbElement.addColumn(column);
	}

}
