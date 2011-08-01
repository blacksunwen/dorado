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