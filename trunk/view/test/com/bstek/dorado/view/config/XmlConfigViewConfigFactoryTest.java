package com.bstek.dorado.view.config;


import com.bstek.dorado.core.Context;
import com.bstek.dorado.view.ViewContextTestCase;
import com.bstek.dorado.view.config.definition.ViewConfigDefinition;

public class XmlConfigViewConfigFactoryTest extends ViewContextTestCase {

	public void test() throws Exception {
		Context context = Context.getCurrent();
		ViewConfigDefinitionFactory viewConfigFactory = (ViewConfigDefinitionFactory) context
				.getServiceBean("xmlViewConfigDefinitionFactory");
		ViewConfigInfo viewConfigInfo = viewConfigFactory
				.getViewConfigInfo("com/bstek/dorado/view/config/xml/TestView1");
		ViewConfigDefinition viewConfigDefinition = viewConfigFactory.create(viewConfigInfo);
		assertNotNull(viewConfigDefinition);
	}
}
