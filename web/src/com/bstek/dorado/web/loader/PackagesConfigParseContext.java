package com.bstek.dorado.web.loader;

import com.bstek.dorado.config.ParseContext;

/**
 * 资源包配置的解析上下文。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 24, 2008
 */
public class PackagesConfigParseContext extends ParseContext {
	private PackagesConfig packagesConfig;

	/**
	 * @param packagesConfig
	 *            用于保存资源包配置信息的对象。
	 */
	public PackagesConfigParseContext(PackagesConfig packagesConfig) {
		this.packagesConfig = packagesConfig;
	}

	/**
	 * 返回用于保存资源包配置信息的对象。
	 */
	public PackagesConfig getPackagesConfig() {
		return packagesConfig;
	}
}
