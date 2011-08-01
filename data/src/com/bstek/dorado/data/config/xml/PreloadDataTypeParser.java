package com.bstek.dorado.data.config.xml;

import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.ConfigurableDispatchableXmlParser;
import com.bstek.dorado.config.xml.XmlConstants;
import com.bstek.dorado.config.xml.XmlParseException;

/**
 * DataType的预解析器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 21 Feb 2007
 */
public class PreloadDataTypeParser extends ConfigurableDispatchableXmlParser {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		DataParseContext dataContext = (DataParseContext) context;
		Element element = ((Element) node);

		String name = element.getAttribute(XmlConstants.ATTRIBUTE_NAME);
		if (StringUtils.isEmpty(name)) {
			throw new XmlParseException("[" + XmlConstants.ATTRIBUTE_NAME
					+ "] attribute can not be empty", element, context);
		}

		Map<String, NodeWrapper> configuredDataTypes = dataContext
				.getConfiguredDataTypes();
		if (configuredDataTypes.containsKey(name)) {
			boolean overwrite = BooleanUtils.toBoolean(element
					.getAttribute(DataXmlConstants.ATTRIBUTE_OVERWRITE));
			if (!overwrite) {
				throw new XmlParseException(DataXmlConstants.DATA_TYPE + " ["
						+ name + "] is not unique!", element, context);
			}
		}

		configuredDataTypes.put(name,
				new NodeWrapper(node, context.getResource()));
		return null;
	}
}
