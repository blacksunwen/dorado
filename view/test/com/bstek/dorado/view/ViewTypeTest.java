package com.bstek.dorado.view;


import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.type.DefaultEntityDataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;

public class ViewTypeTest extends ViewContextTestCase {

	protected DataTypeManager getDataTypeManager() throws Exception {
		Context conetxt = Context.getCurrent();
		DataTypeManager dataTypeManager = (DataTypeManager) conetxt
				.getServiceBean("dataTypeManager");
		return dataTypeManager;
	}

	public void test() throws Exception {
		EntityDataType beanDataType = (EntityDataType) getDataTypeManager()
				.getDataType("Bean");
		assertNotNull(beanDataType);
		assertTrue(DefaultEntityDataType.class.isInstance(beanDataType));
	}
}
