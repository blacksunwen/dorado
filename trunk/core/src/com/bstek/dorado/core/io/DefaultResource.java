package com.bstek.dorado.core.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 默认的资源描述对象的实现类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 16, 2007
 */
public class DefaultResource implements Resource {
	private org.springframework.core.io.Resource adaptee;
	private String path;

	public DefaultResource(org.springframework.core.io.Resource adaptee) {
		this.adaptee = adaptee;
		if (adaptee instanceof org.springframework.core.io.ClassPathResource) {
			path = ((org.springframework.core.io.ClassPathResource) adaptee)
					.getPath();
		} else if (adaptee instanceof org.springframework.core.io.FileSystemResource) {
			path = ((org.springframework.core.io.FileSystemResource) adaptee)
					.getPath();
		} else {
			try {
				URL url = adaptee.getURL();
				if (url != null) {
					path = url.getPath();
				}
			} catch (Exception e) {
				// do nothing
			}
		}
	}

	public org.springframework.core.io.Resource getAdaptee() {
		return adaptee;
	}

	public Resource createRelative(String relativePath) throws IOException {
		return new DefaultResource(adaptee.createRelative(relativePath));
	}

	public String getPath() {
		return path;
	}

	public boolean exists() {
		return adaptee.exists();
	}

	public String getDescription() {
		return adaptee.getDescription();
	}

	public File getFile() throws IOException {
		return adaptee.getFile();
	}

	public String getFilename() {
		return adaptee.getFilename();
	}

	public long getTimestamp() throws IOException {
		File file = adaptee.getFile();
		if (file != null && file.exists()) {
			return file.lastModified();
		} else {
			return 0L;
		}
	}

	public InputStream getInputStream() throws IOException {
		return adaptee.getInputStream();
	}

	public URL getURL() throws IOException {
		return adaptee.getURL();
	}

	@Override
	public String toString() {
		return adaptee.toString();
	}

	@Override
	public boolean equals(Object obj) {
		boolean b = (obj == this);
		if (!b) {
			if (obj instanceof DefaultResource) {
				b = adaptee.equals(((DefaultResource) obj).adaptee);
			}
			if (obj instanceof Resource) {
				b = getDescription().equals(((Resource) obj).getDescription());
			}
		}
		return b;
	}

	@Override
	public int hashCode() {
		return adaptee.hashCode();
	}
}
