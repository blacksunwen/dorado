package com.bstek.dorado.web.resolver;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.w3c.dom.Document;

import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceUtils;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-23
 */
public class ResourceTypeLoader implements ApplicationContextAware {
	private static Log logger = LogFactory.getLog(ResourceTypeLoader.class);

	private ResourceTypeManager resourceTypeManager;
	private String configLocation;
	private List<String> configLocations;
	private XmlDocumentBuilder xmlDocumentBuilder;
	private XmlParser resourceTypeParser;

	public void setResourceTypeManager(ResourceTypeManager resourceTypeManager) {
		this.resourceTypeManager = resourceTypeManager;
	}

	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

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

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		try {
			if (StringUtils.isNotEmpty(configLocation)) {
				loadConfigs(resourceTypeManager,
						ResourceUtils.getResources(configLocation));
			}

			if (configLocations != null) {
				String[] locations = new String[configLocations.size()];
				configLocations.toArray(locations);
				loadConfigs(resourceTypeManager,
						ResourceUtils.getResources(locations));
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
}
