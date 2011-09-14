package com.bstek.dorado.view;

import java.io.Writer;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.web.WebConfigure;

/**
 * 顶层视图对象的输出器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Nov 5, 2008
 */
public class TopViewOutputter extends ViewOutputter {

	@Override
	public void output(Object object, OutputContext context) throws Exception {
		View view = (View) object;
		view.setId("viewMain");
		Set<String> dependsPackages = context.getDependsPackages();
		
		dependsPackages.add("widget");
		if (WebConfigure.getBoolean("view.debugEnabled")) {
			dependsPackages.add("debugger");
		}
		dependsPackages.add("common");

		Writer writer = context.getWriter();
		writer.append("jQuery(document).ready(function(){\n").append("try{\n");
		outputView(view, context);
		writer.append(
				"var doradoView = document.getElementById(\"doradoView\");\n"
						+ "if (doradoView) v.replace(doradoView);\n")
				.append("}\n").append("catch(e){")
				.append("dorado.Exception.processException(e);}\n");
		writer.append("});\n");

		writer.append("$import(\"")
				.append(StringUtils.join(dependsPackages.iterator(), ','))
				.append("\");\n");
	}
}
