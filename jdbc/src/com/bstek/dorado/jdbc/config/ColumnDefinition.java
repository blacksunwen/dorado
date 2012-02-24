package com.bstek.dorado.jdbc.config;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.definition.Operation;
import com.bstek.dorado.jdbc.model.AbstractColumn;
import com.bstek.dorado.jdbc.model.AbstractTable;

/**
 * “列”的定义对象
 * 
 * @author mark.li@bstek.com
 *
 */
public class ColumnDefinition extends ObjectDefinition implements Operation {

	public String getName() {
		return (String)this.getProperties().get("name");
	}
	
	@Override
	public void execute(Object object, CreationContext context)
			throws Exception {
		AbstractColumn column = (AbstractColumn)this.create(context);
		
		AbstractTable dbElement = (AbstractTable)object;
		dbElement.addColumn(column);
	}

}
