package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.jdbc.config.AbstractDbTableDefinition;
import com.bstek.dorado.jdbc.config.JdbcCreationContext;

public class AutoTableDefinition extends AbstractDbTableDefinition {

	@Override
	protected void initObject(Object object, CreationInfo creationInfo,
			CreationContext context) throws Exception {
		AutoTable table = (AutoTable)object;
		JdbcCreationContext jdbcCreatetionContext = (JdbcCreationContext)context;
		jdbcCreatetionContext.setDbElement(table);
		
		super.initObject(object, creationInfo, context);
	}

}
