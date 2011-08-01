package com.bstek.dorado.view.registry;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-8
 */
public interface LazyInitailizeComponentTypeRegistryInfo {
	/**
	 * @return
	 */
	public boolean isInitialized();

	/**
	 * @throws Exception
	 */
	public void initialize() throws Exception;
}
