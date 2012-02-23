package com.bstek.dorado.view.config;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.config.definition.DefinitionManager;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceUtils;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;
import com.bstek.dorado.data.Constants;
import com.bstek.dorado.data.config.definition.DataProviderDefinition;
import com.bstek.dorado.data.config.definition.DataProviderDefinitionManager;
import com.bstek.dorado.data.config.definition.DataResolverDefinition;
import com.bstek.dorado.data.config.definition.DataResolverDefinitionManager;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.config.definition.DataTypeDefinitionManager;
import com.bstek.dorado.util.PathUtils;
import com.bstek.dorado.util.StringAliasUtils;
import com.bstek.dorado.view.config.definition.ViewConfigDefinition;
import com.bstek.dorado.view.config.xml.PreparseContext;
import com.bstek.dorado.view.config.xml.ViewConfigParserUtils;
import com.bstek.dorado.view.config.xml.ViewParseContext;
import com.bstek.dorado.view.config.xml.ViewXmlConstants;

/**
 * 利用解析XML配置文件从而获得视图配置声明对象的工厂。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 1, 2008
 */
public class XmlViewConfigDefinitionFactory implements
		ViewConfigDefinitionFactory {
	private XmlDocumentBuilder xmlDocumentBuilder;
	private String pathPrefix;
	private String pathSubfix;
	private char pathDelimChar = '.';
	private XmlDocumentPreprocessor xmlPreprocessor;
	private XmlParser xmlParser;
	private DataTypeDefinitionManager dataTypeDefinitionManager;
	private DataProviderDefinitionManager dataProviderDefinitionManager;
	private DataResolverDefinitionManager dataResolverDefinitionManager;

	/**
	 * 设置XML配置文件构建类。
	 */
	public void setXmlDocumentBuilder(XmlDocumentBuilder xmlDocumentBuilder) {
		this.xmlDocumentBuilder = xmlDocumentBuilder;
	}

	/**
	 * 返回文件路径的前缀。
	 */
	public String getPathPrefix() {
		return pathPrefix;
	}

	/**
	 * 设置文件路径的前缀。
	 */
	public void setPathPrefix(String pathPrefix) {
		if (pathPrefix.endsWith("/")) {
			pathPrefix = pathPrefix.substring(0, pathPrefix.length() - 1);
		}
		this.pathPrefix = pathPrefix;
	}

	/**
	 * 返回文件路径的后缀。
	 */
	public String getPathSubfix() {
		return pathSubfix;
	}

	/**
	 * 设置文件路径的后缀。
	 */
	public void setPathSubfix(String pathSubfix) {
		this.pathSubfix = pathSubfix;
	}

	public char getPathDelimChar() {
		return pathDelimChar;
	}

	public void setPathDelimChar(char pathDelimChar) {
		this.pathDelimChar = pathDelimChar;
	}

	public void setXmlPreprocessor(XmlDocumentPreprocessor xmlPreprocessor) {
		this.xmlPreprocessor = xmlPreprocessor;
	}

	/**
	 * 设置XML配置文件的根解析器。
	 */
	public void setXmlParser(XmlParser xmlParser) {
		this.xmlParser = xmlParser;
	}

	public void setDataTypeDefinitionManager(
			DataTypeDefinitionManager dataTypeDefinitionManager) {
		this.dataTypeDefinitionManager = dataTypeDefinitionManager;
	}

	public void setDataProviderDefinitionManager(
			DataProviderDefinitionManager dataProviderDefinitionManager) {
		this.dataProviderDefinitionManager = dataProviderDefinitionManager;
	}

	public void setDataResolverDefinitionManager(
			DataResolverDefinitionManager dataResolverDefinitionManager) {
		this.dataResolverDefinitionManager = dataResolverDefinitionManager;
	}

	protected Resource getResource(String viewName, String pathSubfix)
			throws Exception {
		String path = getResoucePath(viewName, pathSubfix);
		Resource[] resources = ResourceUtils.getResources(path);
		if (resources.length == 0) {
			throw new IllegalArgumentException("Resource[" + path
					+ "] not exists.");
		} else if (resources.length > 1) {
			throw new IllegalArgumentException(
					"More than one resources found by path [" + path + "].");
		}

		return resources[0];
	}

	private String getResoucePath(String viewName, String pathSubfix) {
		String path = viewName;
		if (pathDelimChar != PathUtils.PATH_DELIM) {
			path = path.replace(pathDelimChar, PathUtils.PATH_DELIM);
		}
		if (StringUtils.isNotEmpty(pathPrefix)) {
			path = PathUtils.concatPath(pathPrefix, path);
		}

		if (StringUtils.isNotEmpty(pathSubfix)) {
			path += pathSubfix;
		}
		return path;
	}

	public ViewConfigInfo getViewConfigInfo(String viewName) throws Exception {
		Resource resource = getResource(viewName, pathSubfix);
		Document document = xmlDocumentBuilder.loadDocument(resource);
		PreparseContext preparseContext = new PreparseContext(viewName);

		Element viewElement = ViewConfigParserUtils.findViewElement(
				document.getDocumentElement(), preparseContext.getResource());
		Resource tempResource;

		if (xmlPreprocessor.getPropertyValue(viewElement,
				ViewXmlConstants.ATTRIBUTE_PAGE_TEMPALTE) == null) {
			tempResource = getResource(viewName, ".html");
			if (tempResource.exists()) {
				viewElement.setAttribute(
						ViewXmlConstants.ATTRIBUTE_PAGE_TEMPALTE,
						getResoucePath(viewName, ".html"));
			}
		}

		if (xmlPreprocessor.getPropertyValue(viewElement,
				ViewXmlConstants.ATTRIBUTE_JAVASCRIPT_FILE) == null) {
			tempResource = getResource(viewName, ".js");
			if (tempResource.exists()) {
				viewElement.setAttribute(
						ViewXmlConstants.ATTRIBUTE_JAVASCRIPT_FILE,
						getResoucePath(viewName, ".js"));
			}
		}

		if (xmlPreprocessor.getPropertyValue(viewElement,
				ViewXmlConstants.ATTRIBUTE_STYLESHEET_FILE) == null) {
			tempResource = getResource(viewName, ".css");
			if (tempResource.exists()) {
				viewElement.setAttribute(
						ViewXmlConstants.ATTRIBUTE_STYLESHEET_FILE,
						getResoucePath(viewName, ".css"));
			}
		}

		document = xmlPreprocessor.process(viewName, document);
		return new ViewConfigInfo(viewName, resource, document);
	}

	public ViewConfigDefinition create(ViewConfigInfo viewConfigInfo)
			throws Exception {
		Document document = (Document) viewConfigInfo.getConfigModel();
		String viewName = viewConfigInfo.getViewName();

		ViewParseContext parseContext = new ViewParseContext(viewName);
		parseContext.setResource(viewConfigInfo.getResource());

		String viewObjectNamePrefix = ViewXmlConstants.PATH_VIEW_SHORT_NAME
				+ Constants.PRIVATE_DATA_OBJECT_SUBFIX
				+ StringAliasUtils
						.getUniqueAlias(ViewXmlConstants.VIEW_NAME_DELIM
								+ viewName)
				+ ViewXmlConstants.PATH_COMPONENT_PREFIX;
		parseContext.setDataObjectIdPrefix(viewObjectNamePrefix);

		InnerDataTypeDefinitionManager innerDataTypeDefinitionManager = new InnerDataTypeDefinitionManager(
				dataTypeDefinitionManager);
		innerDataTypeDefinitionManager
				.setDataObjectIdPrefix(viewObjectNamePrefix);
		parseContext
				.setDataTypeDefinitionManager(innerDataTypeDefinitionManager);

		InnerDataProviderDefinitionManager innerDataProviderDefinitionManager = new InnerDataProviderDefinitionManager(
				dataProviderDefinitionManager);
		parseContext
				.setDataProviderDefinitionManager(innerDataProviderDefinitionManager);

		InnerDataResolverDefinitionManager innerDataResolverDefinitionManager = new InnerDataResolverDefinitionManager(
				dataResolverDefinitionManager);
		parseContext
				.setDataResolverDefinitionManager(innerDataResolverDefinitionManager);

		Element documentElement = document.getDocumentElement();
		ViewConfigDefinition viewConfigDefinition = (ViewConfigDefinition) xmlParser
				.parse(documentElement, parseContext);
		viewConfigDefinition.setName(viewName);

		DefinitionManager<DataTypeDefinition> dataTypeDefinitionManager = viewConfigDefinition
				.getDataTypeDefinitionManager();
		Map<String, DataTypeDefinition> parsedDataTypes = parseContext
				.getParsedDataTypes();
		for (Map.Entry<String, DataTypeDefinition> entry : parsedDataTypes
				.entrySet()) {
			dataTypeDefinitionManager.registerDefinition(entry.getKey(),
					entry.getValue());
		}

		DefinitionManager<DataProviderDefinition> dataProviderDefinitionManager = viewConfigDefinition
				.getDataProviderDefinitionManager();
		for (Map.Entry<String, DataProviderDefinition> entry : parseContext
				.getParsedDataProviders().entrySet()) {
			dataProviderDefinitionManager.registerDefinition(entry.getKey(),
					entry.getValue());
		}

		DefinitionManager<DataResolverDefinition> dataResolverDefinitionManager = viewConfigDefinition
				.getDataResolverDefinitionManager();
		for (Map.Entry<String, DataResolverDefinition> entry : parseContext
				.getParsedDataResolvers().entrySet()) {
			dataResolverDefinitionManager.registerDefinition(entry.getKey(),
					entry.getValue());
		}
		return viewConfigDefinition;
	}

}
