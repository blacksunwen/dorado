package com.bstek.dorado.web;

import org.springframework.core.io.ResourceLoader;
import org.springframework.web.context.WebApplicationContext;

import com.bstek.dorado.core.io.BaseResourceLoader;

/**
 * 支持从WEB-INF中读取资源的资源装载装载器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 17, 2008
 */
public class WebResourceLoader extends BaseResourceLoader {
	protected WebApplicationContext webApplicationContext;

	public void setWebApplicationContext(
			WebApplicationContext webApplicationContext) {
		this.webApplicationContext = webApplicationContext;
	}

	@Override
	protected ResourceLoader getAdaptee() {
		if (webApplicationContext != null) {
			return webApplicationContext;
		} else {
			return super.getAdaptee();
		}
	}

}
