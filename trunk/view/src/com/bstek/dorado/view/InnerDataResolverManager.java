package com.bstek.dorado.view;

import com.bstek.dorado.data.resolver.DataResolver;
import com.bstek.dorado.data.resolver.manager.DataResolverManager;
import com.bstek.dorado.data.resolver.manager.DefaultDataResolverManager;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-21
 */
public class InnerDataResolverManager extends DefaultDataResolverManager {
	private DataResolverManager parent;

	public InnerDataResolverManager(DataResolverManager parent) {
		this.parent = parent;
	}

	@Override
	public DataResolver getDataResolver(String name) throws Exception {
		DataResolver dataResolver = super.getDataResolver(name);
		if (dataResolver == null && parent != null) {
			dataResolver = parent.getDataResolver(name);
		}
		return dataResolver;
	}
}
