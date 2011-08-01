package com.bstek.dorado.data.provider;

import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.type.DataType;

public class HRMapObjectProviderTest extends ConfigManagerTestSupport {
	@SuppressWarnings({ "rawtypes" })
	public void test1() throws Exception {
		DataProvider dataProvider = getDataProviderManager().getDataProvider(
				"testHRMapObjectProvider1");
		List depts = (List) dataProvider.getResult();
		assertNotNull(depts);
		assertEquals(10, depts.size());

		DataType dataType;
		Map d, e;

		dataType = EntityUtils.getDataType(depts);
		assertNotNull(dataType);
		assertEquals("[map.Department]", dataType.getName());

		d = (Map) depts.get(0);
		dataType = EntityUtils.getDataType(d);
		assertNotNull(dataType);
		assertEquals("map.Department", dataType.getName());

		e = (Map) ((List) d.get("employees")).get(0);
		dataType = EntityUtils.getDataType(e);
		assertNotNull(dataType);
		assertEquals("map.Employee", dataType.getName());
	}
}
