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

public interface IConfigure {

	String[] keys();
	
	String getString(String path);
	void setString(String path, String value);
	
	String[] getStrings(String path);
	void setStrings(String path, String[] value);
	
	Boolean getBoolean(String path);
	void setBoolean(String path, Boolean value);
	
	Boolean[] getBooleans(String path);
	void setBooleans(String path, Boolean[] value);
	
	Integer getInteger(String path);
	void setInteger(String path, Integer value);
	
	Integer[] getIntegers(String path);
	void setIntegers(String path, Integer[] value);
	
	IConfigure getConfigure(String path);
	
	IConfigure[] getConfigures(String path);
}
