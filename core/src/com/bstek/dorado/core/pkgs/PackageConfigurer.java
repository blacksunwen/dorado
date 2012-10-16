package com.bstek.dorado.core.pkgs;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-10-16
 */
public interface PackageConfigurer {

	public String[] getPropertiesConfigLocations() throws Exception;

	public String[] getContextConfigLocations() throws Exception;

	public String[] getServletContextConfigLocations() throws Exception;
}
