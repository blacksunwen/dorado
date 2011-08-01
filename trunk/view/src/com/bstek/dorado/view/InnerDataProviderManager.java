package com.bstek.dorado.view;

import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.data.provider.manager.DefaultDataProviderManager;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-21
 */
public class InnerDataProviderManager extends DefaultDataProviderManager {

	private DataProviderManager parent;

	public InnerDataProviderManager(DataProviderManager parent) {
		this.parent = parent;
	}

	@Override
	public DataProvider getDataProvider(String name) throws Exception {
		DataProvider dataProvider = super.getDataProvider(name);
		if (dataProvider == null && parent != null) {
			dataProvider = parent.getDataProvider(name);
		}
		return dataProvider;
	}
}
