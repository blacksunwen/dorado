package com.bstek.dorado.data.resolver;

import java.util.List;

import com.bstek.dorado.data.model.Department;
import com.bstek.dorado.data.model.TestDataHolder;
import com.bstek.dorado.data.resolver.DataItems;

public class TestHRDomainDataResolver1 {

	@SuppressWarnings("unchecked")
	public void resolve(DataItems dataItems, Object parameter) {
		List<Department> departments = (List<Department>) dataItems
				.get("dataSet");
		TestDataHolder.updateDomainTestData1(departments);
	}
}
