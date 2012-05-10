package com.bstek.dorado.core.resource;

import java.util.Enumeration;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-10
 */
public interface ListableResourceBundle extends ResourceBundle {
	Enumeration<String> getKeys();
}
