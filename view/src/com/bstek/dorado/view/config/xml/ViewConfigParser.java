package com.bstek.dorado.view.config.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.jexl2.JexlContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.core.io.Resource;
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
	protected List<?> dispatchChildElements(Element element,
			ParseContext context) throws Exception {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		ViewConfigDefinition viewConfigDefinition = (ViewConfigDefinition) super
				.doParse(node, context);

		ViewParseContext viewContext = (ViewParseContext) context;
		Element documentElement = (Element) node;
		Map<String, Object> arguments = new HashMap<String, Object>();
		Map<String, Object> viewContextAttributes = new HashMap<String, Object>();

		Element argumentsElement = ViewConfigParserUtils.findArgumentsElement(
				documentElement, context.getResource());
		if (argumentsElement != null) {
			Map<String, Object> argumentMap = (Map<String, Object>) dispatchElement(
					null, argumentsElement, viewContext);
			arguments.putAll(argumentMap);
		}
		viewConfigDefinition.setArguments(arguments);

		JexlContext jexlContext = getExpressionHandler().getJexlContext();
		final String ARGUMENT = "argument";
		Object originArgumentsVar = jexlContext.get(ARGUMENT);
		jexlContext.set(ARGUMENT, arguments);

		try {
			Element contextElement = ViewConfigParserUtils.findContextElement(
					documentElement, context.getResource());
			if (contextElement != null) {
				Map<String, Object> attributes = (Map<String, Object>) dispatchElement(
						null, contextElement, viewContext);
				viewContextAttributes.putAll(attributes);
			}
			viewConfigDefinition.setViewContext(viewContextAttributes);

			Element modelElement = ViewConfigParserUtils.findModelElement(
					documentElement, context.getResource());
			if (modelElement != null) {
				dispatchElement(null, modelElement, viewContext);
			}

			Element viewElement = ViewConfigParserUtils.findViewElement(
					documentElement, context.getResource());
			// parse view element
			ViewDefinition viewDefinition = (ViewDefinition) dispatchElement(
					null, viewElement, viewContext);

			viewDefinition.setDependentResources(viewContext
					.getDependentResources().toArray(new Resource[0]));
		} finally {
			jexlContext.set(ARGUMENT, originArgumentsVar);
		}
		return viewConfigDefinition;
	}

}