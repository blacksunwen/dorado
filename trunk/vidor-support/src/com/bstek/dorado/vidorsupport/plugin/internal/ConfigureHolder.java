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

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.vidorsupport.plugin.ConfigureParseException;
import com.bstek.dorado.vidorsupport.plugin.iapi.ConfigureData;
import com.bstek.dorado.vidorsupport.plugin.iapi.IConfigure;
import com.bstek.dorado.vidorsupport.plugin.iapi.IConfigureHolder;

public class ConfigureHolder implements IConfigureHolder {

	private IConfigure configure;

	@Override
	public IConfigure getConfigure() {
		if (configure == null) {
			Resource resource = getDefaultResource();
			if (resource != null && resource.exists()) {
				this.setResource(resource);
			} else {
				this.configure = newConfigure();
			}
		}
		return configure;
	}

	public void setResource(Resource resource) {
		this.configure = this.createRootConfigure(resource);
	}

	public IConfigure newConfigure(){
		ConfigureData data = ConfigureData.newInstance();
		return new PluginConfigure(data);
	}
	
	protected Resource getDefaultResource() {
		String location = Configure.getString("dorado.vidor.configure.resource");
		if (location != null && location.length() > 0) {
			Context context = Context.getCurrent();
			Resource resource = context.getResource(location);
			return resource;
		}
		return null;
	}

	protected IConfigure createRootConfigure(Resource resource) {
		ConfigureData data = this.createConfigureData(resource);
		return new PluginConfigure(data);
	}

	protected ConfigureData createConfigureData(Resource resource) {
		InputStream in = null;
		try {
			in = resource.getInputStream();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(in);
			JsonData data = new JsonData(jsonNode);
			return data;
		} catch (IOException e) {
			throw new ConfigureParseException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}

}
