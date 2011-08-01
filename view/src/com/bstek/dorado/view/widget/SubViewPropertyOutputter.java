package com.bstek.dorado.view.widget;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.common.event.DefaultClientEvent;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewOutputter;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.manager.ViewConfigManager;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.ObjectPropertyOutputter;
import com.bstek.dorado.view.output.OutputContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-6-19
 */
public class SubViewPropertyOutputter extends ObjectPropertyOutputter {
	private ViewConfigManager viewConfigManager;
	private ViewOutputter viewOutputter;

	public void setViewConfigManager(ViewConfigManager viewConfigManager) {
		this.viewConfigManager = viewConfigManager;
	}

	public void setViewOutputter(ViewOutputter viewOutputter) {
		this.viewOutputter = viewOutputter;
	}

	@Override
	public void output(Object value, OutputContext context) throws Exception {
		String viewName = (String) value;
		if (StringUtils.isNotEmpty(viewName)) {
			ViewConfig viewConfig = viewConfigManager.getViewConfig(viewName);
			View view = null;
			JsonBuilder jsonBuilder = context.getJsonBuilder();
			jsonBuilder.beginValue();
			if (viewConfig != null) {
				view = viewConfig.getView();
				if (view != null) {
					viewOutputter.output(view, context);
				}
			}
			jsonBuilder.endValue();

			if (view != null && StringUtils.isNotEmpty(view.getPageTemplate())) {
				String calloutId = "subview_" + context.getCalloutId();
				context.getCalloutHtmlMap().put(view, calloutId);

				StringBuffer script = new StringBuffer();
				script.append("self.assignDom(document.getElementById(\"")
						.append(calloutId).append("\"));");

				view.addClientEventListener("onCreate", new DefaultClientEvent(
						script.toString()));
			}
		}
	}
}
