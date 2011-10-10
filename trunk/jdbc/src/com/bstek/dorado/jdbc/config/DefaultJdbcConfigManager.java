package com.bstek.dorado.jdbc.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.definition.DefinitionManager;
import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceUtils;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;
import com.bstek.dorado.data.config.xml.NodeWrapper;
import com.bstek.dorado.jdbc.config.xml.JdbcParseContext;
import com.bstek.dorado.jdbc.config.xml.JdbcXmlConstants;
import com.bstek.dorado.jdbc.manager.JdbcModelManager;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.NamedObjectDefinition;

public class DefaultJdbcConfigManager implements JdbcConfigManager, ApplicationContextAware {

	private static Log logger = LogFactory.getLog(DefaultJdbcConfigManager.class);
	protected ApplicationContext ctx;
	private static final String XML_SUFFIX = ".xml";
	
	private List<String> configLocations;
	private XmlDocumentBuilder xmlDocumentBuilder;

	private JdbcModelManager jdbcModelManager;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.ctx = applicationContext;
	}
	
	public XmlDocumentBuilder getXmlDocumentBuilder() {
		return xmlDocumentBuilder;
	}

	public void setXmlDocumentBuilder(XmlDocumentBuilder xmlDocumentBuilder) {
		this.xmlDocumentBuilder = xmlDocumentBuilder;
	}

	public JdbcModelManager getJdbcModelManager() {
		return jdbcModelManager;
	}

	public void setJdbcModelManager(JdbcModelManager jdbcModelManager) {
		this.jdbcModelManager = jdbcModelManager;
	}

	public void setConfigLocations(List<String> configLocations) {
		this.configLocations = null;
		this.addConfigLocations(configLocations);
	}

	public void addConfigLocation(String configLocation) {
		if (this.configLocations == null) {
			this.configLocations = new ArrayList<String>();
		}
		this.configLocations.add(StringUtils.trim(configLocation));
	}
	
	public void addConfigLocations(List<String> configLocations) {
		if (this.configLocations == null) {
			this.configLocations = new ArrayList<String>();
		}
		for (String location : configLocations) {
			this.configLocations.add(StringUtils.trim(location));
		}
	}

	private static class DocumentWrapper {
		private Object documentObject;
		private Resource resource;

		DocumentWrapper(Object documentObject, Resource resource) {
			this.documentObject = documentObject;
			this.resource = resource;
		}

		Object getDocumentObject() {
			return documentObject;
		}

		Resource getResource() {
			return resource;
		}

	}
	
	private DocumentWrapper[] getDocuments(Resource[] resources)
			throws Exception {
		List<DocumentWrapper> documentList = new ArrayList<DocumentWrapper>();
		for (Resource resource : resources) {
			if (resource.exists()) {
				String filename = resource.getFilename().toLowerCase();
				if (filename.endsWith(XML_SUFFIX)) {
					Document document = xmlDocumentBuilder
							.loadDocument(resource);
					documentList.add(new DocumentWrapper(document, resource));
				} else {
					logger.warn("Unsupported jdbc configure - [" + resource + "]");
				}
			} else {
				logger.warn("File not exists - [" + resource + "]");
			}
		}
		return documentList.toArray(new DocumentWrapper[documentList.size()]);
	}
	
	@Override
	public void initialize() throws Exception {
		if (configLocations != null) {
			configLocations.clear();
		}
		configLocations= new ArrayList<String>();
		
		Map<String, GlobalDbModelConfig> configMap = ctx.getBeansOfType(GlobalDbModelConfig.class);
		Set<String> locationSet = new HashSet<String>();
		for (GlobalDbModelConfig config: configMap.values()) {
			locationSet.addAll(config.getConfigLocations());
		}
		configLocations.addAll(locationSet);
		
		String[] locations = new String[configLocations.size()];
		this.configLocations.toArray(locations);
		this.loadConfigs(ResourceUtils.getResources(locations));
	}

	@Override
	public void loadConfigs(Resource[] resources) throws Exception {
		DocumentWrapper[] documents = this.getDocuments(resources);
		JdbcParseContext parseContext = new JdbcParseContext();
		
		for (DocumentWrapper wrapper : documents) {
			parseContext.setResource(wrapper.getResource());
			Object documentObject = wrapper.getDocumentObject();
			if (documentObject instanceof Document) {
				this.preloadConfig((Document) documentObject, parseContext);
			}
		}
		
		DefinitionManager<NamedObjectDefinition> definitionManager = jdbcModelManager.getDefinitionManager();
		for (DbElement.Type type: DbElement.Type.values()) {
			String tagName = type.name();
			StringBuilder message = new StringBuilder("Registered " + tagName + ": [");
			
			Collection<NodeWrapper> wrappers = parseContext.getElements(tagName).values();
			List<String> names = new ArrayList<String>(wrappers.size());
			for (NodeWrapper wrapper: wrappers) {
				Node node = wrapper.getNode();
				ObjectParser parser = jdbcModelManager.getParser(type);
				NamedObjectDefinition definition = (NamedObjectDefinition)parser.parse(node, parseContext);
				definitionManager.registerDefinition(definition.getName(), definition);
				names.add(definition.getName());
			}
			
			message.append(StringUtils.join(names, ',')).append("]");
			logger.info(message.toString());
		}
	}

	protected void preloadConfig(Document document, JdbcParseContext context)
			throws Exception {
		Element documentElement = document.getDocumentElement();
		
		for (DbElement.Type type: DbElement.Type.values()) {
			String tagName = type.name();
			List<Element> elements = DomUtils.getChildElementsByTagName(documentElement, tagName);
			String envName = documentElement.getAttribute(JdbcXmlConstants.ATTRIBUTE_ENVIROMENT);
			for (Element element: elements) {
				if (StringUtils.isNotEmpty(envName)) {
					element.setAttribute(JdbcXmlConstants.ATTRIBUTE_ENVIROMENT, envName);
				}
				
				context.putElement(element);
			}
		}
	}
	
	@Override
	public void unloadConfigs(Resource[] resources) throws Exception {
		throw new UnsupportedOperationException();
	}

}
