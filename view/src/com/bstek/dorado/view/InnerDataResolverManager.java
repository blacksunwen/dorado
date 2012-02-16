package com.bstek.dorado.view;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.resolver.DataResolver;
import com.bstek.dorado.data.resolver.manager.DataResolverManager;
import com.bstek.dorado.data.resolver.manager.DefaultDataResolverManager;
import com.bstek.dorado.view.config.definition.ViewConfigDefinition;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-21
 */
public class InnerDataResolverManager extends DefaultDataResolverManager {
	private DataResolverManager parent;
	private ViewConfigDefinition viewConfigDefinition;

	public InnerDataResolverManager(ViewConfigDefinition viewConfigDefinition,
			DataResolverManager parent) {
		this.viewConfigDefinition = viewConfigDefinition;
		this.parent = parent;
	}

	@Override
	public DataResolver getDataResolver(String name) throws Exception {
		Context context = Context.getCurrent();
		context.setAttribute("privateDataTypeDefinitionManager",
				viewConfigDefinition.getDataTypeDefinitionManager());
		context.setAttribute("privateDataProviderDefinitionManager",
				viewConfigDefinition.getDataProviderDefinitionManager());
		context.setAttribute("privateDataResolverDefinitionManager",
				viewConfigDefinition.getDataResolverDefinitionManager());
		try {
			DataResolver dataResolver = super.getDataResolver(name);
			if (dataResolver == null && parent != null) {
				dataResolver = parent.getDataResolver(name);
			}
			return dataResolver;
		} finally {
			context.removeAttribute("privateDataTypeDefinitionManager");
			context.removeAttribute("privateDataProviderDefinitionManager");
			context.removeAttribute("privateDataResolverDefinitionManager");
		}
	}
}
