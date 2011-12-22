package com.bstek.dorado.view.config.xml;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.data.config.xml.DataObjectParserDispatcher;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-11-24
 */
public class ModelParser implements XmlParser {
	private XmlParser dataObjectPreloadParser;
	private DataObjectParserDispatcher dataObjectParserDispatcher;

	public void setDataObjectPreloadParser(XmlParser dataObjectsPreloadParser) {
		this.dataObjectPreloadParser = dataObjectsPreloadParser;
	}

	public void setDataObjectParserDispatcher(
			DataObjectParserDispatcher dataObjectParserDispatcher) {
		this.dataObjectParserDispatcher = dataObjectParserDispatcher;
	}

	public Object parse(Node node, ParseContext context) throws Exception {
		dataObjectPreloadParser.parse(node, context);
		dataObjectParserDispatcher.parse(null, context);
		return null;
	}

}
