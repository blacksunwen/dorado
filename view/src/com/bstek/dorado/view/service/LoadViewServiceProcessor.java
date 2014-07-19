/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.view.service;

import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.data.JsonUtils;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.manager.ViewConfigManager;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.Outputter;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2014-7-15
 */
public class LoadViewServiceProcessor implements ServiceProcessor {
	private ViewConfigManager viewConfigManager;
	private Outputter viewOutputter;

	public void setViewConfigManager(ViewConfigManager viewConfigManager) {
		this.viewConfigManager = viewConfigManager;
	}

	public void setViewOutputter(Outputter viewOutputter) {
		this.viewOutputter = viewOutputter;
	}

	protected ViewConfig getViewConfig(DoradoContext context, String viewName,
			Map<String, Object> viewContext) throws Exception {
		Map<String, Object> oldContextValues = null;
		try {
			if (viewContext != null && !viewContext.isEmpty()) {
				oldContextValues = new HashMap<String, Object>();
				for (Map.Entry<String, Object> entry : viewContext.entrySet()) {
					String key = entry.getKey();
					oldContextValues.put(key, context.getAttribute(key));
					context.setAttribute(key, entry.getValue());
				}
			}

			return viewConfigManager.getViewConfig(viewName);
		} finally {
			if (oldContextValues != null) {
				for (Map.Entry<String, Object> entry : oldContextValues
						.entrySet()) {
					String key = entry.getKey();
					context.setAttribute(key, oldContextValues.get(key));
				}
			}
		}
	}

	public void execute(Writer writer, ObjectNode objectNode,
			DoradoContext context) throws Exception {
		String viewName = JsonUtils.getString(objectNode, "viewName");

		Map<String, Object> viewContext = new HashMap<String, Object>();
		JsonNode rudeContext = objectNode.get("context");
		if (rudeContext != null && !rudeContext.isNull()) {
			Iterator<Entry<String, JsonNode>> fields = rudeContext.getFields();
			while (fields.hasNext()) {
				Entry<String, JsonNode> entry = fields.next();
				String key = entry.getKey();
				JsonNode jsonValue = rudeContext.get(key);
				Object value = null;
				if (jsonValue != null) {
					value = JsonUtils.toJavaObject(jsonValue, null);
				}
				viewContext.put(key, value);
			}
		}

		ViewConfig viewConfig = getViewConfig(context, viewName, viewContext);

		OutputContext outputContext = new OutputContext(writer);
		outputContext.setUsePrettyJson(Configure
				.getBoolean("view.outputPrettyJson"));

		JsonBuilder jsonBuilder = outputContext.getJsonBuilder();

		jsonBuilder.object();
		jsonBuilder.key("view");
		jsonBuilder.beginValue();
		View view = viewConfig.getView();
		if (view != null) {
			viewOutputter.output(view, outputContext);
		}
		jsonBuilder.endValue();
		jsonBuilder.endObject();
	}

}
