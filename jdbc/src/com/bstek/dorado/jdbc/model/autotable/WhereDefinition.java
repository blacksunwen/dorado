package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.definition.Operation;

public class WhereDefinition extends ObjectDefinition implements Operation {

	@Override
	public void execute(Object object, CreationContext context)
			throws Exception {
		Where where = (Where)this.create(context);
		AutoTable table = (AutoTable)object;
		table.setWhere(where);
		where.setAutoTable(table);
	}

}
