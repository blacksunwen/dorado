/**
 * 
 */
package com.bstek.dorado.view.config.xml;

import java.util.List;

import org.w3c.dom.Element;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.XmlConstants;
import com.bstek.dorado.config.xml.XmlParseException;
import com.bstek.dorado.util.xml.DomUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-11
 */
public final class ViewConfigParserUtils {
	private ViewConfigParserUtils() {
	}

	public static Element findArgumentsElement(Element documentElement,
			ParseContext context) throws Exception {
		List<Element> elements = DomUtils.getChildrenByTagName(documentElement,
				ViewXmlConstants.ARGUMENTS);
		int size = elements.size();
		if (size > 1) {
			throw new XmlParseException("More than one <"
					+ ViewXmlConstants.ARGUMENTS + "> found.", documentElement,
					context.getResource());
		} else if (size == 1) {
			return elements.get(0);
		} else {
			return null;
		}
	}

	public static Element findContextElement(Element documentElement,
			ParseContext context) throws Exception {
		List<Element> elements = DomUtils.getChildrenByTagName(documentElement,
				ViewXmlConstants.CONTEXT);
		int size = elements.size();
		if (size > 1) {
			throw new XmlParseException("More than one <"
					+ ViewXmlConstants.CONTEXT + "> found.", documentElement,
					context.getResource());
		} else if (size == 1) {
			return elements.get(0);
		} else {
			return null;
		}
	}

	public static Element findModelElement(Element documentElement,
			ParseContext context) throws Exception {
		List<Element> elements = DomUtils.getChildrenByTagName(documentElement,
				ViewXmlConstants.MODEL);
		int size = elements.size();
		if (size > 1) {
			throw new XmlParseException("More than one <"
					+ ViewXmlConstants.MODEL + "> found.", documentElement,
					context.getResource());
		} else if (size == 1) {
			return elements.get(0);
		} else {
			return null;
		}
	}

	public static Element findViewElement(Element element, ParseContext context) {
		Element viewElement = null;
		for (Element childElement : DomUtils.getChildElements(element)) {
			String nodeName = childElement.getNodeName();
			if (!nodeName.equals(XmlConstants.PROPERTY)
					&& !nodeName.equals(ViewXmlConstants.ARGUMENTS)
					&& !nodeName.equals(ViewXmlConstants.MODEL)
					&& !nodeName.equals(ViewXmlConstants.CONTEXT)) {
				if (viewElement != null) {
					throw new XmlParseException(
							"More than one view element found.", element,
							context.getResource());
				}
				viewElement = childElement;
			}
		}
		if (viewElement == null) {
			throw new XmlParseException("No view element found.", element,
					context.getResource());
		}
		return viewElement;
	}
}
