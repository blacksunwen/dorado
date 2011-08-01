package com.bstek.dorado.data.provider;

import java.io.IOException;
import java.util.Collection;

import com.bstek.dorado.data.model.Employee;
import com.bstek.dorado.data.model.TestDataHolder;

public class TestHRDomainObjectProvider2 {

	public Collection<Employee> getResult() throws IOException {
		return TestDataHolder.getDomainTestData2();
	}
}
