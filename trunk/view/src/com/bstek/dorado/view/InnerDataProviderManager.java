package com.bstek.dorado.view;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.data.provider.manager.DefaultDataProviderManager;
import com.bstek.dorado.view.config.definition.ViewConfigDefinition;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-21
 */
public class InnerDataProviderManager extends DefaultDataProviderManager {
	private DataProviderManager parent;
	private ViewConfigDefinition viewConfigDefinition;

	public InnerDataProviderManager(ViewConfigDefinition viewConfigDefinition,
			DataProviderManager parent) {
		this.viewConfigDefinition = viewConfigDefinition;
		this.parent = parent;
	}

	@Override
	public DataProvider getDataProvider(String name) throws Exception {
		Context context = Context.getCurrent();
		context.setAttribute("privateDataTypeDefinitionManager",
				viewConfigDefinition.getDataTypeDefinitionManager());
		context.setAttribute("privateDataProviderDefinitionManager",
				viewConfigDefinition.getDataProviderDefinitionManager());
		context.setAttribute("privateDataResolverDefinitionManager",
				viewConfigDefinition.getDataResolverDefinitionManager());
		try {
			DataProvider dataProvider = super.getDataProvider(name);
			if (dataProvider == null && parent != null) {
				dataProvider = parent.getDataProvider(name);
			}
			return dataProvider;
		} finally {
			context.removeAttribute("privateDataTypeDefinitionManager");
			context.removeAttribute("privateDataProviderDefinitionManager");
			context.removeAttribute("privateDataResolverDefinitionManager");
		}
	}
}
