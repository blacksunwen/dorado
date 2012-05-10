/**
 * 
 */
package com.bstek.dorado.data.resource;

import java.util.Locale;

import com.bstek.dorado.config.definition.Definition;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.resource.ResourceBundle;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-5
 */
public interface ModelResourceBundleManager {
	public ResourceBundle getBundle(Resource modelResource, Locale locale)
			throws Exception;

	public ResourceBundle getBundle(Definition definition, Locale locale)
			throws Exception;
}
