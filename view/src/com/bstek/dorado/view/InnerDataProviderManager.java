package com.bstek.dorado.view;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.data.provider.manager.DefaultDataProviderManager;
import com.bstek.dorado.view.config.InnerDataProviderDefinitionManager;
import com.bstek.dorado.view.config.InnerDataResolverDefinitionManager;
import com.bstek.dorado.view.config.InnerDataTypeDefinitionManager;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-21
 */
public class InnerDataProviderManager extends DefaultDataProviderManager {
	private DataProviderManager parent;
	private InnerDataTypeDefinitionManager innerDataTypeDefinitionManager;
	private InnerDataProviderDefinitionManager innerDataProviderDefinitionManager;
	private InnerDataResolverDefinitionManager innerDataResolverDefinitionManager;

	public InnerDataProviderManager(
			DataProviderManager parent,
			InnerDataTypeDefinitionManager innerDataTypeDefinitionManager,
			InnerDataProviderDefinitionManager innerDataProviderDefinitionManager,
			InnerDataResolverDefinitionManager innerDataResolverDefinitionManager) {
		this.setDataProviderDefinitionManager(innerDataProviderDefinitionManager);
		this.innerDataTypeDefinitionManager = innerDataTypeDefinitionManager;
		this.innerDataProviderDefinitionManager = innerDataProviderDefinitionManager;
		this.innerDataResolverDefinitionManager = innerDataResolverDefinitionManager;
		this.parent = parent;
	}

	@Override
	public DataProvider getDataProvider(String name) throws Exception {
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
			DataProvider dataProvider = super.getDataProvider(name);
			if (dataProvider == null && parent != null) {
				dataProvider = parent.getDataProvider(name);
			}
			return dataProvider;
		} finally {
			context.setAttribute("privateDataTypeDefinitionManager", oldDtdm);
			context.setAttribute("privateDataProviderDefinitionManager",
					oldDpdm);
			context.setAttribute("privateDataResolverDefinitionManager",
					oldDrdm);
		}
	}
}
