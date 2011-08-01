package com.bstek.dorado.data.resolver.manager;

import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.data.resolver.DataResolver;
import com.bstek.dorado.data.resolver.TestDataResolver;
import com.bstek.dorado.data.resolver.manager.DataResolverManager;

public class DataResolverManagerTest extends ConfigManagerTestSupport {
	public void testGetDataResolver() throws Exception {
		DataResolverManager dataResolverManager = getDataResolverManager();
		assertNotNull(dataResolverManager);

		DataResolver resolver = dataResolverManager
				.getDataResolver("testResolver1");
		assertNotNull(resolver);

		assertEquals("testResolver1", resolver.getName());
		assertEquals(TestDataResolver.class, resolver.getClass());

		String param = "ABC";
		assertEquals("testResolver1-parameter:" + param,
				resolver.resolve(null, param));
	}
}
