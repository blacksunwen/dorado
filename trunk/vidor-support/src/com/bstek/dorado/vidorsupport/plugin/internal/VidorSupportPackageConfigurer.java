package com.bstek.dorado.vidorsupport.plugin.internal;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceLoader;
import com.bstek.dorado.core.pkgs.AbstractPackageConfigurer;
import com.bstek.dorado.vidorsupport.plugin.iapi.IPluginManager;
import com.bstek.dorado.vidorsupport.plugin.iapi.PluginMeta;
import com.bstek.dorado.vidorsupport.plugin.iapi.PluginSpringConfigLocations;

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
			String[] vidorContextConfigLocations = new String[] { "classpath:com/bstek/dorado/vidorsupport/context.xml" };
			String[] pluginContextConfigLocations = this
					.getPluginContextConfigLocations(resourceLoader);
			return (String[]) ArrayUtils.addAll(vidorContextConfigLocations,
					pluginContextConfigLocations);
		}
		return null;
	}

	public String[] getServletContextConfigLocations(
			ResourceLoader resourceLoader) throws Exception {
		if (isVidorSupportEnabled()) {
			String[] vidorServletContextConfigLocations = new String[] { "classpath:com/bstek/dorado/vidorsupport/servlet-context.xml" };
			String[] pluginServletContextConfigLocations = this
					.getPluginServletContextConfigLocations(resourceLoader);
			return (String[]) ArrayUtils.addAll(
					vidorServletContextConfigLocations,
					pluginServletContextConfigLocations);
		}
		return null;
	}

	public String[] getComponentConfigLocations(ResourceLoader resourceLoader)
			throws Exception {
		return null;
	}

	private String[] getPluginContextConfigLocations(
			ResourceLoader resourceLoader) throws Exception {
		IPluginManager pluginManager = PluginManager.getInstance();
		Set<String> pluginContextConfigLocations = this
				.getContextConfigLocations(pluginManager);

		Collection<String> containerContextConfigLocations = this
				.getContainerContextConfigLocations(resourceLoader);
		pluginContextConfigLocations.addAll(containerContextConfigLocations);
		return pluginContextConfigLocations.toArray(new String[0]);
	}

	private Set<String> getContextConfigLocations(IPluginManager pluginManager) {
		PluginMeta[] plugins = pluginManager.getPlugins();
		Set<String> locations = new LinkedHashSet<String>();
		for (PluginMeta plugin : plugins) {
			PluginSpringConfigLocations springLocations = plugin
					.getSpringConfigLocations();
			if (springLocations != null) {
				Set<String> l = springLocations.getContext();
				if (l != null && l.size() > 0)
					locations.addAll(l);
			}
		}

		return locations;
	}

	private String[] getPluginServletContextConfigLocations(
			ResourceLoader resourceLoader) throws Exception {
		IPluginManager pluginManager = PluginManager.getInstance();
		Set<String> pluginContextConfigLocations = this
				.getServletContextConfigLocations(pluginManager);
		return pluginContextConfigLocations.toArray(new String[0]);
	}

	private Set<String> getServletContextConfigLocations(
			IPluginManager pluginManager) {
		PluginMeta[] plugins = pluginManager.getPlugins();
		Set<String> locations = new LinkedHashSet<String>();
		for (PluginMeta plugin : plugins) {
			PluginSpringConfigLocations springLocations = plugin
					.getSpringConfigLocations();
			if (springLocations != null) {
				Set<String> l = springLocations.getServletContext();
				if (l != null && l.size() > 0)
					locations.addAll(l);
			}
		}

		return locations;
	}

	private Collection<String> getContainerContextConfigLocations(
			ResourceLoader resourceLoader) throws Exception {
		Properties homeProperties = readProperties(resourceLoader,
				"home:configure.properties");
		String container = homeProperties.getProperty("cloudo.container");
		if (container != null && container.length() > 0) {
			String path = "classpath:com/bstek/dorado/cloudo/" + container
					+ "-service-context.xml";
			return Arrays.asList(path);
		} else {
			return Collections.emptySet();
		}
	}

	private Properties readProperties(ResourceLoader resourceLoader, String path)
			throws Exception {
		Resource resource = resourceLoader.getResource(path);
		Properties properties = new Properties();
		if (resource.exists()) {
			InputStream in = resource.getInputStream();
			properties = new Properties();
			try {
				properties.load(in);
			} finally {
				in.close();
			}
		}
		return properties;
	}

}
