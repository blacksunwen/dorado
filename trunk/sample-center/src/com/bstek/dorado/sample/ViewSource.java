package com.bstek.dorado.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.dorado.core.Constants;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.sample.dao.ExampleSourceDao;
import com.bstek.dorado.sample.entity.ExampleSource;
import com.bstek.dorado.web.DoradoContext;

@Component
public class ViewSource {

	@Resource
	private ExampleSourceDao exampleSourceDao;

	public void outputSummary(Writer writer) throws Exception {
		DoradoContext context = DoradoContext.getCurrent();
		HttpServletRequest request = context.getRequest();
		long sourceId = Long.valueOf(request.getParameter("sourceId"));
		ExampleSource exampleSource = exampleSourceDao.get(sourceId);
		context.setAttribute("exampleSource", exampleSource);

		String summary = exampleSource.getSummary();
		if (StringUtils.isNotEmpty(summary)) {
			String html = BikiUtils.render(summary);
			writer.append(html);
		}
	}

	private void doOutputSource(Writer writer,
			com.bstek.dorado.core.io.Resource resource) throws IOException {
		InputStream in = resource.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				Constants.DEFAULT_CHARSET));
		try {
			while (reader.ready()) {
				String line = reader.readLine();
				StringEscapeUtils.escapeHtml(writer, line);
				writer.append('\n');
			}
		} finally {
			in.close();
		}
	}

	protected String getHighlightBrushName(String path) {
		String extFileName = StringUtils.substringAfterLast(path, ".");
		if ("xml".equalsIgnoreCase(extFileName)) {
			return "xml";
		} else if ("java".equalsIgnoreCase(extFileName)) {
			return "java";
		} else if ("javascript".equalsIgnoreCase(extFileName)) {
			return "javascript";
		} else {
			return "xml";
		}
	}

	public void outputSource(Writer writer) throws Exception {
		Context context = Context.getCurrent();
		ExampleSource exampleSource = (ExampleSource) context
				.getAttribute("exampleSource");
		String path = exampleSource.getPath();

		writer.append("<code class=\"").append(getHighlightBrushName(path))
				.append("\">\n");
		com.bstek.dorado.core.io.Resource resource = Context.getCurrent()
				.getResource(path);
		if (resource.exists()) {
			doOutputSource(writer, resource);
		} else {
			writer.append("File not exists.");
		}
		writer.append("\n</code>");
	}
}
