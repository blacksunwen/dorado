package com.bstek.dorado.view.manager;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.type.AggregationDataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewContextTestCase;
import com.bstek.dorado.view.manager.ViewConfigManager;
import com.bstek.dorado.view.widget.data.DataSet;

public class DefaultViewConfigManagerTest extends ViewContextTestCase {

	private ViewConfigManager getViewConfigManager() throws Exception {
		Context context = Context.getCurrent();
		ViewConfigManager viewConfigManager = (ViewConfigManager) context
				.getServiceBean("viewConfigManager");
		return viewConfigManager;
	}

	private ViewConfig getTestViewConfig() throws Exception {
		ViewConfigManager viewConfigManager = getViewConfigManager();
		ViewConfig viewConfig = viewConfigManager
				.getViewConfig("com/bstek/dorado/view/config/xml/TestView1");
		return viewConfig;
	}

	public void test1() throws Exception {
		ViewConfig viewConfig = getTestViewConfig();
		assertNotNull(viewConfig);

		View view = viewConfig.getView();
		DataSet ds1 = (DataSet) view.getComponent("ds1");
		assertNotNull(ds1);

		DataProvider dataProvider = ds1.getDataProvider();
		assertNotNull(dataProvider);
		assertEquals("testProvider1", dataProvider.getName());

		AggregationDataType resultDataType = (AggregationDataType) dataProvider
				.getResultDataType();
		assertNotNull(resultDataType);
	}

	public void test2() throws Exception {
		ViewConfig viewConfig = getTestViewConfig();
		assertNotNull(viewConfig);

		View view = viewConfig.getView();

		DataSet ds2 = (DataSet) view.getComponent("ds2");
		assertNotNull(ds2);

		DataProvider dataProvider = ds2.getDataProvider();
		assertNotNull(dataProvider);
		assertFalse("testProvider1".equals(dataProvider.getName()));

		AggregationDataType resultDataType = (AggregationDataType) dataProvider
				.getResultDataType();
		assertNotNull(resultDataType);

		EntityDataType entityDataType = (EntityDataType) resultDataType
				.getElementDataType();
		assertNotNull(entityDataType);
		assertEquals(5, entityDataType.getPropertyDefs().size());

		PropertyDef property2 = entityDataType.getPropertyDef("key2");
		assertNotNull(property2);
		assertEquals("Key 2", property2.getLabel());

		PropertyDef property5 = entityDataType.getPropertyDef("key5");
		assertNotNull(property5);
		assertEquals("Key 5", property5.getLabel());
	}
}
