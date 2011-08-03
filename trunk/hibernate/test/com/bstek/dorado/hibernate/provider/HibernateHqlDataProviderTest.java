package com.bstek.dorado.hibernate.provider;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.hibernate.HibernateContextTestCase;
import com.bstek.dorado.hibernate.provider.HqlDataProvider;

public class HibernateHqlDataProviderTest extends HibernateContextTestCase {

	protected DataProviderManager getDataProviderManager() throws Exception {
		Context conetxt = Context.getCurrent();
		DataProviderManager dataProviderManager = (DataProviderManager) conetxt
				.getServiceBean("dataProviderManager");
		return dataProviderManager;
	}

	public void testGetDataProvider() throws Exception {
		HqlDataProvider provider = (HqlDataProvider) getDataProviderManager()
				.getDataProvider("testHqlProvider1");
		assertNotNull(provider);
		String hqlClause = "from Category where id = ?";
		assertEquals(provider.getHql(), hqlClause);

		provider.getResult("343434");
	}
}
