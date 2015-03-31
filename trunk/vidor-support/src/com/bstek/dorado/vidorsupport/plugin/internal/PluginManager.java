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
package com.bstek.dorado.vidorsupport.plugin.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import com.bstek.dorado.vidorsupport.plugin.iapi.IPluginManager;
import com.bstek.dorado.vidorsupport.plugin.iapi.PluginMeta;
import com.bstek.dorado.vidorsupport.plugin.iapi.PluginRule;

public class PluginManager implements IPluginManager {

	private Map<String, PluginMeta> metas = new LinkedHashMap<String, PluginMeta>();
	
	public PluginManager(PluginMeta[] plugins) {
		super();
		for (PluginMeta plugin: plugins) {
			metas.put(plugin.getName(), plugin);
		}
	}
	
	public PluginManager() {
		super();
	}
	
	private static PluginManager instance;
	static void setInstace(PluginMeta[] plugins) {
		if (plugins == null)
			instance = null;
		else {
			instance = new PluginManager(plugins);
		}
	}
	
	static void setInstace(PluginManager pluginManager) {
		instance = pluginManager;
	}
	
	static void setDefaultInstance() {
		PluginManager pluginManager = new PluginManager();
		try {
			Enumeration<URL> urls = PluginManager.class.getClassLoader().getResources(PluginRule.DEFAULT_PATH);
			List<URL> urlList = new ArrayList<URL>();
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				urlList.add(url);
			}
			
			for (URL url: urlList) {
				PluginMeta plugin = pluginManager.readPlugin(url);
				if (plugin != null) {
					pluginManager.metas.put(plugin.getName(), plugin);
				}
			}
			
			PluginManager.setInstace(pluginManager);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public static PluginManager getInstance() {
		if(instance==null){
			PluginManager.setDefaultInstance();
		}
		return instance;
	}
	
	public PluginMeta[] getPlugins() {
		return metas.values().toArray(new PluginMeta[0]);
	}

	public PluginMeta getPlugin(String name) {
		return metas.get(name);
	}

	private PluginMeta readPlugin(URL url) throws IOException {
		ObjectNode objectNode = this.readObjectNode(url);
		if (objectNode != null) {
			PluginMeta plugin = this.convertPlugin(objectNode);
			return plugin;
		}
		return null;
	}
	
	private ObjectNode readObjectNode(URL url) throws IOException {
		InputStream input = url.openStream();
		if (input != null) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				ObjectNode objectNode = (ObjectNode)objectMapper.readTree(input);
				return objectNode;
			} finally {
				input.close();
			}
		}
		
		return null;
	}
	
	private PluginMeta convertPlugin(ObjectNode objectNode) {
		JsonData data = new JsonData(objectNode);
		PluginConfigure configure = new PluginConfigure(data);
		PluginMeta plugin = PluginMeta.valueOf(configure);
		return plugin;
	}
}
