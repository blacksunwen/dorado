package com.bstek.dorado.config.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;

/**
 * XML解析器的桥接器。用于将本节点的解析任务转交给某个符合条件的子解析器。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-29
 */
public class XmlParserBridge extends ConfigurableDispatchableXmlParser {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		return dispatchElement((Element) node, context);
	}

}
