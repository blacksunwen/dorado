/**
 * 
 */
package com.bstek.dorado.view.service;

import java.util.List;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-4-29
 */
public class DataPreloadConfig {
	private String property;
	private int recursiveLevel = 0;
	private List<DataPreloadConfig> childPreloadConfigs;

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public int getRecursiveLevel() {
		return recursiveLevel;
	}

	public void setRecursiveLevel(int recursiveLevel) {
		this.recursiveLevel = recursiveLevel;
	}

	public List<DataPreloadConfig> getChildPreloadConfigs() {
		return childPreloadConfigs;
	}

	public void setChildPreloadConfigs(
			List<DataPreloadConfig> childPreloadConfigs) {
		this.childPreloadConfigs = childPreloadConfigs;
	}
}
