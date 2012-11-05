/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.view.widget;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.common.event.DefaultClientEvent;
import com.bstek.dorado.data.Constants;
import com.bstek.dorado.util.StringAliasUtils;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.config.xml.ViewXmlConstants;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.manager.ViewConfigManager;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.ObjectOutputterDispatcher;
import com.bstek.dorado.view.output.OutputContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-6-19
 */
public class SubViewPropertyOutputter extends ObjectOutputterDispatcher {
	private ViewConfigManager viewConfigManager;

	public void setViewConfigManager(ViewConfigManager viewConfigManager) {
		this.viewConfigManager = viewConfigManager;
	}

	@Override
	protected void outputObject(Object object, OutputContext context)
			throws Exception {
		String viewName = (String) object;
		if (StringUtils.isNotEmpty(viewName)) {
			ViewConfig viewConfig = viewConfigManager.getViewConfig(viewName);
			View view = null;
			JsonBuilder jsonBuilder = context.getJsonBuilder();
			jsonBuilder.beginValue();
			if (viewConfig != null) {
				view = viewConfig.getView();
				if (view != null) {
					String dataTypeIdPrefix = context
							.getOutputtableDataTypeIdPrefix();
					context.setOutputtableDataTypeIdPrefix(ViewXmlConstants.PATH_VIEW_SHORT_NAME
							+ Constants.PRIVATE_DATA_OBJECT_SUBFIX
							+ StringAliasUtils.getUniqueAlias(view.getName())
							+ ViewXmlConstants.PATH_COMPONENT_PREFIX);
					try {
						super.outputObject(view, context);
					} finally {
						context.setOutputtableDataTypeIdPrefix(dataTypeIdPrefix);
					}
				}
			}
			jsonBuilder.endValue();

			if (view != null && StringUtils.isNotEmpty(view.getPageTemplate())) {
				String calloutId = "subview_" + context.getCalloutId();
				context.addCalloutHtml(view, calloutId);

				StringBuffer script = new StringBuffer();
				script.append("self.assignDom(document.getElementById(\"")
						.append(calloutId).append("\"));");

				view.addClientEventListener("onCreate", new DefaultClientEvent(
						script.toString()));
			}
		}
	}
}
