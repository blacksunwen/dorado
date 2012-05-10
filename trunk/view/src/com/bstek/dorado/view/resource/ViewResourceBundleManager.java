/**
 * 
 */
package com.bstek.dorado.view.resource;

import java.util.Locale;

import com.bstek.dorado.core.resource.ResourceBundle;
import com.bstek.dorado.view.config.definition.ViewConfigDefinition;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-5
 */
public interface ViewResourceBundleManager {
	public ResourceBundle getBundle(ViewConfigDefinition viewConfigDefinition,
			Locale locale) throws Exception;
}
