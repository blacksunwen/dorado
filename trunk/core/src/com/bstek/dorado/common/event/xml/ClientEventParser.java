package com.bstek.dorado.common.event.xml;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ClassUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.common.event.ClientEventRegisterInfo;
import com.bstek.dorado.common.event.ClientEventRegistry;
import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.util.Assert;
import com.bstek.dorado.util.xml.DomUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-30
 */
public class ClientEventParser implements XmlParser {

	public Object parse(Node node, ParseContext context) throws Exception {
		List<Element> typeElements = DomUtils.getChildrenByTagName(
				(Element) node, "type");
		for (Element typeElement : typeElements) {
			parseTypeElement(typeElement, context);
		}
		return null;
	}

	private void parseTypeElement(Element typeElement, ParseContext context)
			throws Exception {
		String className = typeElement.getAttribute("class");
		Assert.notEmpty(className);
		Class<?> cl = ClassUtils.forName(className, ClassUtils
				.getDefaultClassLoader());

		List<Element> eventElements = DomUtils.getChildrenByTagName(
				typeElement, "event");
		for (Element eventElement : eventElements) {
			String eventName = eventElement.getAttribute("name");
			Assert.notEmpty(eventName);
			String signatureText = eventElement.getAttribute("signature");
			String[] signature = (StringUtils.isEmpty(signatureText)) ? null
					: StringUtils.split(signatureText, ',');
			ClientEventRegistry
					.registerClientEvent(new ClientEventRegisterInfo(cl,
							eventName, signature));
		}
	}

}
