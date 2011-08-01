package com.bstek.dorado.view.type;

import java.io.StringWriter;


import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.type.AggregationDataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;
import com.bstek.dorado.view.ViewContextTestCase;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.Outputter;

public class DataTypeOutputterTest extends ViewContextTestCase {

	private Outputter getDataTypeOutputter() throws Exception {
		Context conetxt = Context.getCurrent();
		return (Outputter) conetxt.getServiceBean("dataTypeOutputter");
	}

	private DataTypeManager getDataTypeManager() throws Exception {
		Context conetxt = Context.getCurrent();
		DataTypeManager dataTypeManager = (DataTypeManager) conetxt
				.getServiceBean("dataTypeManager");
		return dataTypeManager;
	}

	private String getOutput(Object object) throws Exception {
		StringWriter writer = new StringWriter();
		getDataTypeOutputter().output(object, new OutputContext(writer));
		return writer.toString();
	}

	public void testEntityDataType1() throws Exception {
		EntityDataType dataType = (EntityDataType) getDataTypeManager()
				.getDataType("hr.Employee");

		System.out.println(getOutput(dataType));
	}

	public void testAggregationDataType1() throws Exception {
		AggregationDataType dataType = (AggregationDataType) getDataTypeManager()
				.getDataType("hr.Managers");

		System.out.println(getOutput(dataType));
	}
}
