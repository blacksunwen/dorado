package com.bstek.dorado.view.config.attachment;

import java.io.IOException;

import net.sf.ehcache.Element;

import com.bstek.dorado.core.io.RefreshableResource;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-29
 */
public class ResourceCacheElement extends Element {
	private static final long serialVersionUID = -4363610522391395305L;

	public ResourceCacheElement(RefreshableResource resource, Object value)
			throws IOException {
		super(resource, value);
	}

	@Override
	public boolean isExpired() {
		if (super.isExpired()) {
			return true;
		}

		RefreshableResource resource = (RefreshableResource) getObjectKey();
		if (resource != null) {
			return !resource.isValid();
		} else {
			return false;
		}
	}
}