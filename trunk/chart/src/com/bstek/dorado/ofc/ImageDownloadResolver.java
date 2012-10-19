package com.bstek.dorado.ofc;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import sun.misc.BASE64Decoder;

import com.bstek.dorado.web.resolver.AbstractResolver;

public class ImageDownloadResolver extends AbstractResolver {

	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String fileData = request.getParameter("fileData");
		String fileName = request.getParameter("fileName");
		if (fileName == null) fileName = "chart.png";
		response.setContentType("image/PNG;charset=UTF-8");
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(fileName.getBytes(), "iso-8859-1"));
		
		this.writeImage(response.getOutputStream(), fileData);

		return null;
	}

	private void writeImage(OutputStream os, String base64Code) throws IOException {
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(base64Code);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}

			os.write(b);

		} catch (Exception e) {
			logger.error(" create img error:", e);
		} finally {
			os.flush();
			os.close();
		}
	}
}
