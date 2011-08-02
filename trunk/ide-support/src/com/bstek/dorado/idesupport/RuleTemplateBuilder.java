package com.bstek.dorado.idesupport;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ClassUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.config.xml.DispatchableXmlParser;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.pkgs.PackageInfo;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;
import com.bstek.dorado.idesupport.initializer.InitializerContext;
import com.bstek.dorado.idesupport.initializer.RuleTemplateInitializer;
import com.bstek.dorado.idesupport.parse.ConfigRuleParseContext;
import com.bstek.dorado.idesupport.template.ChildTemplate;
import com.bstek.dorado.idesupport.template.RuleTemplate;
import com.bstek.dorado.util.PathUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-18
 */
public class RuleTemplateBuilder implements RuleTemplateManagerListener {
	private XmlDocumentBuilder xmlDocumentBuilder;
	private XmlParser preloadParser;
	private XmlParser ruleTemplateParser;
	private Map<String, RuleTemplateInitializer> initializerNameMap;
	private Map<Class<?>, RuleTemplateInitializer> initializerTypeMap;

	private List<String> configTemplateFiles;

	private int initializingRuleTemplate = 0;
	private InitializerContext initializerContext;

	public void setXmlDocumentBuilder(XmlDocumentBuilder xmlDocumentBuilder) {
		this.xmlDocumentBuilder = xmlDocumentBuilder;
	}

	public void setConfigTemplateFiles(List<String> configTemplateFiles) {
		this.configTemplateFiles = configTemplateFiles;
	}

	public void setPreloadParser(XmlParser preloadParser) {
		this.preloadParser = preloadParser;
	}

	public void setRuleTemplateParser(XmlParser ruleTemplateParser) {
		this.ruleTemplateParser = ruleTemplateParser;
	}

