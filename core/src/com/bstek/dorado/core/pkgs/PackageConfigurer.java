package com.bstek.dorado.core.pkgs;

import com.bstek.dorado.core.io.ResourceLoader;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-10-16
 */
public interface PackageConfigurer {

	public String[] getPropertiesConfigLocations(ResourceLoader resourceLoader)
			throws Exception;

	public String[] getContextConfigLocations(ResourceLoader resourceLoader)
			throws Exception;

	public String[] getServletContextConfigLocations(
			ResourceLoader resourceLoader) throws Exception;
}
