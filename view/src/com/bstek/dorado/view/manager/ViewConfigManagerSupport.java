package com.bstek.dorado.view.manager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bstek.dorado.util.PathUtils;
import com.bstek.dorado.view.config.ViewConfigDefinitionFactory;
import com.bstek.dorado.view.config.ViewConfigInfo;
import com.bstek.dorado.view.config.definition.ViewConfigDefinition;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.DoradoContextUtils;
import com.bstek.dorado.web.WebConfigure;

/**
 * 默认的视图管理器实现类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 1, 2008
 */
public abstract class ViewConfigManagerSupport implements ViewConfigManager {
	private static final String DEFAULT_PATTERN = "**";
	private static final Object NULL_VIEW_FACTORY = new Object();

	private Map<String, Object> factoryMap = new LinkedHashMap<String, Object>();
	private Map<String, Object> factoryCache = new HashMap<String, Object>();

	public void registerViewConfigFactory(String viewNamePattern,
			Object viewConfigFactory) {
		factoryMap.put(viewNamePattern, viewConfigFactory);
		factoryCache.clear();
	}

	public Object getViewConfigFactory(String viewName) {
		Object viewConfigFactory = factoryCache.get(viewName);
		if (viewConfigFactory == null) {
			String viewNameForMatching = (viewName.charAt(0) == '/') ? viewName
					.substring(1) : viewName;
			Object defaultFactory = factoryMap.get(DEFAULT_PATTERN);

			for (Map.Entry<String, Object> entry : factoryMap.entrySet()) {
				String pattern = entry.getKey();
				if (!DEFAULT_PATTERN.equals(pattern)
						&& PathUtils.match(pattern, viewNameForMatching)) {
					viewConfigFactory = entry.getValue();
					break;
				}
			}
			if (viewConfigFactory == null) {
				viewConfigFactory = defaultFactory;
			}

			factoryCache.put(viewName,
					(viewConfigFactory == null) ? NULL_VIEW_FACTORY
							: viewConfigFactory);
		}

		if (viewConfigFactory == NULL_VIEW_FACTORY) {
			viewConfigFactory = null;
		}
		return viewConfigFactory;
	}

	public ViewConfig getViewConfig(String viewName) throws Exception {
		if ("inherent".equals(WebConfigure.getString("view.skin"))) {
			throw new IllegalArgumentException(
					"\"inherent\" is not a valid dorado skin.");
		}

		DoradoContext context = DoradoContext.getCurrent();
		DoradoContextUtils.pushNewViewContext(context);

		try {
			ViewConfig viewConfig = null;
			Object viewConfigFactory = getViewConfigFactory(viewName);
			if (viewConfigFactory != null) {
				if (viewConfigFactory instanceof ViewConfigFactory) {
					viewConfig = ((ViewConfigFactory) viewConfigFactory)
							.create(viewName);
				} else if (viewConfigFactory instanceof ViewConfigDefinitionFactory) {
					ViewConfigDefinitionFactory vcdf = (ViewConfigDefinitionFactory) viewConfigFactory;
					ViewConfigInfo viewConfigInfo = vcdf
							.getViewConfigInfo(viewName);
					ViewConfigDefinition viewConfigDefinition = vcdf
							.create(viewConfigInfo);
					if (viewConfigDefinition != null) {
						viewConfig = (ViewConfig) viewConfigDefinition
								.create(new ViewCreationContext());
					}
				} else if (viewConfigFactory instanceof ViewConfigDefinition) {
					viewConfig = (ViewConfig) ((ViewConfigDefinition) viewConfigFactory)
							.create(new ViewCreationContext());
				} else {
					viewConfig = (ViewConfig) viewConfigFactory;
					if (viewConfig != null) {
						viewConfig.setName(viewName);
					}
				}
			}

			if (viewConfig != null) {				
				DoradoContextUtils.bindViewContext(context, viewConfig);
			}
			return viewConfig;
		} finally {
			DoradoContextUtils.popViewContext(context);
		}
	}
}