	public void setInitializerMap(
			Map<String, RuleTemplateInitializer> initializerMap)
			throws ClassNotFoundException, LinkageError {
		this.initializerNameMap = new LinkedHashMap<String, RuleTemplateInitializer>();
		this.initializerTypeMap = new LinkedHashMap<Class<?>, RuleTemplateInitializer>();
		for (Map.Entry<String, RuleTemplateInitializer> entry : initializerMap
				.entrySet()) {
			String key = entry.getKey();
			if (key.startsWith("classType:")) {
				initializerTypeMap.put(ClassUtils.forName(key.substring(10),
						getClass().getClassLoader()), entry.getValue());
			} else {
				initializerNameMap.put(key, entry.getValue());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized RuleTemplateManager getRuleTemplateManager()
			throws Exception {
		ConfigRuleParseContext parserContext = new ConfigRuleParseContext();
		RuleTemplateManager ruleTemplateManager = (RuleTemplateManager) Context
				.getCurrent().getServiceBean("idesupport.ruleTemplateManager");
		ruleTemplateManager.addListener(this);
		parserContext.setRuleTemplateManager(ruleTemplateManager);

		for (String configTemplateFile : configTemplateFiles) {
			parseConfigTemplate(parserContext, configTemplateFile);
		}

		ruleTemplateManager.getPackageInfos().clear();
		List<PackageInfo> packageInfos = (List<PackageInfo>) parserContext
				.getAttributes().get("packageInfos");
		if (packageInfos != null) {
			ruleTemplateManager.getPackageInfos().addAll(packageInfos);
		}

		for (RuleTemplate ruleTemplate : parserContext.getRuleTemplateMap()
				.values()) {
			ruleTemplateManager.addRuleTemplate(ruleTemplate);
		}
		initRuleTemplates(ruleTemplateManager);
		return ruleTemplateManager;
	}

	private Resource getResource(String file) throws FileNotFoundException {
		Resource resource = Context.getCurrent().getResource(file);
		if (!resource.exists()) {
			throw new FileNotFoundException("File not exists - [" + resource
					+ "]");
		}
		return resource;
	}

	protected void parseConfigTemplate(ConfigRuleParseContext parserContext,
			String templateFile) throws Exception {
		Document document = xmlDocumentBuilder
				.loadDocument(getResource(templateFile));
		preloadParser.parse(document.getDocumentElement(), parserContext);
		Map<String, Element> ruleElementMap = parserContext.getRuleElementMap();
		for (String name : ruleElementMap.keySet().toArray(new String[0])) {
			Element element = ruleElementMap.get(name);
			ruleTemplateParser.parse(element, parserContext);
		}
	}

	protected void initRuleTemplates(RuleTemplateManager ruleTemplateManager)
			throws Exception {
		InitializerContext initializerContext = new InitializerContext(
				ruleTemplateManager);
		this.initializerContext = initializerContext;
		try {
			for (RuleTemplate ruleTemplate : ruleTemplateManager
					.getRuleTemplates().toArray(new RuleTemplate[0])) {
				initRuleTemplate(initializerContext, ruleTemplate);
			}

			for (RuleTemplate ruleTemplate : ruleTemplateManager
					.getRuleTemplates().toArray(new RuleTemplate[0])) {
				removeSelfRules(ruleTemplateManager, ruleTemplate);
			}
		} finally {
			this.initializerContext = null;
		}
	}

	private void removeSelfRules(RuleTemplateManager ruleTemplateManager,
			RuleTemplate ruleTemplate) throws Exception {
		Map<String, ChildTemplate> children = ruleTemplate.getChildren();
		for (ChildTemplate childTemplate : children.values().toArray(
				new ChildTemplate[0])) {
			RuleTemplate childRuleTemplate = childTemplate.getRuleTemplate();
			if (DispatchableXmlParser.SELF.equals(childRuleTemplate
					.getNodeName())) {
				children.remove(childTemplate.getName());
				ruleTemplateManager.removeRuleTemplate(childRuleTemplate
						.getName());

				// 此判断屏蔽于2011/01/21，导致ToolBar中的子节点信息丢失
				// if (childRuleTemplate.isGlobal()) {
				children.putAll(childRuleTemplate.getChildren());

				for (ChildTemplate selfChildTemplate : childRuleTemplate
						.getChildren().values()) {
					RuleTemplate selfChildRuleTemplate = selfChildTemplate
							.getRuleTemplate();
					boolean isGlobal = selfChildRuleTemplate.isGlobal();
					ruleTemplateManager.addRuleTemplate(selfChildRuleTemplate);
					if (!isGlobal) {
						removeSelfRules(ruleTemplateManager,
								selfChildRuleTemplate);
					}
				}
				// }
			} else if (!childRuleTemplate.isGlobal()) {
				removeSelfRules(ruleTemplateManager, childRuleTemplate);
			}
		}
	}

	protected void initRuleTemplate(InitializerContext initializerContext,
			RuleTemplate ruleTemplate) throws Exception {
		if (ruleTemplate.isInitialized()) {
			return;
		}
		ruleTemplate.setInitialized(true);

		initializingRuleTemplate++;
		try {
			String parentName = null, parentNodeName = null;
			RuleTemplate[] parents = ruleTemplate.getParents();
			if (parents != null) {
				for (RuleTemplate parent : parents) {
					initRuleTemplate(initializerContext, parent);
					if (StringUtils.isNotEmpty(parent.getName())) {
						parentName = parent.getName();
					}
					if (StringUtils.isNotEmpty(parent.getNodeName())) {
						parentNodeName = parent.getNodeName();
					}
				}
			}

			if (StringUtils.isEmpty(ruleTemplate.getName())) {
				if (StringUtils.isEmpty(ruleTemplate.getNodeName())) {
					if (parentNodeName != null) {
						ruleTemplate.setNodeName(parentNodeName);
					} else if (parentName != null) {
						ruleTemplate.setNodeName(parentName);
					} else {
						throw new IllegalArgumentException(
								"Neither [name] or [nodeName] is defined for Rule.");
					}
				}
			}

			processInitializer(ruleTemplate, initializerContext);

			Collection<ChildTemplate> children = ruleTemplate.getChildren()
					.values();
			if (!children.isEmpty()) {
				for (ChildTemplate childTemplate : children) {
					initRuleTemplate(initializerContext,
							childTemplate.getRuleTemplate());
				}
			}
		} finally {
			initializingRuleTemplate--;
		}
	}

	protected void processInitializer(RuleTemplate ruleTemplate,
			InitializerContext initializerContext) throws Exception {
		String ruleName = ruleTemplate.getName();
		for (Map.Entry<String, RuleTemplateInitializer> entry : initializerNameMap
				.entrySet()) {
			if (PathUtils.match(entry.getKey(), ruleName)) {
				RuleTemplateInitializer initializer = entry.getValue();
				initializer.initRuleTemplate(ruleTemplate, initializerContext);
			}
		}

		String typeName = ruleTemplate.getType();
		Class<?> type = null;
		if (StringUtils.isNotEmpty(typeName)) {
			type = ClassUtils.forName(typeName, getClass().getClassLoader());
		}
		if (type != null) {
			for (Map.Entry<Class<?>, RuleTemplateInitializer> entry : initializerTypeMap
					.entrySet()) {
				if (entry.getKey().isAssignableFrom(type)) {
					RuleTemplateInitializer initializer = entry.getValue();
					initializer.initRuleTemplate(ruleTemplate,
							initializerContext);
				}
			}
		}
	}

	public void ruleTemplateAdded(RuleTemplateManager ruleTemplateManager,
			RuleTemplate ruleTemplate) throws Exception {
		if (initializingRuleTemplate > 0) {
			initRuleTemplate(initializerContext, ruleTemplate);
		}
	}

	public List<String> getConfigTemplateFiles() {
		return configTemplateFiles;
	}

}
