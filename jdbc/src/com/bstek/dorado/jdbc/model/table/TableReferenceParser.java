package com.bstek.dorado.jdbc.model.table;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.DefaultDefinitionReference;
import com.bstek.dorado.config.xml.PropertyParser;
import com.bstek.dorado.jdbc.config.DbElementDefinition;
import com.bstek.dorado.jdbc.config.DbmManager;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class TableReferenceParser extends PropertyParser{

	private DbmManager dbmManager;
	
	public DbmManager getDbmManager() {
		return dbmManager;
	}

	public void setDbmManager(DbmManager dbmManager) {
		this.dbmManager = dbmManager;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		String elementName = (String)super.doParse(node, context);
		if (StringUtils.isNotEmpty(elementName)) {
			DefaultDefinitionReference<DbElementDefinition> defRef = 
					new DefaultDefinitionReference<DbElementDefinition>(dbmManager, elementName);
			return defRef;
		} else {
			return null;
		}
	}
}
