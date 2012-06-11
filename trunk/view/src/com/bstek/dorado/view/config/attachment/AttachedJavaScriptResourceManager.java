package com.bstek.dorado.view.config.attachment;

import java.io.Writer;

import com.bstek.dorado.core.el.Expression;
import com.bstek.dorado.core.io.Resource;
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

			writer.append("(function(view){\n");

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
							jsonBuilder.value(expression);
						}
						jsonBuilder.endArray();
					}
					jsonBuilder.endObject();
				}
				jsonBuilder.endArray();
				writer.append(");");
			}

			writer.append("\n})(view);\n");
		} else {
			super.outputContent(context, javaScriptContent.getContent());
		}
	}
}
