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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.vidorsupport.plugin.NotSingleValueException;
import com.bstek.dorado.vidorsupport.plugin.iapi.ConfigureData;
import com.bstek.dorado.vidorsupport.plugin.iapi.IConfigure;

public class PluginConfigure implements IConfigure {

	protected ConfigureData data;
	
	public PluginConfigure(ConfigureData data) {
		this.data = data;
	}
	
	protected ConfigureData getLastData(String[] keys, boolean create) {
		ConfigureData lastData = null, currentData = data;
		for (String key: keys) {
			if (currentData == null && create) {
				currentData = ConfigureData.newInstance();
				lastData.data(key, currentData);
			} 
			
			if (currentData != null) {
				lastData = currentData;
				currentData = lastData.data(key);
				if (currentData == null && create) {
					currentData = ConfigureData.newInstance();
					lastData.data(key, currentData);
				}
			} else {
				break;
			}
		}
		return currentData;
	}
	
	protected String[] toKeys(String path) {
		String[] keys = StringUtils.split(path, '/');
		List<String> newKeyList = new ArrayList<String>(keys.length);
		for (String key: keys) {
			key = key.trim();
			if (key.length() > 0) {
				newKeyList.add(key);
			}
		}
		
		keys = newKeyList.toArray(new String[newKeyList.size()]);
		return keys;
	}
	
	protected void setValue(String path, Object value) {
		String[] keys = this.toKeys(path);
		if (keys.length == 1) {
			data.value(keys[0], value);
		} else if (keys.length > 1) {
			String[] lastKeys = Arrays.copyOf(keys, keys.length - 1);
			ConfigureData lastData = this.getLastData(lastKeys, true);
			lastData.value(keys[keys.length - 1], value);
		}
	}
	

	public String[] keys() {
		return data.keys();
	}

	public String getString(String path) {
		return this.getSingleValue(path, String.class);
	}

	public void setString(String path, String value) {
		this.setValue(path, value);
	}

	public String[] getStrings(String path) {
		return this.getArrayValues(path, String.class);
	}

	public void setStrings(String path, String[] value) {
		this.setValue(path, value);
	}

	public Boolean getBoolean(String path) {
		return this.getSingleValue(path, Boolean.class);
	}

	public void setBoolean(String path, Boolean value) {
		this.setValue(path, value);
	}

	public Boolean[] getBooleans(String path) {
		return this.getArrayValues(path, Boolean.class);
	}

	public void setBooleans(String path, Boolean[] value) {
		this.setValue(path, value);
	}

	public Integer getInteger(String path) {
		return this.getSingleValue(path, Integer.class);
	}

	public void setInteger(String path, Integer value) {
		this.setValue(path, value);
	}

	public Integer[] getIntegers(String path) {
		return this.getArrayValues(path, Integer.class);
	}

	public void setIntegers(String path, Integer[] value) {
		this.setValue(path, value);
	}

	public IConfigure getConfigure(String path) {
		ConfigureData data = this.getSingleValue(path, ConfigureData.class);
		if (data != null) {
			PluginConfigure configure = new PluginConfigure(data);
			return configure;
		} else {
			return null;
		}
	}

	public IConfigure[] getConfigures(String path) {
		ConfigureData[] datas = this.getArrayValues(path, ConfigureData.class);
		if (datas != null) {
			IConfigure[] configures = new IConfigure[datas.length];
			for (int i=0; i<datas.length; i++) {
				PluginConfigure configure = new PluginConfigure(datas[i]);
				configures[i] = configure;
			}
			return configures;
		} else {
			return null;
		}
	}

	protected <T> T getValue(String path) {
		String[] keys = this.toKeys(path);
		if (keys.length == 1) {
			return data.value(keys[0]);
		} else if (keys.length > 1) {
			String[] lastKeys = Arrays.copyOf(keys, keys.length - 1);
			ConfigureData lastData = this.getLastData(lastKeys, false);
			if (lastData != null) {
				return lastData.value(keys[keys.length - 1]);
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private <T>T getSingleValue(String path, Class<T> componentType) {
		Object array = this.getArrayValues(path, componentType);
		int length = Array.getLength(array);
		if (length == 0) 
			return null;
		if (length == 1) 
			return (T)Array.get(array, 0);
		
		throw new NotSingleValueException(path);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T[] getArrayValues(String path, Class<T> componentType) {
		Object value = this.getValue(path);
		if (value == null) {
			return (T[])Array.newInstance(componentType, 0);
		}
		
		if (value.getClass().isArray()) {
			Object datas = this.getValue(path);
			int length = Array.getLength(datas);
			if (length == 0) {
				return (T[])Array.newInstance(componentType, 0);
			} else {
				Object array = Array.newInstance(componentType, length);
				for (int i=0; i< length; i++) {
					Object d = Array.get(datas, i);
					Array.set(array, i, d);
				}
				return (T[])array;
			}
		} else {
			Object array = Array.newInstance(componentType, 1);
			Array.set(array, 0, value);
			return (T[])array;
		}
	}

}
