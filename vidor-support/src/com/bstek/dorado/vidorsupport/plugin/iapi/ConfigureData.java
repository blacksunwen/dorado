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

import java.util.LinkedHashMap;

public class ConfigureData {
	private LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	
	public static ConfigureData newInstance(){
		return new ConfigureData();
	}
	
	public String[] keys(){
		return map.keySet().toArray(new String[0]);
	}
	public boolean contains(String key) {
		return map.containsKey(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T value(String key) {
		T value = (T) map.get(key);
		return value;
	}
	public ConfigureData value(String key, Object value) {
		map.put(key, value);
		return this;
	}
	
	public String string(String key) {
		String value = value(key);
		return value;
	}
	public ConfigureData string(String key, String value) {
		return value(key, value);
	}
	
	public String[] strings(String key) {
		String[] value = value(key);
		return value;
	}
	public ConfigureData strings(String key, String[] values){
		return value(key, values);
	}
	
	public Boolean bool(String key) {
		Boolean value = value(key);
		return value;
	}
	public ConfigureData bool(String key, Boolean value){
		return value(key, value);
	}
	
	public Boolean[] bools(String key) {
		Boolean[] value = value(key);
		return value;
	}
	public ConfigureData bools(String key, Boolean[] values){
		return value(key, values);
	}
	
	public Integer integer(String key) {
		Integer value = value(key);
		return value;
	}
	public ConfigureData integer(String key, Integer value){
		return value(key, value);
	}
	
	public Integer[] integers(String key) {
		Integer[] value = value(key);
		return value;
	}
	public ConfigureData integers(String key, Integer[] values){
		return value(key, values);
	}

	public ConfigureData data(String key) {
		ConfigureData value = value(key);
		return value;
	}
	public ConfigureData data(String key, ConfigureData data){
		return value(key, data);
	}
	
	public ConfigureData[] datas(String key) {
		ConfigureData[] value = value(key);
		return value;
	}
	public ConfigureData datas(String key, ConfigureData[] datas){
		return value(key, datas);
	}
}
