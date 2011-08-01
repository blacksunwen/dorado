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
