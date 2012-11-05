/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.web.servlet;

import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-7
 */
public class SpringContextLoaderListener extends ContextLoaderListener {
	private static final Log logger = LogFactory
			.getLog(SpringContextLoaderListener.class);

	private DoradoLoader doradoLoader;

	public SpringContextLoaderListener() {
		doradoLoader = DoradoLoader.getInstance();
	}

	@Override
	protected void customizeContext(ServletContext servletContext,
			ConfigurableWebApplicationContext applicationContext) {
		try {
			doradoLoader.preload(servletContext, true);
			List<String> doradoContextLocations = doradoLoader
					.getContextLocations(false);
			String[] realResourcesPath = doradoLoader
					.getRealResourcesPath(doradoContextLocations);
			applicationContext.setConfigLocations(realResourcesPath);
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
}
