package com.bstek.dorado.core.resource;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-10
 */
public interface ResourceBundle {
	String getString(String key, Object... args) throws Exception;
}
