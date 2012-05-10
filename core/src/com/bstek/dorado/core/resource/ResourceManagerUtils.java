package com.bstek.dorado.core.resource;

import com.bstek.dorado.core.Context;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-10
 */
public class ResourceManagerUtils {
	public static ResourceManager get(String bundleName) {
		try {
			ResourceManager resourceManager = (ResourceManager) Context
					.getCurrent().getServiceBean("ResourceManager");
			resourceManager.init(bundleName);
			return resourceManager;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static ResourceManager get(Class<?> bindingType) {
		return get(bindingType.getName());
	}
}
