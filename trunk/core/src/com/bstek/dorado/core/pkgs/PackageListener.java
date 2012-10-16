package com.bstek.dorado.core.pkgs;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-10-16
 */
public interface PackageListener {

	public void beforeLoadPackage(PackageInfo packageInfo) throws Exception;

	public void afterLoadPackage(PackageInfo packageInfo) throws Exception;
}
