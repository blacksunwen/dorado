package com.bstek.dorado.data.resolver.manager;

import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.resolver.DataResolver;

public class DataResolverInterceptorTest extends ConfigManagerTestSupport {
	public void testMethod1() throws Exception {
		DataResolver resolver = getDataResolverManager().getDataResolver(
				"resolverWithInterceptor1");
		assertNotNull(resolver);
		assertEquals("Return value of method1", resolver.resolve(null, null));
	}

	public void testMethod2() throws Exception {
		DataResolver resolver = getDataResolverManager().getDataResolver(
				"resolverWithInterceptor2");
		assertNotNull(resolver);

		DataItems dataItems = new DataItems();
		assertSame(dataItems, resolver.resolve(dataItems, null));
	}

	public void testMethod3() throws Exception {
		DataResolver resolver = getDataResolverManager().getDataResolver(
				"resolverWithInterceptor3");
		assertNotNull(resolver);

		DataItems dataItems = new DataItems();
		assertSame(dataItems, resolver.resolve(dataItems, null));
	}

	public void testMethod4() throws Exception {
		DataResolver resolver = getDataResolverManager().getDataResolver(
				"resolverWithInterceptor4");
		assertNotNull(resolver);

		DataItems dataItems = new DataItems();
		int i = 999;
		assertEquals(String.valueOf(i), resolver.resolve(dataItems, i));
	}

	public void testMethod5() throws Exception {
		DataResolver resolver = getDataResolverManager().getDataResolver(
				"resolverWithInterceptor5");
		assertNotNull(resolver);

		DataItems dataItems = new DataItems();
		dataItems.put("value1", "Haha");
		dataItems.put("value2", 345);
		dataItems.put("value3", "hihi");
		dataItems.put("value4", 678.9123f);

		boolean parameter = true;
		assertEquals("method5-Haha345678.9123true",
				resolver.resolve(dataItems, parameter));
	}
}
