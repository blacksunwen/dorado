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

	private static class ViewConfigCacheElement extends Element {
		private static final long serialVersionUID = 2242361888668510593L;

		private RefreshableResource refreshableResource;

		public ViewConfigCacheElement(String viewName,
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
			element = new ViewConfigCacheElement(viewName, definition);
			cache.put(element);
		}
		return definition;
	}

}
