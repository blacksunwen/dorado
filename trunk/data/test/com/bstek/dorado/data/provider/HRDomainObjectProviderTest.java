package com.bstek.dorado.data.provider;

import java.util.List;

import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.model.Department;
import com.bstek.dorado.data.model.Employee;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.type.DataType;

public class HRDomainObjectProviderTest extends ConfigManagerTestSupport {
	@SuppressWarnings("unchecked")
	public void test1() throws Exception {
		DataProvider dataProvider = getDataProviderManager().getDataProvider(
				"testHRDomainObjectProvider1");
		List<Department> depts = (List<Department>) dataProvider.getResult();
		assertNotNull(depts);
		assertEquals(10, depts.size());

		DataType dataType;
		Department d;
		Employee e;

		dataType = EntityUtils.getDataType(depts);
		assertNotNull(dataType);
		assertEquals("List", dataType.getName());

		d = depts.get(0);
		dataType = EntityUtils.getDataType(d);
		assertNotNull(dataType);
		assertEquals("domain.Department", dataType.getName());

		e = d.getEmployees().get(0);
		dataType = EntityUtils.getDataType(e);
		assertNotNull(dataType);
		assertEquals("domain.Employee", dataType.getName());
	}
}
