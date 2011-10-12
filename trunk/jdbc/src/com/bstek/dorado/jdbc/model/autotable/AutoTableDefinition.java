package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.jdbc.model.DbElementCreationContext;
import com.bstek.dorado.jdbc.model.DbElementDefinition;

public class AutoTableDefinition extends DbElementDefinition {

	@Override
	protected void initObject(Object object, CreationInfo creationInfo,
			CreationContext context) throws Exception {
		AutoTable table = (AutoTable)object;
		DbElementCreationContext jdbcCreatetionContext = (DbElementCreationContext)context;
		jdbcCreatetionContext.setDbElement(table);
		
		super.initObject(object, creationInfo, context);
	}

}
