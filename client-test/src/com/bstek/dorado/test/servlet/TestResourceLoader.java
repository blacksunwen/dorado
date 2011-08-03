package com.bstek.dorado.test.servlet;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Constants;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.web.DoradoContext;

public class TestResourceLoader extends HttpServlet {
	private static final long serialVersionUID = -5575538947208468135L;

	private static final int BUFFER_SIZE = 1024;
	private static final int ONE_SECOND = 1000;

	static final String CONTENT_TYPE_HTML = "text/html";
	static final String CONTENT_TYPE_JAVASCRIPT = "text/javascript";
	static final String CONTENT_TYPE_CSS = "text/css";
	static final String CONTENT_TYPE_OCTET_STREAM = "application/octet-stream";

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String pathPrefix = StringUtils.defaultIfEmpty(
				System.getenv("DORADO7_WORKSPACE"), "");
		String urlRoot = request.getContextPath() + "/workspace";
		String path = request.getRequestURI().substring(urlRoot.length());
		String realPath;
		boolean isTestWrapper = false;

		if (path.endsWith(".jsunit.html")) {
			isTestWrapper = true;
			realPath = pathPrefix + path.substring(0, path.length() - 12)
					+ ".js";
		} else {
			realPath = pathPrefix + path;
		}

		DoradoContext context = DoradoContext
				.init(getServletContext(), request);
		try {
			Resource resource = context.getResource(realPath);
			if (!resource.exists()) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				throw new FileNotFoundException(realPath);
			}

			// 获取Client端缓存资源的最后修改时间，如果尚没有缓存将返回-1
			long cachedLastModified = request
					.getDateHeader("if-modified-since");

			// 获取Server端资源的最后修改时间
			long lastModified = resource.getTimestamp();

			// 判断是否利用Client端缓存
			if (lastModified > 0 && cachedLastModified > 0
					&& Math.abs(lastModified - cachedLastModified) < ONE_SECOND) {
				// 通知浏览器Server端的资源没有改变，可以使用Client端的缓存
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			} else {
				response.setContentType(getContentType(path));

				// 告知Client端此资源的最后修改时间
				response.addDateHeader("Last-Modified", lastModified);

				OutputStream out = new BufferedOutputStream(
						response.getOutputStream());
				try {
					PrintWriter writer = new PrintWriter(out);
					if (isTestWrapper) {
						writer.append(
								"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">\n")
								.append("<html>\n")
								.append("<head>\n")
								.append("<script type=\"text/javascript\" src=\"")
								.append(request.getContextPath())
								.append("/brich-client/src-test/include-all.js\" charset=\""
										+ Constants.DEFAULT_CHARSET
										+ "\"></script>\n")
								.append("<script type=\"text/javascript\" charset=\""
										+ Constants.DEFAULT_CHARSET + "\">\n");
						writer.flush();
					}

					outputFile(out, resource);

					if (isTestWrapper) {
						writer.append("</script>\n").append("</head>\n")
								.append("<body></body>\n").append("</html>\n");
						writer.flush();
					}
					out.flush();
				} finally {
					out.close();
				}
			}
		} finally {
			DoradoContext.dispose(request);
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected String getContentType(String path) {
		int i = path.lastIndexOf('.');
		if (i > 0 && i < path.length() - 1) {
			String suffix = path.substring(i + 1);
			if ("html".equalsIgnoreCase(suffix)) {
				return CONTENT_TYPE_HTML;
			} else if ("js".equalsIgnoreCase(suffix)) {
				return CONTENT_TYPE_JAVASCRIPT;
			} else if ("css".equalsIgnoreCase(suffix)) {
				return CONTENT_TYPE_CSS;
			}
		}
		return CONTENT_TYPE_OCTET_STREAM;
	}

	protected void outputFile(OutputStream out, Resource resource)
			throws IOException {
		InputStream in = resource.getInputStream();
		byte[] buffer = new byte[BUFFER_SIZE];
		int len = in.read(buffer);
		while (len != -1) {
			out.write(buffer, 0, len);
			len = in.read(buffer);
		}
	}

}
