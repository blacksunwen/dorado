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
	private String allowedExtensions;
	private String deniedExtensions;
	
	public long getMaxUploadSize() {
		return maxUploadSize;
	}

	public void setMaxUploadSize(long maxUploadSize) {
		this.maxUploadSize = maxUploadSize;
	}

	public String getAllowedExtensions() {
		return allowedExtensions;
	}

	public void setAllowedExtensions(String allowedExtensions) {
		this.allowedExtensions = allowedExtensions;
	}

	public String getDeniedExtensions() {
		return deniedExtensions;
	}

	public void setDeniedExtensions(String deniedExtensions) {
		this.deniedExtensions = deniedExtensions;
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
				writeScript("<script>parent.uploadCallbackForHtmlEditor('Exception:超出了文件容量限制。');</script>", request, response);
				return null;
			}
		} else if (request instanceof MultipartHttpServletRequest){
			multipartRequest = (MultipartHttpServletRequest)request;
		} else {
			return null;
		}
		
		MultipartFile uploadFile = multipartRequest.getFile("filename");
		
		String fileName = new String(uploadFile.getOriginalFilename().getBytes("ISO-8859-1"),"UTF-8"), fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (!isExtensionAllowed(fileSuffix)) {
			writeScript("<script>parent.uploadCallbackForHtmlEditor('Exception:该后缀名的文件不允许上传。');</script>", request, response);
			return null;
		}
		
		String url = null;
		try {
			url = doUploadFile(multipartRequest, uploadFile);
		} catch(Exception e) {
			e.printStackTrace();
			writeScript("<script>parent.uploadCallbackForHtmlEditor('Exception:" + e.getMessage() + "');</script>", request, response);
		}

		if (url == null) {
			url = "";
		}

		writeScript("<script>parent.uploadCallbackForHtmlEditor('" + url + "')</script>", request, response);
		
		return null;
	}
	
	public boolean isExtensionAllowed(String fileSuffix) {
		if (fileSuffix == null)
			return false;
		
		if (allowedExtensions != null) {
			return fileSuffix.toLowerCase().matches(allowedExtensions);
		}
		
		if (deniedExtensions != null) {
			return !fileSuffix.toLowerCase().matches(deniedExtensions);
		}
		
		return true;
	}
	
	protected void writeScript(String script, HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html");
		OutputStream out = response.getOutputStream();
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, characterEncoding));
		try {
			writer.println("<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\n</head>\n<body>\n");
			writer.println(script);
			writer.println("</body>\n</html>");
		} finally {
			writer.flush();
			writer.close();
		}
	}
	
	protected abstract String doUploadFile(MultipartHttpServletRequest request, MultipartFile uploadFile) throws Exception;
}
