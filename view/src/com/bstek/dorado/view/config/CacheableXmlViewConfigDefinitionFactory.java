/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.view.config;

import java.io.IOException;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.bstek.dorado.core.io.DefaultRefreshableResource;
import com.bstek.dorado.core.io.RefreshableResource;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.view.config.definition.ViewConfigDefinition;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-11
 */
public class CacheableXmlViewConfigDefinitionFactory extends
		XmlViewConfigDefinitionFactory {

	private static class DefinitionCacheElement extends Element {
		private static final long serialVersionUID = 2242361888668510593L;

		private RefreshableResource refreshableResource;

		public DefinitionCacheElement(String viewName,
				ViewConfigDefinition defintion) throws IOException {
			super(viewName, defintion);

			Resource resource = defintion.getResource();
			if (resource != null) {
				if (resource instanceof RefreshableResource) {
					refreshableResource = (RefreshableResource) resource;
				} else {
					refreshableResource = new DefaultRefreshableResource(
							resource);
				}
			}
		}

		@Override
		public boolean isExpired() {
			if (super.isExpired()) {
				return true;
			}

			if (refreshableResource != null) {
				return !refreshableResource.isValid();
			} else {
				return false;
			}
		}
	}

	private Ehcache cache;

	public void setCache(Ehcache cache) {
		this.cache = cache;
	}

	@Override
	public ViewConfigDefinition create(String viewName) throws Exception {
		ViewConfigDefinition definition;
		Element element = cache.get(viewName);
		if (element != null) {
			definition = (ViewConfigDefinition) element.getObjectValue();
		} else {
			definition = super.create(viewName);
			element = new DefinitionCacheElement(viewName, definition);
			cache.put(element);
		}
		return definition;
	}

}
