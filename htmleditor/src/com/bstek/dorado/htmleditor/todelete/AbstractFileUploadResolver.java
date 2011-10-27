package com.bstek.dorado.htmleditor.todelete;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.dorado.core.Constants;
import com.bstek.dorado.web.resolver.AbstractResolver;

public abstract class AbstractFileUploadResolver extends AbstractResolver {
	private String characterEncoding = Constants.DEFAULT_CHARSET;

	private MultipartResolver multipartResolver;

	public void setMultipartResolver(MultipartResolver multipartResolver) {
		this.multipartResolver = multipartResolver;
	}

	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 如果直接在Spring上下文中配置一个multipartResolver会导致所有含有multipart特性的Request都被自动处理，
		// 这时，如果用户希望自行读取request.getInputStream会得到null，因为multipartResolver已经抢先读取了。
		// 目前BDF的部分使用场景就碰到了这个问题，因此将multipartResolver改为此Bean的内部类。
		MultipartHttpServletRequest multipartRequest = null;
		if (!(request instanceof MultipartHttpServletRequest) && multipartResolver.isMultipart(request)) {
			multipartRequest = multipartResolver.resolveMultipart(request);
		} else if (request instanceof MultipartHttpServletRequest){
			multipartRequest = (MultipartHttpServletRequest)request;
		} else {
			return null;
		}

		String url = execute(multipartRequest, response);

		if (url == null) {
			url = "";
		}

		PrintWriter writer = getWriter(request, response);
		try {
			writer.println("<script>parent.reloadHtmlEditorFile('" + url
					+ "')</script>");
		} finally {
			writer.flush();
			writer.close();
		}
		return null;
	}

	protected PrintWriter getWriter(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		OutputStream out = response.getOutputStream();
		return new PrintWriter(new OutputStreamWriter(out, characterEncoding));
	}

	protected abstract String execute(MultipartHttpServletRequest request,
			HttpServletResponse response) throws Exception;

}
