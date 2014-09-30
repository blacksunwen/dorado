/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.vidorsupport.plugin.iapi;

import java.util.Arrays;

import com.bstek.dorado.core.bean.BeanFactoryUtils;
import com.bstek.dorado.vidorsupport.plugin.ConfigureParseException;

public class PluginMeta {
	private String name;
	private PluginType type;
	
	private PluginPackages pluginPackages;
	private PluginSpringConfigLocations springConfigLocations;
	
	private String cloudoListenerExpr;
	private ICloudoListener cloudoListener;
	
	public PluginMeta(String name) {
		super();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public PluginType getType() {
		return type;
	}
	public void setType(PluginType type) {
		this.type = type;
	}
	
	public PluginPackages getPluginPackages() {
		return pluginPackages;
	}
	public void setPluginPackages(PluginPackages pluginPackages) {
		this.pluginPackages = pluginPackages;
	}
	
	public PluginSpringConfigLocations getSpringConfigLocations() {
		return springConfigLocations;
	}
	public void setSpringConfigLocations(
			PluginSpringConfigLocations springConfigLocations) {
		this.springConfigLocations = springConfigLocations;
	}
	
	public ICloudoListener getCloudoListener() {
		if (cloudoListener == null) {
			if (cloudoListenerExpr != null && cloudoListenerExpr.length() > 0) {
				try {
					ICloudoListener cloudoListener = (ICloudoListener)BeanFactoryUtils.getBean(cloudoListenerExpr);
					this.setCloudoListener(cloudoListener);
				} catch (Exception e) {
					throw new ConfigureParseException(e);
				}
			}
		}
		
		return cloudoListener;
	}
	public void setCloudoListener(ICloudoListener cloudoListener) {
		this.cloudoListener = cloudoListener;
	}

	// ---------------------------------------- valueOf ----------------------------------------
	
	public static PluginMeta valueOf(IConfigure configure) {
		Builder b = new Builder(configure);
		PluginMeta plugin = b.build();
		return plugin;
	}
	
	private static class Builder {
		PluginMeta plugin;
		IConfigure configure;
		
		Builder(IConfigure configure) {
			String name = configure.getString("name");
			if (name == null || name.length() == 0) throw new ConfigureParseException("'name' must not be empty.");
			PluginMeta plugin = new PluginMeta(name);
			this.plugin = plugin;
			this.configure = configure;
		}
		
		PluginMeta build() {
			this.buildBaseInfo();
			this.buildPackages();
			this.buildSpringConfigLocations();
			this.buildListener();
			return this.plugin;
		}
		
		private void buildBaseInfo() {
			String type = configure.getString("type");
			PluginType pluginType = PluginType.USER_PLUGIN;
			if (type != null && type.length() > 0) {
				pluginType = PluginType.valueOf(type);
			}
			plugin.setType(pluginType);
		}
		
		private  void buildListener() {
			plugin.cloudoListenerExpr = configure.getString("listener");
		}
		
		private void buildPackages() {
			IConfigure packagesData = configure.getConfigure("packages");
			if (packagesData != null) {
				PluginPackages pluginPackages = new PluginPackages();
				IConfigure def = packagesData.getConfigure("def");
				if (def != null) {
					String[] keys = def.keys();
					for (String libName: keys) {
						IConfigure libData = def.getConfigure(libName);
						PluginPackage pluginPackage = new PluginPackage(libName);
						String[] fileNames = libData.getStrings("fileNames");
						if (fileNames != null && fileNames.length > 0) {
							pluginPackage.setFileNames(fileNames);
						}
						String[] depends = libData.getStrings("depends");
						if (depends != null && depends.length > 0) {
							pluginPackage.getDepends().addAll(Arrays.asList(depends));
						}
						pluginPackages.getPackageDefinitions().put(pluginPackage.getName(), pluginPackage);
					}
				}
				String[] bootFileNames = packagesData.getStrings("boot");
				if (bootFileNames != null && bootFileNames.length > 0) {
					pluginPackages.getBootPackages().addAll(Arrays.asList(bootFileNames));
				}

				plugin.setPluginPackages(pluginPackages);
			}
		}
		
		private void buildSpringConfigLocations() {
			IConfigure locationsData = configure.getConfigure("springConfigLocations");
			if (locationsData != null) {
				PluginSpringConfigLocations springConfigLocations = new PluginSpringConfigLocations();
				String[] servletContextLocations = locationsData.getStrings("servletContext");
				if (servletContextLocations != null && servletContextLocations.length > 0) {
					springConfigLocations.getServletContext().addAll(Arrays.asList(servletContextLocations));
				}
				String[] contextLocations = locationsData.getStrings("context");
				if (contextLocations != null && contextLocations.length > 0) {
					springConfigLocations.getContext().addAll(Arrays.asList(contextLocations));
				}
				plugin.setSpringConfigLocations(springConfigLocations);
			}
		}
	}
	
}
