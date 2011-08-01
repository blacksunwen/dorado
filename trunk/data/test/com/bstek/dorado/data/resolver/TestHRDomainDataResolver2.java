package com.bstek.dorado.data.resolver;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.bstek.dorado.data.model.Employee;
import com.bstek.dorado.data.model.TestDataHolder;
import com.bstek.dorado.data.resolver.DataItems;

public class TestHRDomainDataResolver2 {

	@SuppressWarnings("unchecked")
	public void resolve(DataItems dataItems, Object parameter)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		List<Employee> employees = (List<Employee>) dataItems.get("dataSet");
		TestDataHolder.updateDomainTestData2(employees);
	}
}
