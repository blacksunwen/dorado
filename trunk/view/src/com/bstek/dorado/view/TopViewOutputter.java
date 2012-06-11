package com.bstek.dorado.view;

import java.io.Writer;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.view.output.ClientOutputHelper;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.web.WebConfigure;

/**
 * 顶层视图对象的输出器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Nov 5, 2008
 */
public class TopViewOutputter extends ViewOutputter {
	private ClientOutputHelper clientOutputHelper;

	public void setClientOutputHelper(ClientOutputHelper clientOutputHelper) {
		this.clientOutputHelper = clientOutputHelper;
	}

	@Override
	public void output(Object object, OutputContext context) throws Exception {
		View view = (View) object;
		view.setId("viewMain");

		context.addDependsPackage("widget");
		context.addDependsPackage("common");

		Writer writer = context.getWriter();
		writer.append("jQuery(document).ready(function(){\n");
		// writer.append("try{\n");
		writer.append("AUTO_APPEND_TO_TOPVIEW=false;\n");

		ViewOutputter outputter = (ViewOutputter) clientOutputHelper
				.getOutputter(view.getClass());
		outputter.outputView(view, context);

		writer.append("AUTO_APPEND_TO_TOPVIEW=true;\n");
		writer.append("var doradoView = document.getElementById(\"doradoView\");\n"
				+ "if (doradoView) view.replace(doradoView);\n");
		// writer.append("}\n").append("catch(e){")
		// .append("dorado.Exception.processException(e);}\n");
		writer.append("});\n");

		if (WebConfigure.getBoolean("view.debugEnabled")) {
			context.addDependsPackage("debugger");
		}

		Set<String> dependsPackages = context.getDependsPackages();
		if (dependsPackages != null && !dependsPackages.isEmpty()) {
			writer.append("$import(\"")
					.append(StringUtils.join(dependsPackages, ','))
					.append("\");\n");
		}
	}
}
