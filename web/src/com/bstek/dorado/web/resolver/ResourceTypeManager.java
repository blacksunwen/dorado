package com.bstek.dorado.web.resolver;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-23
 */
public class ResourceTypeManager {
	private Map<String, ResourceType> resourceTypeMap = new HashMap<String, ResourceType>();

	public void registerResourceType(ResourceType resourceType) {
		String type = resourceType.getType();
		Assert.notNull(type);
		resourceTypeMap.put(type.toLowerCase(), resourceType);
	}

	public ResourceType getResourceType(String type) {
		return resourceTypeMap.get(type.toLowerCase());
	}
}