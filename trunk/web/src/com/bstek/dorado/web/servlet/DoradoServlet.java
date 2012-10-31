/*
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
package com.bstek.dorado.web.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.web.ConsoleUtils;

/**
 * 用于提供dorado引擎服务的Servlet，同时可用于在Web服务器启动时完成dorado引擎的初始化。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 27, 2008
 */
public class DoradoServlet extends DispatcherServlet {
	private static final long serialVersionUID = 5788753993615625187L;

	private static final String SERVLET_CONTEXT_CONFIG_PROPERTY = "core.servletContextConfigLocation";
	private static Log logger = LogFactory.getLog(DoradoServlet.class);

	@Override
	protected WebApplicationContext createWebApplicationContext(
			WebApplicationContext parent) throws BeansException {
		try {
			String contextConfigLocation = Configure
					.getString(SERVLET_CONTEXT_CONFIG_PROPERTY);
			setContextConfigLocation(contextConfigLocation);

			ConsoleUtils
					.outputLoadingInfo("Loading serlvet context configures...");
		} catch (Exception e) {
			logger.error(e, e);
		}
		return super.createWebApplicationContext(parent);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		try {
			DoradoLoader doradoLoader = DoradoLoader.getInstance();
			doradoLoader.load(getServletContext());
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

}
