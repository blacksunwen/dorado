package com.bstek.dorado.view.resolver;

import org.springframework.beans.factory.InitializingBean;

import com.bstek.dorado.util.Assert;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-4
 */
public class ClientI18NFileRegister implements InitializingBean {
	private ClientI18NFileRegistry clientI18NFileRegistry;
	private String packageName;
	private String path;
	private boolean replace;

	public void setClientI18NFileRegistry(
			ClientI18NFileRegistry clientI18NFileRegistry) {
		this.clientI18NFileRegistry = clientI18NFileRegistry;
	}

	public ClientI18NFileRegistry getClientI18NFileRegistry() {
		return clientI18NFileRegistry;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setReplace(boolean replace) {
		this.replace = replace;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notEmpty(packageName);
		Assert.notEmpty(path);
		clientI18NFileRegistry.register(packageName, path, replace);
	}

}
