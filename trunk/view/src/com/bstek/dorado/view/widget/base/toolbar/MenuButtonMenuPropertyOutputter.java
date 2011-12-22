package com.bstek.dorado.view.widget.base.toolbar;

import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.ObjectOutputterDispatcher;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.VirtualPropertyOutputter;
import com.bstek.dorado.view.widget.base.menu.Menu;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-8-8
 */
public class MenuButtonMenuPropertyOutputter extends ObjectOutputterDispatcher
		implements VirtualPropertyOutputter {

	public void output(Object object, String property, OutputContext context)
			throws Exception {
		Menu menu = ((MenuButton) object).getEmbededMenu();
		if (menu != null) {
			JsonBuilder jsonBuilder = context.getJsonBuilder();
			jsonBuilder.key("menu").beginValue();
			outputObject(menu, context);
			jsonBuilder.endValue();
		}
	}
}
