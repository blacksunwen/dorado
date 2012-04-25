package com.bstek.dorado.jdbc.model.table;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.DefaultDefinitionReference;
import com.bstek.dorado.config.xml.PropertyParser;
import com.bstek.dorado.jdbc.config.DbElementDefinition;
import com.bstek.dorado.jdbc.config.DbmDefinitionManager;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class TableReferenceParser extends PropertyParser{

	private DbmDefinitionManager dbmDefinitionManager;
	
	public DbmDefinitionManager getDbmDefinitionManager() {
		return dbmDefinitionManager;
	}

	public void setDbmDefinitionManager(DbmDefinitionManager dbmDefinitionManager) {
		this.dbmDefinitionManager = dbmDefinitionManager;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		String elementName = (String)super.doParse(node, context);
		if (StringUtils.isNotEmpty(elementName)) {
			DefaultDefinitionReference<DbElementDefinition> defRef = 
					new DefaultDefinitionReference<DbElementDefinition>(getDbmDefinitionManager(), elementName);
			return defRef;
		} else {
			return null;
		}
	}
}
