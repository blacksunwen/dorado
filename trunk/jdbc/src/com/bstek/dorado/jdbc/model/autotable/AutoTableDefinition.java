package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.jdbc.model.DbElementCreationContext;
import com.bstek.dorado.jdbc.model.NamedObjectDefinition;

public class AutoTableDefinition extends NamedObjectDefinition {

	@Override
	protected void initObject(Object object, CreationInfo creationInfo,
			CreationContext context) throws Exception {
		AutoTable table = (AutoTable)object;
		DbElementCreationContext jdbcCreatetionContext = (DbElementCreationContext)context;
		jdbcCreatetionContext.setDbElement(table);
		
		super.initObject(object, creationInfo, context);
	}

	
}
