package com.bstek.dorado.data.provider;

import java.io.IOException;
import java.util.List;

import com.bstek.dorado.data.model.TestDataHolder;

public class TestHRMapObjectProvider1 {
	@SuppressWarnings({ "rawtypes" })
	public List getResult() throws IOException {
		return TestDataHolder.getMapTestData1();
	}
}
