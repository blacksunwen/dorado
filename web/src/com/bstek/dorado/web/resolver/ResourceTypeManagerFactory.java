package com.bstek.dorado.web.resolver;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.w3c.dom.Document;

import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceUtils;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-23
 */
public class ResourceTypeManagerFactory implements
		FactoryBean<ResourceTypeManager> {
	private static Log logger = LogFactory
			.getLog(ResourceTypeManagerFactory.class);

	private List<String> configLocations;
	private XmlDocumentBuilder xmlDocumentBuilder;
	private XmlParser resourceTypeParser;

	public void setConfigLocations(List<String> configLocations) {
		this.configLocations = configLocations;
	}

	public void setXmlDocumentBuilder(XmlDocumentBuilder xmlDocumentBuilder) {
		this.xmlDocumentBuilder = xmlDocumentBuilder;
	}

	public void setResourceTypeParser(XmlParser resourceTypeParser) {
		this.resourceTypeParser = resourceTypeParser;
	}

	protected synchronized void loadConfigs(
			ResourceTypeManager resourceTypeManager, Resource[] resources)
			throws Exception {
		ResourceTypeParser.ResourceTypeParseContext context = new ResourceTypeParser.ResourceTypeParseContext();
		context.setResourceTypeManager(resourceTypeManager);
		for (Resource resource : resources) {
			if (resource.exists()) {
				Document document = xmlDocumentBuilder.loadDocument(resource);
				context.setResource(resource);
				resourceTypeParser
						.parse(document.getDocumentElement(), context);
			}
		}
	}

	public ResourceTypeManager getObject() throws Exception {
		ResourceTypeManager resourceTypeManager = new ResourceTypeManager();
		try {
			if (configLocations != null) {
				String[] locations = new String[configLocations.size()];
				configLocations.toArray(locations);
				loadConfigs(resourceTypeManager,
						ResourceUtils.getResources(locations));
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return resourceTypeManager;
	}

	public Class<ResourceTypeManager> getObjectType() {
		return ResourceTypeManager.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
