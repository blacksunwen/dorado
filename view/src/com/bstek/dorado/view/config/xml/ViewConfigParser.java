package com.bstek.dorado.view.config.xml;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jexl2.JexlContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.data.config.xml.DataObjectsParser;
import com.bstek.dorado.data.config.xml.GenericObjectParser;
import com.bstek.dorado.view.config.definition.ViewConfigDefinition;
import com.bstek.dorado.view.config.definition.ViewDefinition;

/**
 * View配置文件中XML文档根节点的解析器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 27, 2008
 */
public class ViewConfigParser extends GenericObjectParser {
	private XmlParser dataObjectsPreloadParser;
	private DataObjectsParser dataObjectsParser;
	private XmlParser argumentsParser;
	private XmlParser contextParser;
	private XmlParser viewParser;

	public void setDataObjectsPreloadParser(XmlParser dataObjectsPreloadParser) {
		this.dataObjectsPreloadParser = dataObjectsPreloadParser;
	}

	public void setDataObjectsParser(DataObjectsParser dataObjectsParser) {
		this.dataObjectsParser = dataObjectsParser;
	}

	public void setArgumentsParser(XmlParser argumentsParser) {
		this.argumentsParser = argumentsParser;
	}

	public void setContextParser(XmlParser contextParser) {
		this.contextParser = contextParser;
	}

	public void setViewParser(XmlParser viewParser) {
		this.viewParser = viewParser;
	}

	@Override
	protected ObjectDefinition createDefinition(Element element,
			ParseContext context) throws Exception {
		ViewParseContext viewContext = (ViewParseContext) context;
		ViewConfigDefinition viewConfigDefinition = new ViewConfigDefinition(
				viewContext.getDataTypeDefinitionManager(),
				viewContext.getDataProviderDefinitionManager(),
				viewContext.getDataResolverDefinitionManager());
		viewContext.setViewConfigDefinition(viewConfigDefinition);
		return viewConfigDefinition;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		ViewConfigDefinition viewConfigDefinition = (ViewConfigDefinition) super
				.doParse(node, context);

		ViewParseContext viewContext = (ViewParseContext) context;
		Element documentElement = (Element) node;
		Map<String, Object> arguments = new HashMap<String, Object>();
		Map<String, Object> viewContextAttributes = new HashMap<String, Object>();

		Element argumentsElement = ViewConfigParserUtils.findArgumentsElement(
				documentElement, context);
		parseArguments(arguments, argumentsElement, context);
		viewConfigDefinition.setArguments(arguments);

		JexlContext jexlContext = getExpressionHandler().getJexlContext();
		final String ARGUMENT = "argument";
		Object originArgumentsVar = jexlContext.get(ARGUMENT);
		jexlContext.set(ARGUMENT, arguments);

		try {
			Element contextElement = ViewConfigParserUtils.findContextElement(
					documentElement, context);
			parseContext(viewContextAttributes, contextElement, context);
			viewConfigDefinition.setViewContext(viewContextAttributes);

			Element modelElement = ViewConfigParserUtils.findModelElement(
					documentElement, context);
			parseModelElement(modelElement, context);

			Element viewElement = ViewConfigParserUtils.findViewElement(
					documentElement, context);
			// parse view element
			ViewDefinition viewDefinition = (ViewDefinition) viewParser.parse(
					viewElement, context);

			viewDefinition.setDependentResources(viewContext
					.getDependentResources().toArray(new Resource[0]));
		} finally {
			jexlContext.set(ARGUMENT, originArgumentsVar);
		}
		return viewConfigDefinition;
	}

	@SuppressWarnings("unchecked")
	protected void parseArguments(Map<String, Object> arguments,
			Element argumentsElement, ParseContext context) throws Exception {
		if (argumentsElement != null) {
			Map<String, Object> argumentMap = (Map<String, Object>) argumentsParser
					.parse(argumentsElement, context);
			arguments.putAll(argumentMap);
		}
	}

	@SuppressWarnings("unchecked")
	protected void parseContext(Map<String, Object> viewContextAttributes,
			Element contextElement, ParseContext context) throws Exception {
		if (contextElement != null) {
			Map<String, Object> attributes = (Map<String, Object>) contextParser
					.parse(contextElement, context);
			viewContextAttributes.putAll(attributes);
		}
	}

	protected void preloadModels(Element modelElement, ParseContext context)
			throws Exception {
		dataObjectsPreloadParser.parse(modelElement, context);
	}

	protected void parseModelElement(Element modelElement, ParseContext context)
			throws Exception {
		if (modelElement != null) {
			preloadModels(modelElement, context);
			dataObjectsParser.parse(context);
		}
	}
}