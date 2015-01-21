package com.bstek.dorado.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2014年12月5日
 */
public class ServletContextListenerProxy extends
		DelegatingServletContextListener {

	private ServletContextListener servletContextListener;

	public void setListener(ServletContextListener servletContextListener) {
		this.servletContextListener = servletContextListener;
	}

	public void contextInitialized(ServletContextEvent sce) {
		servletContextListener.contextInitialized(sce);
	}

	public void contextDestroyed(ServletContextEvent sce) {
		servletContextListener.contextDestroyed(sce);
	}

}
