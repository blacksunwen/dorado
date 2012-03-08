package com.bstek.dorado.jdbc.config;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.ObjectParser;

/**
 * 抽象的{@link com.bstek.dorado.jdbc.model.DbTable}的解析器
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractDbTableParser extends ObjectParser {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		AbstractDbTableDefinition def = (AbstractDbTableDefinition) super.doParse(node, context);
		
		boolean autoCreateDataType = def.getVirtualPropertyBoolean(XmlConstants.AUTO_CREATE_DATATYPE, false);
		def.setAutoCreateDataType(autoCreateDataType);
		def.getProperties().remove(XmlConstants.AUTO_CREATE_DATATYPE);
		
		boolean autoCreateDataProvider = def.getVirtualPropertyBoolean(XmlConstants.AUTO_CREATE_DATAPROVIDER, false);
		def.setAutoCreateDataProvider(autoCreateDataProvider);
		def.getProperties().remove(XmlConstants.AUTO_CREATE_DATAPROVIDER);
		
		return def;
	}

}
