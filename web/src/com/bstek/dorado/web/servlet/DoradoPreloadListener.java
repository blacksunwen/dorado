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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-21
 */
public class DoradoPreloadListener implements ServletContextListener {
	private static final Log logger = LogFactory
			.getLog(DoradoPreloadListener.class);

	public void contextInitialized(ServletContextEvent event) {
		DoradoLoader doradoLoader = DoradoLoader.getInstance();
		try {
			ServletContext servletContext = event.getServletContext();
			doradoLoader.preload(servletContext, false);
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	public void contextDestroyed(ServletContextEvent event) {
	}
}