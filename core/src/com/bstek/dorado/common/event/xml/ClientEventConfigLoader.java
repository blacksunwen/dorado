package com.bstek.dorado.common.event.xml;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.core.EngineStartupListener;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceUtils;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-30
 */
public class ClientEventConfigLoader extends EngineStartupListener {
	private static Log logger = LogFactory
			.getLog(ClientEventConfigLoader.class);

	private List<String> configLocations;
	private XmlDocumentBuilder xmlDocumentBuilder;
	private XmlParser clientEventParser;

	public void setConfigLocations(List<String> configLocations) {
		this.configLocations = configLocations;
	}

	public void setXmlDocumentBuilder(XmlDocumentBuilder xmlDocumentBuilder) {
		this.xmlDocumentBuilder = xmlDocumentBuilder;
	}

	public void setClientEventParser(XmlParser clientEventParser) {
		this.clientEventParser = clientEventParser;
	}

	protected synchronized void loadConfigs(Resource[] resources)
			throws Exception {
		ParseContext context = new ParseContext();
		for (Resource resource : resources) {
			if (resource.exists()) {
				logger.info("Loading client event settings from [" + resource
						+ "]...");
				Document document = xmlDocumentBuilder.loadDocument(resource);
				context.setResource(resource);
				clientEventParser.parse(document.getDocumentElement(), context);
			}
			else {
				logger.warn("File not exists - [" + resource + "]");
			}
		}
	}

	@Override
	public void onStartup() throws Exception {
		if (configLocations != null) {
			String[] locations = new String[configLocations.size()];
			configLocations.toArray(locations);
			loadConfigs(ResourceUtils.getResources(locations));
		}
	}
}
