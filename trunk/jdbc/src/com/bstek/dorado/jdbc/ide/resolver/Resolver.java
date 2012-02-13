package com.bstek.dorado.jdbc.ide.resolver;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.web.resolver.AbstractTextualResolver;

public abstract class Resolver extends AbstractTextualResolver {

	public abstract String getContent(HttpServletRequest request,
			HttpServletResponse response);
	
	protected void output(String result, PrintWriter writer) {
		if (StringUtils.isNotEmpty(result)) {
			try {
				writer.write(result);
			} finally {
				writer.flush();
				writer.close();
			}
		}
	}
	
	@Override
	public void execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String content = getContent(request, response);
		if (StringUtils.isEmpty(content)) {
			content="";
		}
		PrintWriter writer = getWriter(request, response);
		output(content, writer);
	}
}
