package com.bstek.dorado.htmleditor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.dorado.core.Constants;
import com.bstek.dorado.web.resolver.AbstractResolver;

public abstract class AbstractUploadResolver extends AbstractResolver{
	private String characterEncoding = Constants.DEFAULT_CHARSET;
	private long maxUploadSize = 10485760;
	private String suffixLimit;
	
	public long getMaxUploadSize() {
		return maxUploadSize;
	}

	public void setMaxUploadSize(long maxUploadSize) {
		this.maxUploadSize = maxUploadSize;
	}

	public String getSuffixLimit() {
		return suffixLimit;
	}

	public void setSuffixLimit(String suffixLimit) {
		this.suffixLimit = suffixLimit;
	}

	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 如果直接在Spring上下文中配置一个multipartResolver会导致所有含有multipart特性的Request都被自动处理，
		// 这时，如果用户希望自行读取request.getInputStream会得到null，因为multipartResolver已经抢先读取了。
		// 目前BDF的部分使用场景就碰到了这个问题，因此将multipartResolver改为此Bean的内部类。
		// 另外，如果配置全局MultipartResolver，在上传文件超过预定义大小的情况下，会导致死循环，应避免是用全局MultipartResolver。
		MultipartHttpServletRequest multipartRequest = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		if (!(request instanceof MultipartHttpServletRequest) && multipartResolver.isMultipart(request)) {
			try {
				multipartResolver.setMaxUploadSize(getMaxUploadSize());
				multipartRequest = multipartResolver.resolveMultipart(request);
			} catch(MaxUploadSizeExceededException e) {
				writeScript("<script>parent.uploadCallbackForHtmlEditor('Exception:MaxUploadSizeExceeded');</script>", request, response);
				return null;
			}
		} else if (request instanceof MultipartHttpServletRequest){
			multipartRequest = (MultipartHttpServletRequest)request;
		} else {
			return null;
		}
		
		MultipartFile uploadFile = multipartRequest.getFile("filename");
		
		if (suffixLimit != null) {
			String fileName = uploadFile.getName(), fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if (!isSuffixAllowed(fileSuffix)) {
				writeScript("<script>parent.uploadCallbackForHtmlEditor('Exception:FileSuffixNotAllowed');</script>", request, response);
				return null;
			}
		}
		
		String url = doUploadFile(multipartRequest, uploadFile);

		if (url == null) {
			url = "";
		}

		writeScript("<script>parent.uploadCallbackForHtmlEditor('" + url + "')</script>", request, response);
		
		return null;
	}
	
	public boolean isSuffixAllowed(String fileSuffix) {
		if (fileSuffix == null)
			return false;
		String suffixs[] = suffixLimit.split(",");
		for (int i = 0, j = suffixs.length; i < j; i++) {
			String suffix = suffixs[i];
			if (suffix.trim().toLowerCase().equals(fileSuffix.trim().toLowerCase())) 
				return true;
		}
		
		return false;
	}
	
	protected void writeScript(String script, HttpServletRequest request, HttpServletResponse response) throws IOException{
		OutputStream out = response.getOutputStream();
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, characterEncoding));
		try {
			writer.println(script);
		} finally {
			writer.flush();
			writer.close();
		}
	}
	
	protected abstract String doUploadFile(MultipartHttpServletRequest request, MultipartFile uploadFile) throws Exception;
}
