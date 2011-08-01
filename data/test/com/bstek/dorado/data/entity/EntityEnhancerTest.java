package com.bstek.dorado.data.entity;

import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.data.entity.EntityEnhancer;
import com.bstek.dorado.data.provider.DataProvider;

public class EntityEnhancerTest extends ConfigManagerTestSupport {

	@SuppressWarnings("rawtypes")
	public void testPropertyIntercepting() throws Exception {
		DataProvider dataProvider = getDataProviderManager().getDataProvider(
				"providerMaster");
		List data = (List) dataProvider.getResult();
		Map master = (Map) data.get(0);

		Object result;
		EntityEnhancer.disableGetterInterception();
		try {
			result = master.get("reference1");
			assertNull(result);
			assertTrue(EntityEnhancer.hasGetterResultSkiped());
		} finally {
			EntityEnhancer.enableGetterInterception();
		}

		result = master.get("reference1");
		assertEquals("54321", result);

		EntityEnhancer.disableGetterInterception();
		try {
			result = master.get("reference1");
			assertNull(result);
			assertTrue(EntityEnhancer.hasGetterResultSkiped());
		} finally {
			EntityEnhancer.enableGetterInterception();
		}
	}
}
