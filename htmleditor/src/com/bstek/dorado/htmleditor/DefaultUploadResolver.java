package com.bstek.dorado.htmleditor;

import java.io.File;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class DefaultUploadResolver extends AbstractUploadResolver {	
	private String saveFolderName = "editorimages";
	
	public String getSaveFolderName() {
		return saveFolderName;
	}

	public void setSaveFolderName(String saveFolderName) {
		this.saveFolderName = saveFolderName;
	}

	protected String doUploadFile(MultipartHttpServletRequest request, MultipartFile uploadFile) throws Exception {
		String path = request.getSession().getServletContext().getRealPath("/" + saveFolderName);
		
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdir();
		}
		
		if (uploadFile != null) {
			String name = new String(uploadFile.getOriginalFilename().getBytes("ISO-8859-1"),"UTF-8");
			File target = new File(path, name);
			if (target.exists()) {
				String prefix = name.substring(0, name.lastIndexOf("."));
				String suffix = name.substring(name.lastIndexOf("."));
				name = prefix + (new Date()).getTime() + "_" + Math.round(Math.random() * 1000000) + suffix;
				target = new File(path, name);
			}
			uploadFile.transferTo(target);
			
			return ">" + saveFolderName + "/" + name;
		}
		
		return null;
	}
}
