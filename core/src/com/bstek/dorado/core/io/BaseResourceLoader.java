package com.bstek.dorado.core.io;

import org.springframework.core.io.ResourceLoader;

/**
 * 资源装载类的基本实现。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 9, 2007
 */
public class BaseResourceLoader extends SpringResourceLoaderAdapter {
	private org.springframework.core.io.ResourceLoader springResourceLoader;

	@Override
	protected ResourceLoader getAdaptee() {
		if (springResourceLoader == null) {
			springResourceLoader = new org.springframework.core.io.DefaultResourceLoader();
		}
		return springResourceLoader;
	}

	@Override
	protected String transformLocation(String location) {
		location = LocationTransformerHolder.transformLocation(location);
		return super.transformLocation(location);
	}
}
