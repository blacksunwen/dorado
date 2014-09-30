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

import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import com.bstek.dorado.vidorsupport.plugin.iapi.PluginMeta;
import com.bstek.dorado.vidorsupport.plugin.iapi.PluginPackage;
import com.bstek.dorado.vidorsupport.plugin.iapi.PluginPackages;
import com.bstek.dorado.web.resolver.AbstractTextualResolver;

public class PluginManagerResolver extends AbstractTextualResolver {

	protected ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String serviceName = this.getServiceName(request);
		JsonNode result = null;

		if ("packages".equals(serviceName)) {
			ObjectNode objectNode = objectMapper.createObjectNode();
			JsonNode packages = packages();
			if (packages != null) {
				objectNode.put("def", packages);
			}

			JsonNode bootPackages = bootPackages();
			if (bootPackages != null) {
				objectNode.put("boot", bootPackages);
			}

			result = objectNode;
		}

		if (result != null) {
			response.setContentType("text/plain");
			PrintWriter writer = this.getWriter(request, response);
			try {
				String message = result.toString();
				writer.append(message);
			} finally {
				writer.flush();
				writer.close();
			}
		}
	}

	protected JsonNode packages() {
		ObjectNode objectNode = objectMapper.createObjectNode();
		PluginManager pluginManager = PluginManager.getInstance();
		if (pluginManager != null) {
			PluginMeta[] plugins = pluginManager.getPlugins();
			for (PluginMeta plugin : plugins) {
				PluginPackages pluginPackages = plugin.getPluginPackages();
				if (pluginPackages != null) {
					Map<String, PluginPackage> packageDefinitions = pluginPackages
							.getPackageDefinitions();
					for (PluginPackage packageDef : packageDefinitions.values()) {
						ObjectNode packageNode = objectMapper
								.createObjectNode();
						String name = packageDef.getName();
						String pattern = packageDef.getPattern();
						packageNode.put("pattern", pattern);

						String[] fileNames = packageDef.getFileNames();
						ArrayNode fileNameNode = objectMapper.createArrayNode();
						for (String fileName : fileNames) {
							fileNameNode.add(fileName);
						}
						packageNode.put("fileName", fileNameNode);

						Set<String> depends = packageDef.getDepends();
						if (depends != null && depends.size() > 0) {
							ArrayNode dependsNode = objectMapper
									.createArrayNode();
							for (String dep : depends) {
								dependsNode.add(dep);
							}
							packageNode.put("depends", dependsNode);
						}

						objectNode.put(name, packageNode);
					}
				}
			}
		}
		return objectNode;
	}

	protected JsonNode bootPackages() {
		PluginManager pluginManager = PluginManager.getInstance();
		if (pluginManager != null) {
			PluginMeta[] plugins = pluginManager.getPlugins();
			Set<String> boot = new LinkedHashSet<String>();
			for (PluginMeta plugin : plugins) {
				PluginPackages pluginPackages = plugin.getPluginPackages();
				if (pluginPackages != null) {
					Set<String> packages = pluginPackages.getBootPackages();
					if (packages != null && packages.size() > 0) {
						boot.addAll(packages);
					}
				}
			}

			if (boot.size() > 0) {
				ArrayNode node = objectMapper.createArrayNode();
				for (String pkg : boot) {
					node.add(pkg);
				}
				return node;
			}
		}
		return null;
	}

	protected String getServiceName(HttpServletRequest request)
			throws Exception {
		String uri = this.getRelativeRequestURI(request);
		int si = uri.lastIndexOf('/');
		String serviceName = uri.substring(si + 1);
		return serviceName;
	}
}
