package com.bstek.dorado.web.resolver;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.util.Assert;
import com.bstek.dorado.util.xml.DomUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-23
 */
public class ResourceTypeParser implements XmlParser {

	public static class ResourceTypeParseContext extends ParseContext {
		public ResourceTypeManager resourceTypeManager;

		public ResourceTypeManager getResourceTypeManager() {
			return resourceTypeManager;
		}

		public void setResourceTypeManager(
				ResourceTypeManager resourceTypeManager) {
			this.resourceTypeManager = resourceTypeManager;
		}
	}

	public Object parse(Node node, ParseContext context) throws Exception {
		List<Element> typeElements = DomUtils.getChildrenByTagName(
				(Element) node, "resource-type");
		for (Element typeElement : typeElements) {
			parseTypeElement(typeElement, context);
		}
		return null;
	}

	private void parseTypeElement(Element typeElement, ParseContext context)
			throws Exception {
		String type = typeElement.getAttribute("type");
		Assert.notEmpty(type);
		String contentType = typeElement.getAttribute("content-type");
		boolean compressible = Boolean.parseBoolean(typeElement
				.getAttribute("compressible"));

		ResourceTypeManager resourceTypeManager = ((ResourceTypeParseContext) context)
				.getResourceTypeManager();
		resourceTypeManager.registerResourceType(new ResourceType(type,
				contentType, compressible));
	}
}
