package com.bstek.dorado.htmleditor.todelete;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class FlashUploadResolver extends AbstractFlashUploadResolver{

	private String FOLDER_PATH = "editorflash/";
	
	protected String execute(MultipartHttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MultipartFile filename = request.getFile("filename");
		
		String path = request.getSession().getServletContext().getRealPath("/" + FOLDER_PATH);
		
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdir();
		}
		
		if (filename != null) {
			String name = new String(filename.getOriginalFilename().getBytes("ISO-8859-1"),"UTF-8");
			File file = new File(path, name);
			if (file.exists()) {
				String prefix = name.substring(0, name.lastIndexOf("."));
				String suffix = name.substring(name.lastIndexOf("."));
				name = prefix + (new Date()).getTime() + "_" + Math.round(Math.random() * 1000000) + suffix;
				file = new File(path, name);
			}
			filename.transferTo(file);
			
			return ">" + FOLDER_PATH + name;
		}
		
		return null;
	}

}
