package com.bstek.dorado.view;

import java.io.Writer;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.view.config.attachment.AttachedResourceManager;
import com.bstek.dorado.view.config.attachment.JavaScriptContent;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.Outputter;
import com.bstek.dorado.view.widget.ContainerOutputter;

/**
 * 视图对象的输出器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 19, 2008
 */
public class ViewOutputter extends ContainerOutputter {
	protected Outputter childrenComponentOutputter;
	protected Outputter includeDataTypesOutputter;
	private AttachedResourceManager javaScriptResourceManager;
	private AttachedResourceManager styleSheetResourceManager;

	public void setChildrenComponentOutputter(
			Outputter childrenComponentOutputter) {
		this.childrenComponentOutputter = childrenComponentOutputter;
	}

	public void setIncludeDataTypesOutputter(Outputter includeDataTypesOutputter) {
		this.includeDataTypesOutputter = includeDataTypesOutputter;
	}

	public void setJavaScriptResourceManager(
			AttachedResourceManager javaScriptResourceManager) {
		this.javaScriptResourceManager = javaScriptResourceManager;
	}

	public void setStyleSheetResourceManager(
			AttachedResourceManager styleSheetResourceManager) {
		this.styleSheetResourceManager = styleSheetResourceManager;
	}

	public ViewOutputter() {
		setUsePrototype(true);
	}

	public void outputView(View view, OutputContext context) throws Exception {
		String exPackages = view.getPackages();
		if (StringUtils.isNotEmpty(exPackages)) {
			for (String pkg : StringUtils.split(exPackages, ",;")) {
				if (StringUtils.isNotEmpty(pkg)) {
					context.addDependsPackage(pkg);
				}
			}
		}

		Writer writer = context.getWriter();
		context.createJsonBuilder();
		try {
			writer.append("var view=");
			super.output(view, context);
			writer.append(";\n");

			// 输出顶层的子组件
			boolean hasChild = !view.getChildren().isEmpty();
			if (hasChild) {
				writer.append("function f(view){").append("view.set(\"children\",");
				childrenComponentOutputter.output(view.getChildren(), context); // 事实上此array不可能为空，前面已判断过了。
				writer.append(");").append("}\n");
			}

			// 输出DataType
			outputIncludeDataTypes(context);

			if (hasChild) {
				writer.append("f(view);\n");
			}

			Context doradoContext = Context.getCurrent();

			String javaScriptFiles = view.getJavaScriptFile();
			if (StringUtils.isNotEmpty(javaScriptFiles)) {
				for (String file : StringUtils.split(javaScriptFiles, ";,")) {
					if (StringUtils.isNotEmpty(file)) {
						Resource resource = doradoContext.getResource(file);
						JavaScriptContent content = (JavaScriptContent) javaScriptResourceManager
								.getContent(resource);
						if (content.getIsController()) {
							javaScriptResourceManager.outputContent(context,
									content);
						} else {
							context.addJavaScriptContent(content);
						}
					}
				}
			}

			String styleSheetFiles = view.getStyleSheetFile();
			if (StringUtils.isNotEmpty(styleSheetFiles)) {
				for (String file : StringUtils.split(styleSheetFiles, ",;")) {
					if (StringUtils.isNotEmpty(file)) {
						Resource resource = doradoContext.getResource(file);
						Object content = styleSheetResourceManager
								.getContent(resource);
						context.addStyleSheetContent(content);
					}
				}
			}
		} finally {
			context.restoreJsonBuilder();
		}
	}

	@Override
	public void output(Object object, OutputContext context) throws Exception {
		Writer writer = context.getWriter();
		writer.append("(function(){\n");
		outputView((View) object, context);
		writer.append("return view;\n").append("})()");
	}

	/**
	 * 输出客户端需要的DataType
	 */
	protected void outputIncludeDataTypes(OutputContext context)
			throws Exception {
		Map<String, DataType> includeDataTypes = context
				.getIncludeDataTypes(false);
		if (includeDataTypes == null || includeDataTypes.isEmpty())
			return;

		Writer writer = context.getWriter();
		writer.write("view.get(\"dataTypeRepository\").parseJsonData(");
		includeDataTypesOutputter.output(includeDataTypes, context);
		writer.write(");\n");
	}
}
