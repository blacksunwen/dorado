package com.bstek.dorado.view.resolver;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.Outputter;
import com.bstek.dorado.web.WebConfigure;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-26
 */
public class PageHeaderOutputter implements Outputter {
	private Outputter topViewOutputter;

	public void setTopViewOutputter(Outputter topViewOutputter) {
		this.topViewOutputter = topViewOutputter;
	}

	protected void writeSetting(Writer writer, String key, Object value,
			Object escapeValue, boolean quote) throws IOException {
		if (value == escapeValue
				|| (escapeValue != null && escapeValue.equals(value))) {
			return;
		}

		writer.append("$setting[\"").append(key).append("\"]=");
		if (quote) writer.append('"');
		writer
				.append(StringEscapeUtils.escapeJavaScript(String
						.valueOf(value)));
		if (quote) writer.append('"');
		writer.append(";\n");
	}

	public void output(Object object, OutputContext context) throws Exception {
		View view = (View) object;
		Writer writer = context.getWriter();
		HttpServletRequest request = DoradoContext.getAttachedRequest();

		writer
				.append(
						"<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />\n")
				.append(
						"<meta http-equiv=\"cache-control\" content=\"no-cache\" />\n");
		if (StringUtils.isNotEmpty(view.getTitle())) {
			writer.append("<title>").append(
					StringEscapeUtils.escapeHtml(view.getTitle())).append(
					"</title>\n");
		}

		writer
				.append(
						"<script language=\"javascript\" type=\"text/javascript\" charset=\"UTF-8\" src=\"")
				.append(request.getContextPath())
				.append("/dorado/client/boot.dpkg\"></script>\n")
				.append(
						"<script language=\"javascript\" type=\"text/javascript\">\n");

		writeSetting(writer, "code.debugEnabled", WebConfigure
				.getBoolean("view.debugEnabled"), Configure
				.getBoolean("view.debugEnabled"), true);

		writer.append("$import(\"widget\");\n</script>\n").append(
				"<script language=\"javascript\" type=\"text/javascript\">\n");
		topViewOutputter.output(view, context);
		writer.append("</script>\n");
	}

}
