/**
 * 
 */
package com.bstek.dorado.core;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-8-6
 */
public interface ConfigureListener {
	public void beforeConfigureChange(String property, Object newValue);

	public void onConfigureChange(String property);
}
