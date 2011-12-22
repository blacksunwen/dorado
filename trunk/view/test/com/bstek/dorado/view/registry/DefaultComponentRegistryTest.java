package com.bstek.dorado.view.registry;


import com.bstek.dorado.core.Context;
import com.bstek.dorado.view.ViewContextTestCase;
import com.bstek.dorado.view.widget.MockDataSet;

public class DefaultComponentRegistryTest extends ViewContextTestCase {
	public void test() throws Exception {
		Context context = Context.getCurrent();
		ComponentTypeRegistry componentTypeRegistry = (ComponentTypeRegistry) context
				.getServiceBean("componentTypeRegistry");
		ComponentTypeRegisterInfo componentRegisterInfo;

		componentRegisterInfo = componentTypeRegistry
				.getRegisterInfo("TestDataSet");
		assertNotNull(componentRegisterInfo);

		componentRegisterInfo = componentTypeRegistry
				.getRegisterInfo(MockDataSet.class);
		assertNotNull(componentRegisterInfo);
	}
}
