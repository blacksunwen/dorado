package com.bstek.dorado.idesupport.parse;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.ConfigurableDispatchableXmlParser;
import com.bstek.dorado.idesupport.model.ClientEvent;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-19
 */
public class ClientEventParser extends ConfigurableDispatchableXmlParser {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		Element element = (Element) node;
		ClientEvent event = new ClientEvent();
		Map<String, Object> properties = this.parseProperties(element, context);
		BeanUtils.copyProperties(event, properties);
		return event;
	}

}
