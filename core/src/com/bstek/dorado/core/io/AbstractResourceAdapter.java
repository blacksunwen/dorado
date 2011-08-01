package com.bstek.dorado.core.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 抽象的资源描述对象的代理。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Dec 7, 2008
 */
public abstract class AbstractResourceAdapter implements Resource {

	/**
	 * 被代理的资源描述对象。
	 */
	protected Resource adaptee;

	/**
	 * @param adaptee 被代理的资源描述对象。
	 */
	public AbstractResourceAdapter(Resource adaptee) {
		this.adaptee = adaptee;
	}

	public Resource createRelative(String relativePath) throws IOException {
		return adaptee.createRelative(relativePath);
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

	public InputStream getInputStream() throws IOException {
		return adaptee.getInputStream();
	}

	public String getPath() {
		return adaptee.getPath();
	}

	public long getTimestamp() throws IOException {
		return adaptee.getTimestamp();
	}

	public URL getURL() throws IOException {
		return adaptee.getURL();
	}
}
