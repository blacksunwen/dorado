package com.bstek.dorado.view;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;
import com.bstek.dorado.data.DataContextTestCase;
import com.bstek.dorado.view.config.definition.ViewDefinition;
import com.bstek.dorado.view.config.xml.ViewParseContext;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.manager.ViewConfigManager;

public abstract class ViewContextTestCase extends DataContextTestCase {
	public ViewContextTestCase() {
		addExtensionContextConfigLocation("com/bstek/dorado/view/context.xml");
		addExtensionContextConfigLocation("com/bstek/dorado/view/test-context.xml");
	}

	protected ViewDefinition getViewDefinition(String path) throws Exception {
		Context context = Context.getCurrent();
		XmlDocumentBuilder xmlDocumentBuilder = (XmlDocumentBuilder) context
				.getServiceBean("xmlDocumentBuilder");
		XmlParser xmlParser = (XmlParser) context
				.getServiceBean("viewDocumentElementParser");

		Resource resource = context.getResource(path);

		ViewParseContext parseContext = new ViewParseContext("TestView");
		parseContext.setResource(resource);

		Document document = xmlDocumentBuilder.loadDocument(resource);
		Element documentElement = document.getDocumentElement();
		ViewDefinition viewDefinition = (ViewDefinition) xmlParser.parse(
				documentElement, parseContext);
		return viewDefinition;
	}

	protected ViewConfig getViewConfig(String viewName) throws Exception {
		Context context = Context.getCurrent();
		ViewConfigManager viewConfigManager = (ViewConfigManager) context
				.getServiceBean("viewConfigManager");
		return viewConfigManager.getViewConfig(viewName);
	}

	protected View getView(String viewName) throws Exception {
		return getViewConfig(viewName).getView();
	}
}
