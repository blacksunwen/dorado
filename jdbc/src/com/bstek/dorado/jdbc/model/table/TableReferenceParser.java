package com.bstek.dorado.jdbc.model.table;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.DefaultDefinitionReference;
import com.bstek.dorado.config.definition.DefinitionManager;
import com.bstek.dorado.config.xml.PropertyParser;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.config.DbElementDefinition;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class TableReferenceParser extends PropertyParser{

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		String elementName = (String)super.doParse(node, context);
		DefinitionManager<DbElementDefinition> manager = JdbcUtils.getDbmManager();
		
		DefaultDefinitionReference<DbElementDefinition> defRef = 
				new DefaultDefinitionReference<DbElementDefinition>(manager, elementName);
		return defRef;
	}
}
