package com.bstek.dorado.htmleditor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.dorado.core.Constants;
import com.bstek.dorado.web.resolver.AbstractResolver;

public abstract class AbstractImageUploadResolver extends AbstractResolver{
	private String characterEncoding = Constants.DEFAULT_CHARSET;
	
	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {  
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
		
        String url = execute(multipartRequest, response);
        
        if (url == null) {
        	url = "";
        }
        
		PrintWriter writer = getWriter(request, response);
		try {
			writer.println("<script>parent.reloadHtmlEditorImage('" + url + "')</script>");
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
