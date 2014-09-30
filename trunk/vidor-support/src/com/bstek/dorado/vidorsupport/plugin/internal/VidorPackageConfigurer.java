package com.bstek.dorado.vidorsupport.plugin.internal;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceLoader;
import com.bstek.dorado.core.pkgs.AbstractPackageConfigurer;
import com.bstek.dorado.vidorsupport.plugin.iapi.IPluginManager;
import com.bstek.dorado.vidorsupport.plugin.iapi.PluginMeta;
import com.bstek.dorado.vidorsupport.plugin.iapi.PluginSpringConfigLocations;

public class VidorPackageConfigurer extends AbstractPackageConfigurer {

	private static final String PROPERTIES_RESOURCE_PATH = "classpath:com/bstek/dorado/cloudo/configure.properties";

	public VidorPackageConfigurer(){
		super();
		PluginManager.setDefaultInstance();
	}
	
	@Override
	public String[] getContextConfigLocations(ResourceLoader resourceLoader)
			throws Exception {
		IPluginManager pluginManager = PluginManager.getInstance();
		Set<String> pluginContextConfigLocations = this.getContextConfigLocations(pluginManager);
		
		Collection<String> containerContextConfigLocations = this.getContainerContextConfigLocations(resourceLoader);
		pluginContextConfigLocations.addAll(containerContextConfigLocations);
		return pluginContextConfigLocations.toArray(new String[0]);
	}
	
	@Override
	public String[] getServletContextConfigLocations(
			ResourceLoader resourceLoader) throws Exception {
		IPluginManager pluginManager = PluginManager.getInstance();
		Set<String> pluginContextConfigLocations = this.getServletContextConfigLocations(pluginManager);
		return pluginContextConfigLocations.toArray(new String[0]);
	}
	
	private Set<String> getContextConfigLocations(IPluginManager pluginManager) {
		PluginMeta[] plugins = pluginManager.getPlugins();
		Set<String> locations = new LinkedHashSet<String>();
		for (PluginMeta plugin: plugins) {
			PluginSpringConfigLocations springLocations = plugin.getSpringConfigLocations();
			if (springLocations != null) {
				Set<String> l = springLocations.getContext();
				if (l != null && l.size() > 0)
					locations.addAll(l);
			}
		}
		
		return locations;
	}
	
	private Set<String> getServletContextConfigLocations(IPluginManager pluginManager) {
		PluginMeta[] plugins = pluginManager.getPlugins();
		Set<String> locations = new LinkedHashSet<String>();
		for (PluginMeta plugin: plugins) {
			PluginSpringConfigLocations springLocations = plugin.getSpringConfigLocations();
			if (springLocations != null) {
				Set<String> l = springLocations.getServletContext();
				if (l != null && l.size() > 0)
					locations.addAll(l);
			}
		}
		
		return locations;
	}
	
	// cloudo.container
	private Collection<String> getContainerContextConfigLocations(ResourceLoader resourceLoader) throws Exception{
		Properties homeProperties = readProperties(resourceLoader, "home:configure.properties");
		Properties properties = readProperties(resourceLoader, PROPERTIES_RESOURCE_PATH);
		properties.putAll(homeProperties);
		
		String container = properties.getProperty("cloudo.container");
		if (container != null && container.length() > 0) {
			String path = "classpath:com/bstek/dorado/cloudo/" + container + "-service-context.xml";
			return Arrays.asList(path);
		} else {
			return Collections.emptySet();
		}
	}
	
	private Properties readProperties(ResourceLoader resourceLoader, String path) throws Exception {
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
