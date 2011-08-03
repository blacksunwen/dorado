package com.bstek.dorado.ofc;

import com.bstek.dorado.view.View;

public class ChartParserTest extends ChartContextTestCase {
	private static final String TEST_VIEW_1 = "com.bstek.dorado.ofc.Test1";

	public void test1() throws Exception {
		View view = getView(TEST_VIEW_1);
		assertNotNull(view);

		OpenFlashChart chart1 = (OpenFlashChart) view.getComponent("chart1");
		assertNotNull(chart1);
		assertEquals("blue", chart1.getBackgroundColor());

		Legend legend = chart1.getLegend();
		assertNotNull(legend);
		assertEquals("yellow", legend.getBackgroundColor());

		Text xlegend = chart1.getxLegend();
		assertNull(xlegend);
	}
}
