package com.bstek.dorado.view;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.resolver.DataResolver;
import com.bstek.dorado.data.resolver.manager.DataResolverManager;
import com.bstek.dorado.data.resolver.manager.DefaultDataResolverManager;
import com.bstek.dorado.view.config.InnerDataProviderDefinitionManager;
import com.bstek.dorado.view.config.InnerDataResolverDefinitionManager;
import com.bstek.dorado.view.config.InnerDataTypeDefinitionManager;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-21
 */
public class InnerDataResolverManager extends DefaultDataResolverManager {
	private DataResolverManager parent;
	private InnerDataTypeDefinitionManager innerDataTypeDefinitionManager;
	private InnerDataProviderDefinitionManager innerDataProviderDefinitionManager;
	private InnerDataResolverDefinitionManager innerDataResolverDefinitionManager;

	public InnerDataResolverManager(
			DataResolverManager parent,
			InnerDataTypeDefinitionManager innerDataTypeDefinitionManager,
			InnerDataProviderDefinitionManager innerDataProviderDefinitionManager,
			InnerDataResolverDefinitionManager innerDataResolverDefinitionManager) {
		this.setDataResolverDefinitionManager(innerDataResolverDefinitionManager);
		this.innerDataTypeDefinitionManager = innerDataTypeDefinitionManager;
		this.innerDataResolverDefinitionManager = innerDataResolverDefinitionManager;
		this.innerDataResolverDefinitionManager = innerDataResolverDefinitionManager;
		this.parent = parent;
	}

	@Override
	public DataResolver getDataResolver(String name) throws Exception {
		Context context = Context.getCurrent();
		Object oldDtdm = context
				.getAttribute("privateDataTypeDefinitionManager");
		Object oldDpdm = context
				.getAttribute("privateDataProviderDefinitionManager");
		Object oldDrdm = context
				.getAttribute("privateDataResolverDefinitionManager");

		context.setAttribute("privateDataTypeDefinitionManager",
				innerDataTypeDefinitionManager);
		context.setAttribute("privateDataProviderDefinitionManager",
				innerDataProviderDefinitionManager);
		context.setAttribute("privateDataResolverDefinitionManager",
				innerDataResolverDefinitionManager);
		try {
			DataResolver dataResolver = super.getDataResolver(name);
			if (dataResolver == null && parent != null) {
				dataResolver = parent.getDataResolver(name);
			}
			return dataResolver;
		} finally {
			context.setAttribute("privateDataTypeDefinitionManager", oldDtdm);
			context.setAttribute("privateDataProviderDefinitionManager",
					oldDpdm);
			context.setAttribute("privateDataResolverDefinitionManager",
					oldDrdm);
		}
	}
}
