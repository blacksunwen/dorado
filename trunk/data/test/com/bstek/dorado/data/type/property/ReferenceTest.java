package com.bstek.dorado.data.type.property;

import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.data.provider.DataProvider;

public class ReferenceTest extends ConfigManagerTestSupport {

	@SuppressWarnings("rawtypes")
	public void testReference1() throws Exception {
		DataProvider dataProvider = getDataProviderManager().getDataProvider(
				"providerMaster");

		List data = (List) dataProvider.getResult();
		assertEquals(data.size(), 1);

		Map master = (Map) data.get(0);
		assertEquals(master.get("referenceKey"), "key1");
		assertEquals(master.get("reference1"), "54321");
	}
}
