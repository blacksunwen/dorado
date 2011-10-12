package com.bstek.dorado.jdbc.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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

import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.config.xml.XmlConstants;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceUtils;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;
import com.bstek.dorado.jdbc.config.xml.JdbcXmlConstants;
import com.bstek.dorado.jdbc.config.xml.TagedObjectParser;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.DbElement.Type;
import com.bstek.dorado.jdbc.model.DbElementCreationContext;
import com.bstek.dorado.jdbc.sql.SqlGenerator;
import com.bstek.dorado.util.Assert;

public class DefaultJdbcConfigManager implements JdbcConfigManager, ApplicationContextAware {

	private static Log logger = LogFactory.getLog(DefaultJdbcConfigManager.class);
	protected ApplicationContext ctx;
	private XmlDocumentBuilder xmlDocumentBuilder;

	private JdbcDefinitionManager definitionManager = new JdbcDefinitionManager();
	private Map<DbElement.Type, TagedObjectParser> objectParsers = new HashMap<DbElement.Type, TagedObjectParser>();
	private Map<DbElement.Type, SqlGenerator<DbElement>> sqlGenerators = new HashMap<DbElement.Type, SqlGenerator<DbElement>>();
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.ctx = applicationContext;
	}
	
	@Override
	public JdbcDefinitionManager getDefinitionManager() {
		return definitionManager;
	}
	
	public void setDefinitionManager(JdbcDefinitionManager definitionManager) {
		this.definitionManager = definitionManager;
	}
	
	public void setParsers(List<TagedObjectParser> parsers) {
		objectParsers.clear();
		for (TagedObjectParser parser: parsers) {
			String tagName = parser.getTagName();
			objectParsers.put(DbElement.Type.valueOf(tagName), parser);
		}
	}

	public void setSqlGenerators(List<SqlGenerator<DbElement>> generators) {
		sqlGenerators.clear();
		for (SqlGenerator<DbElement> generator: generators) {
			Type type = generator.getType();
			sqlGenerators.put(type, generator);
		}
	}
	
	@Override
	public DbElement getDbElement(String name) {
		ObjectDefinition definition = getDefinitionManager().getDefinition(name);
		Assert.notNull(definition, "no DbElement named [" + name + "].");
		
		DbElementCreationContext context = new DbElementCreationContext();
		try {
			return (DbElement)definition.create(context);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ObjectParser getParser(Type type) {
		Assert.notNull(type);
		TagedObjectParser parser = objectParsers.get(type);
		
		Assert.notNull(parser);
		return parser;
	}

	public SqlGenerator<DbElement> getSqlGenerator(DbElement.Type type) {
		Assert.notNull(type);
		SqlGenerator<DbElement> generator = sqlGenerators.get(type);

		Assert.notNull(generator);
		return generator;
	}
	
	public XmlDocumentBuilder getXmlDocumentBuilder() {
		return xmlDocumentBuilder;
	}

	public void setXmlDocumentBuilder(XmlDocumentBuilder xmlDocumentBuilder) {
		this.xmlDocumentBuilder = xmlDocumentBuilder;
	}

	@Override
	public void initialize() throws Exception {
		Resource[] resources = this.getInitialResources();
		Map<String, XmlElementWrapper> dbElementNodeMap = this.loadConfigs(resources);
		definitionManager.reset(dbElementNodeMap);
	}

	/**
	 * 获取初始化资源列表
	 * @return
	 * @throws IOException
	 */
	protected Resource[] getInitialResources() throws IOException {
		List<String> configLocations= new ArrayList<String>();
		
		Map<String, GlobalDbModelConfig> configMap = ctx.getBeansOfType(GlobalDbModelConfig.class);
		Set<String> locationSet = new HashSet<String>();
		for (GlobalDbModelConfig config: configMap.values()) {
			locationSet.addAll(config.getConfigLocations());
		}
		configLocations.addAll(locationSet);
		
		String[] locations = new String[configLocations.size()];
		configLocations.toArray(locations);
		return ResourceUtils.getResources(locations);
	}
	
	@Override
	public Map<String, XmlElementWrapper> loadConfigs(Resource[] resources) throws Exception {
		DocumentWrapper[] documents = JdbcConfigUtils.getDocuments(resources, getXmlDocumentBuilder());
		Map<String, XmlElementWrapper> dbElementNodeMap = new LinkedHashMap<String, XmlElementWrapper>();
		
		/*
		 * 读取全部的Document，收集DbElement节点
		 */
		for(DocumentWrapper documentWrapper: documents) {
			Document document = documentWrapper.getDocument();
			Resource resource = documentWrapper.getResource();
			
			Element documentElement = document.getDocumentElement();
			for (DbElement.Type type: DbElement.Type.values()) {
				String tagName = type.name();
				ObjectParser parser = this.getParser(type);
				List<Element> elements = DomUtils.getChildElementsByTagName(documentElement, tagName);
				String envName = documentElement.getAttribute(JdbcXmlConstants.ATTRIBUTE_ENVIROMENT);
				for (Element element: elements) {
					if (StringUtils.isNotEmpty(envName)) {
						element.setAttribute(JdbcXmlConstants.ATTRIBUTE_ENVIROMENT, envName);
					}
					
					String name = element.getAttribute(XmlConstants.ATTRIBUTE_NAME);
					if (!dbElementNodeMap.containsKey(name)){
						dbElementNodeMap.put(name, new XmlElementWrapper(element, resource, parser));
					} else {
						throw new IllegalArgumentException("Duplicate DbElelment named [" + name +"].");
					}
				}
			}
		}
		
		/*
		 * 日志输出读取到的全部DbElement节点
		 */
		Map<String, List<String>> nameListMap = new LinkedHashMap<String, List<String>>();
		for (XmlElementWrapper elementWrapper: dbElementNodeMap.values()) {
			Element element = elementWrapper.getElement();
			String name = element.getAttribute(XmlConstants.ATTRIBUTE_NAME);
			String tagName = element.getNodeName();
			List<String> names = nameListMap.get(tagName);
			if (names == null) {
				names = new ArrayList<String>();
				nameListMap.put(tagName, names);
			}
			names.add(name);
 		}
		for(String typeName: nameListMap.keySet()) {
			List<String> names = nameListMap.get(typeName);
			StringBuilder message = new StringBuilder("Registered " + typeName + ": [");
			message.append(StringUtils.join(names, ',')).append("]");
			logger.info(message.toString());
		}
		
		return dbElementNodeMap;
	}

}
