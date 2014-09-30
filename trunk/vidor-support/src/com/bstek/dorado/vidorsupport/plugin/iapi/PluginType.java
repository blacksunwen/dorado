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

public class PluginType {
	public static final PluginType CLOUDO_INTERNAL = new CloudoInternal();
	public static final PluginType USER_PLUGIN = new UserPlugin();
	public static final PluginType DORADO_ADDON = new DoradoAddon();
	
	private static final PluginType[] TYPES = new PluginType[]{
		USER_PLUGIN, DORADO_ADDON, CLOUDO_INTERNAL
	};
	
	public static PluginType valueOf(String text) {
		for (PluginType type: TYPES) {
			if (type.getText().equals(text))
				return type;
		}
		
		throw new RuntimeException("unknown value '" + text + "'");
	}
	
	protected String text;
	
	private String getText() {
		return text;
	}
	
	private static class CloudoInternal extends PluginType {
		CloudoInternal() {
			super();
			this.text = "cloudo-internal";
		}
	}

	private static class DoradoAddon extends PluginType {
		DoradoAddon() {
			super();
			this.text = "dorado-addon";
		}
	}
	
	private static class UserPlugin extends PluginType {
		UserPlugin() {
			super();
			this.text = "user-plugin";
		}
	}
}
