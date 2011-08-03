package com.bstek.dorado.ofc;

import java.io.PrintWriter;
import java.io.Writer;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.Outputter;

public class ChartOutputTest extends ChartContextTestCase {
	private static final String TEST_VIEW_1 = "com.bstek.dorado.ofc.Test1";

	protected Outputter getChartOutputter() throws Exception {
		return (Outputter) Context.getCurrent().getServiceBean(
				"controlOutputter");
	}
	
	protected Outputter getViewOutputter() throws Exception{
		return (Outputter) Context.getCurrent().getServiceBean("viewOutputter");
	}

	public void test1() throws Exception {
		View view = getView(TEST_VIEW_1);
		assertNotNull(view);

		OpenFlashChart chart1 = (OpenFlashChart) view.getComponent("chart1");
		assertNotNull(chart1);

		Writer writer = new PrintWriter(System.out);
		writer.append("\n===== OpenFlashChart ======\n");

		OutputContext outputContext = new OutputContext(writer);
		outputContext.setUsePrettyJson(true);
		try {
			getViewOutputter().output(view, outputContext);
		} finally {
			writer.close();
		}
	}
}
