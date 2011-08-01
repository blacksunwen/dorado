package com.bstek.dorado.data.provider;

import java.io.IOException;
import java.util.List;

import com.bstek.dorado.data.model.Department;
import com.bstek.dorado.data.model.TestDataHolder;

public class TestHRDomainObjectProvider1 {

	public List<Department> getResult() throws IOException {
		return TestDataHolder.getDomainTestData1();
	}
}
