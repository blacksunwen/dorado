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

import java.util.LinkedHashSet;
import java.util.Set;

public class PluginSpringConfigLocations {
	private Set<String> servletContext = new LinkedHashSet<String>();
	private Set<String> context = new LinkedHashSet<String>();
	
	public Set<String> getContext() {
		return context;
	}
	
	public Set<String> getServletContext() {
		return servletContext;
	}
	
}
