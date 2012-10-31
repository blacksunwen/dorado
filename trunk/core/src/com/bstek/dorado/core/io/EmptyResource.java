﻿/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
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
