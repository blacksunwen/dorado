package com.bstek.dorado.view;

import java.io.Writer;
import java.util.Map;

import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.Outputter;
import com.bstek.dorado.view.widget.ChildrenComponentOutputter;
import com.bstek.dorado.view.widget.ContainerOutputter;

/**
 * 视图对象的输出器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 19, 2008
 */
public class ViewOutputter extends ContainerOutputter {

	protected ChildrenComponentOutputter childrenComponentOutputter;
	protected Outputter includeDataTypesOutputter;

	public void setChildrenComponentOutputter(
			ChildrenComponentOutputter childrenComponentOutputter) {
		this.childrenComponentOutputter = childrenComponentOutputter;
	}

	public void setIncludeDataTypesOutputter(Outputter includeDataTypesOutputter) {
		this.includeDataTypesOutputter = includeDataTypesOutputter;
	}

	public ViewOutputter() {
		setUseTypedJson(false);
	}

	protected void outputView(View view, OutputContext context)
			throws Exception {
		Writer writer = context.getWriter();
		context.createJsonBuilder();
		try {
			writer.append("var v=");
			super.output(view, context);
			writer.append(";\n");

			// 输出顶层的子组件
			boolean hasChild = !view.getChildren().isEmpty();
			if (hasChild) {
				writer.append("function f(v){").append("v.set(\"children\",");
				childrenComponentOutputter.outputChildrenComponents(view,
						context); // 事实上此array不可能为空，前面已判断过了。
				writer.append(");").append("}\n");
			}

			// 输出DataType
			outputIncludeDataTypes(context);

			if (hasChild)
				writer.append("f(v);\n");
		} finally {
			context.restoreJsonBuilder();
		}
	}

	@Override
	public void output(Object object, OutputContext context) throws Exception {
		Writer writer = context.getWriter();
		writer.append("(function(){\n");
		outputView((View) object, context);
		writer.append("return v;\n").append("})()");
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
		writer.write("v.get(\"dataTypeRepository\").parseJsonData(");
		includeDataTypesOutputter.output(includeDataTypes, context);
		writer.write(");\n");
	}
}
