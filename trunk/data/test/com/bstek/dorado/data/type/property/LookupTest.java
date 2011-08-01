package com.bstek.dorado.data.type.property;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.Lookup;
import com.bstek.dorado.data.type.property.LookupConstraint;

public class LookupTest extends ConfigManagerTestSupport {

	public void testDef() throws Exception {
		EntityDataType dataType = (EntityDataType) getDataTypeManager()
				.getDataType("test.Lookup");
		Lookup lookupProp = (Lookup) dataType.getPropertyDef("lookup1");
		assertNotNull(lookupProp);

		DataProvider dataProvider = lookupProp.getDataProvider();
		assertNotNull(dataProvider);

		List<LookupConstraint> constraints = lookupProp.getConstraints();
		assertEquals(1, constraints.size());

		LookupConstraint lookupConstraint = constraints.get(0);
		assertEquals("key", lookupConstraint.getLookupKeyProperty());
		assertEquals("lookupKey", lookupConstraint.getKeyProperty());

		lookupProp = (Lookup) dataType.getPropertyDef("lookup2");
		assertNotNull(lookupProp);
		assertEquals("value", lookupProp.getLookupProperty());
		assertEquals(168, lookupProp.getCacheTimeToLiveSeconds());
		assertEquals(158, lookupProp.getCacheTimeToIdleSeconds());
	}

	@SuppressWarnings("rawtypes")
	public void testLookup1() throws Exception {
		DataProvider dataProvider = getDataProviderManager().getDataProvider(
				"providerLookup");
		List list = (List) dataProvider.getResult();

		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map map = (Map) it.next();
			assertEquals("key" + (5 - i), map.get("lookupKey"));
			Map lookupMap = (Map) map.get("lookup1");
			assertNotNull(lookupMap);
			lookupMap.get("value");
			assertEquals("value" + (5 - i), lookupMap.get("value"));
			i++;
		}
	}

	@SuppressWarnings({ "rawtypes" })
	public void testLookup2() throws Exception {
		DataProvider dataProvider = getDataProviderManager().getDataProvider(
				"providerLookup");
		List list = (List) dataProvider.getResult();

		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map map = (Map) it.next();
			assertEquals("key" + (5 - i), map.get("lookupKey"));
			Object lookupResult = map.get("lookup2");
			assertNotNull(lookupResult);
			assertEquals("value" + (5 - i), lookupResult);
			i++;
		}
	}
}
