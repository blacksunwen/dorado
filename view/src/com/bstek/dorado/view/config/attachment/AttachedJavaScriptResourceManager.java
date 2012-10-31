﻿/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.view.config.attachment;

import java.io.Writer;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.el.Expression;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.OutputContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-29
 */
public class AttachedJavaScriptResourceManager extends AttachedResourceManager {
	private JavaScriptParser javaScriptParser;
	private boolean asControllerInDefault = true;

	public JavaScriptParser getJavaScriptParser() {
		return javaScriptParser;
	}

	public void setJavaScriptParser(JavaScriptParser javaScriptParser) {
		this.javaScriptParser = javaScriptParser;
	}

	public boolean isAsControllerInDefault() {
		return asControllerInDefault;
	}

	public void setAsControllerInDefault(boolean asControllerInDefault) {
		this.asControllerInDefault = asControllerInDefault;
	}

	@Override
	protected Object parseContent(Resource resource) throws Exception {
		JavaScriptContent javaScriptContent = javaScriptParser.parse(resource,
				getCharset(), asControllerInDefault);
		Expression expression = getExpressionHandler().compile(
				(String) javaScriptContent.getContent());
		if (expression != null) {
			javaScriptContent.setContent(expression);
		}
		return javaScriptContent;
	}

	@Override
	public void outputContent(OutputContext context, Object content)
			throws Exception {
		JavaScriptContent javaScriptContent = (JavaScriptContent) content;
		if (javaScriptContent.getIsController()) {
			Writer writer = context.getWriter();
			JsonBuilder jsonBuilder = context.getJsonBuilder();
			writer.append("\n\n(function(view){\n");

			super.outputContent(context, javaScriptContent.getContent());

			if (javaScriptContent.getFunctionInfos() != null) {
				writer.append("\ndorado.widget.Controller.registerFunctions(view,");
				jsonBuilder.array();
				for (FunctionInfo functionInfo : javaScriptContent
						.getFunctionInfos()) {
					jsonBuilder.object();
					jsonBuilder.key("name").value(
							functionInfo.getFunctionName());

					jsonBuilder.key("func").beginValue();
					writer.append(functionInfo.getFunctionName());
					jsonBuilder.endValue();

					if (functionInfo.getShouldRegisterToGlobal()) {
						jsonBuilder.key("global").value(true);
					}
					if (functionInfo.getShouldRegisterToView()) {
						jsonBuilder.key("view").value(true);
					}

					BindingInfo bindingInfo = functionInfo.getBindingInfo();
					if (bindingInfo != null) {
						jsonBuilder.key("bindingInfos").array();
						for (String expression : bindingInfo.getExpressions()) {
							if (expression.charAt(0) == '@') {
								registerIncludeDataType(context, expression);
							}
							jsonBuilder.value(expression);
						}
						jsonBuilder.endArray();
					}
					jsonBuilder.endObject();
				}
				jsonBuilder.endArray();
				writer.append(");\n");
			}
			writer.append("})(view);\n");
		} else {
			super.outputContent(context, javaScriptContent.getContent());
		}
	}

	protected void registerIncludeDataType(OutputContext context,
			String expression) throws Exception {
		String dataTypeName = StringUtils
				.substringBetween(expression, "@", ".");
		if (StringUtils.isEmpty(dataTypeName)) {
			return;
		}

		ViewConfig viewConfig = context.getCurrentView().getViewConfig();
		if (viewConfig == null) {
			return;
		}

		DataType dataType = viewConfig.getDataType(dataTypeName);
		if (dataType == null) {
			return;
		}

		Map<String, DataType> includeDataTypes = context
				.getIncludeDataTypes(true);
		if (!includeDataTypes.containsKey(dataTypeName)) {
			includeDataTypes.put(dataTypeName, dataType);
		}
	}
}
