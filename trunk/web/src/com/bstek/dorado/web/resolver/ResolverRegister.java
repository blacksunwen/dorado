package com.bstek.dorado.web.resolver;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-13
 */
public class ResolverRegister {
	private String url;
	private Object resolver;
	private int order = 999;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Object getResolver() {
		return resolver;
	}

	public void setResolver(Object resolver) {
		this.resolver = resolver;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
