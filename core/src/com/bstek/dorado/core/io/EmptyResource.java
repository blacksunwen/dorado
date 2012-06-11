package com.bstek.dorado.core.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-29
 */
public class EmptyResource implements Resource {

	public static final EmptyResource INSTANCE = new EmptyResource();

	private EmptyResource() {
	}

	public String getPath() {
		return null;
	}

	public boolean exists() {
		return false;
	}

	public long getTimestamp() throws IOException {
		return 0;
	}

	public InputStream getInputStream() throws IOException {
		return null;
	}

	public URL getURL() throws IOException {
		return null;
	}

	public File getFile() throws IOException {
		return null;
	}

	public Resource createRelative(String relativePath) throws IOException {
		return null;
	}

	public String getFilename() {
		return null;
	}

	public String getDescription() {
		return null;
	}
}
