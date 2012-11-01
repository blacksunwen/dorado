/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.web.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;

import com.bstek.dorado.core.io.BaseResourceLoader;
import com.bstek.dorado.core.io.InputStreamResource;
import com.bstek.dorado.core.io.Resource;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-12
 */
public class ServletContextResourceLoader extends BaseResourceLoader {
	private ServletContext servletContext;

	public ServletContextResourceLoader(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public Resource getResource(String resourceLocation) {
		if (resourceLocation.startsWith("/WEB-INF/")) {
			return new InputStreamResource(
					servletContext.getResourceAsStream(resourceLocation));
		} else {
			return super.getResource(resourceLocation);
		}
	}

	@Override
	public Resource[] getResources(String locationPattern) throws IOException {
		throw new UnsupportedOperationException();
	}

}
