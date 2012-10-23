package com.bstek.dorado.core.pkgs;

import com.bstek.dorado.core.io.ResourceLoader;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-10-16
 */
public interface PackageListener {

	public void beforeLoadPackage(PackageInfo packageInfo,
			ResourceLoader resourceLoader) throws Exception;

	public void afterLoadPackage(PackageInfo packageInfo,
			ResourceLoader resourceLoader) throws Exception;
}
