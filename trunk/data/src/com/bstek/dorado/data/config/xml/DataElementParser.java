package com.bstek.dorado.data.config.xml;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.xml.XmlConstants;
import com.bstek.dorado.config.xml.XmlParseException;
import com.bstek.dorado.core.el.Expression;
import com.bstek.dorado.util.xml.DomUtils;

/**
 * 数据节点的解析器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apirl 18, 2007
 */
public class DataElementParser extends DataElementParserSupport {

	@Override
	protected Object internalParse(Node node, DataParseContext context)
			throws Exception {
		Object data = null;
		if (node instanceof Element) {
			boolean duringParsingDataElement = context
					.isDuringParsingDataElement();
			if (!duringParsingDataElement) {
				context.setDuringParsingDataElement(true);
			}

			Element element = (Element) node;
			if (element.getChildNodes().getLength() == 0) {
				String valueText = element
						.getAttribute(XmlConstants.ATTRIBUTE_VALUE);
				if (StringUtils.isNotEmpty(valueText)) {
					data = parseValueFromText(valueText, context);
				}
			} else {
				List<Element> elements = DomUtils.getChildElements(element);
				int size = elements.size();
				if (size == 0) {
					data = dispatchElement(XmlConstants.VALUE, element, context);
				} else if (size == 1) {
					data = dispatchElement(elements.get(0), context);
				} else if (size > 1) {
					throw new XmlParseException(
							"Can not contains more than one data elements.",
							element, context);
				}
			}

			if (!duringParsingDataElement) {
				context.setDuringParsingDataElement(false);
			}
		} else {
			data = node.getNodeValue();
			Expression expression = getExpressionHandler().compile(
					(String) data);
			if (expression != null) {
				data = expression;
			}
		}
		return data;
	}

}
