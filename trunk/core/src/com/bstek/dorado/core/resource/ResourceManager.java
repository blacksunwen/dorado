package com.bstek.dorado.core.resource;

public interface ResourceManager {

	public void init(String bundleName);

	public abstract ResourceBundle getBundle() throws Exception;

	public abstract String getString(String path, Object... args);

}