package com.bstek.dorado.vidorsupport.plugin.internal;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.io.ResourceLoader;
import com.bstek.dorado.core.pkgs.AbstractPackageConfigurer;

public class VidorSupportPackageConfigurer extends AbstractPackageConfigurer {

	private boolean isVidorSupportEnabled() {
		String vidorSupportEnabledConfig = Configure.getString(
				"vidorSupport.enabled", "auto");
		boolean vidorSupportEnabled;
		if (StringUtils.isEmpty(vidorSupportEnabledConfig)
				|| "auto".equalsIgnoreCase(vidorSupportEnabledConfig)) {
			vidorSupportEnabled = "debug".equals(Configure
					.getString("core.runMode"));
		} else {
			vidorSupportEnabled = Boolean
					.parseBoolean(vidorSupportEnabledConfig);
		}
		return vidorSupportEnabled;
	}

	public String[] getPropertiesConfigLocations(ResourceLoader resourceLoader)
			throws Exception {
		if (isVidorSupportEnabled()) {
			return new String[] { "classpath:com/bstek/dorado/vidorsupport/configure.properties" };
		}
		return null;
	}

	public String[] getContextConfigLocations(ResourceLoader resourceLoader)
			throws Exception {
		if (isVidorSupportEnabled()) {
			return new String[] { "classpath:com/bstek/dorado/vidorsupport/context.xml" };
		}
		return null;
	}

	public String[] getComponentConfigLocations(ResourceLoader resourceLoader)
			throws Exception {
		return null;
	}

	public String[] getServletContextConfigLocations(
			ResourceLoader resourceLoader) throws Exception {
		if (isVidorSupportEnabled()) {
			return new String[] { "classpath:com/bstek/dorado/vidorsupport/servlet-context.xml" };
		}
		return null;
	}

}
