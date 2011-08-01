package com.bstek.dorado.data.provider;

import java.io.IOException;

import com.bstek.dorado.data.model.TestDataHolder;
import com.bstek.dorado.data.provider.Page;

public class TestHRDomainObjectProvider3 {

	public void getResult(Page<?> page) throws IOException {
		TestDataHolder.getDomainTestData3(page);
	}
}
