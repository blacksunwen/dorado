package com.bstek.dorado.view.config;

import java.util.List;
import java.util.Map;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.view.ViewContextTestCase;
import com.bstek.dorado.view.config.definition.ControlDefinition;
import com.bstek.dorado.view.config.definition.DataSetDefinition;
import com.bstek.dorado.view.config.definition.LayoutDefinition;
import com.bstek.dorado.view.config.definition.ViewConfigDefinition;
import com.bstek.dorado.view.config.definition.ViewDefinition;

public class XmlConfigViewConfigFactoryTest extends ViewContextTestCase {

	private ViewConfigDefinitionFactory getViewConfigDefinitionFactory(
			Context context) throws Exception {
		return (ViewConfigDefinitionFactory) context
				.getServiceBean("xmlViewConfigDefinitionFactory");
	}

	@SuppressWarnings("unchecked")
	public void test1() throws Exception {
		Context context = Context.getCurrent();
		ViewConfigDefinitionFactory viewConfigFactory = getViewConfigDefinitionFactory(context);
		ViewConfigInfo viewConfigInfo = viewConfigFactory
				.getViewConfigInfo("com/bstek/dorado/view/config/xml/TestView1");
		ViewConfigDefinition viewConfigDefinition = viewConfigFactory
				.create(viewConfigInfo);
		assertNotNull(viewConfigDefinition);

		ViewDefinition viewDefinition = viewConfigDefinition
				.getViewDefinition();
		assertNotNull(viewDefinition);

		LayoutDefinition layout = viewDefinition.getLayout();
		assertNotNull(layout);
		assertEquals("anchor", layout.getType().toLowerCase());
		assertEquals("10", layout.getProperty("padding"));

		DataSetDefinition ds1 = (DataSetDefinition) viewDefinition
				.getComponent("ds1");
		assertNotNull(ds1);

		DataSetDefinition ds2 = (DataSetDefinition) viewDefinition
				.getComponent("ds2");
		assertNotNull(ds2);

		ControlDefinition toolbar1 = (ControlDefinition) viewDefinition
				.getComponent("toolbar1");
		assertNotNull(toolbar1);

		Map<String, Object> layoutConstraint = (Map<String, Object>) toolbar1
				.getLayoutConstraint();
		assertNotNull(layoutConstraint);
		assertEquals("0", layoutConstraint.get("left"));
		assertEquals("0", layoutConstraint.get("top"));
		assertEquals("0", layoutConstraint.get("right"));

		List<ControlDefinition> items = (List<ControlDefinition>) toolbar1
				.getProperty("items");
		assertNotNull(items);
		assertEquals(3, items.size());

		ControlDefinition button = items.get(0);
		assertNotNull(button);
		assertEquals("com.bstek.dorado.view.widget.base.Button",
				button.getImpl());
		assertEquals("Test Button", button.getProperty("caption"));

		ControlDefinition toolbarButton = items.get(1);
		assertNotNull(toolbarButton);
		assertEquals("com.bstek.dorado.view.widget.base.toolbar.Button",
				toolbarButton.getImpl());
		assertEquals("Test ToolBarButton", toolbarButton.getProperty("caption"));

		ControlDefinition checkBox = items.get(2);
		assertNotNull(checkBox);
		assertEquals("com.bstek.dorado.view.widget.form.CheckBox",
				checkBox.getImpl());
	}

	public void test2() throws Exception {
		Context context = Context.getCurrent();
		ViewConfigDefinitionFactory viewConfigFactory = getViewConfigDefinitionFactory(context);
		ViewConfigInfo viewConfigInfo = viewConfigFactory
				.getViewConfigInfo("com/bstek/dorado/view/config/xml/TestView2");
		ViewConfigDefinition viewConfigDefinition = viewConfigFactory
				.create(viewConfigInfo);
		assertNotNull(viewConfigDefinition);
	}
}
