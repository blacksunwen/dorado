package com.bstek.dorado.web.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public class ProviderInterceptor {

	public Map<String, Object> getReferenceData1(Map<String, Object> parameter) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.putAll(parameter);
		result.put("fromServer", "Hello Reference Reader.");
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getReferenceData2(Page page, Map<String, Object> parameter) {
		page.getPageNo();
		page.setEntityCount(108);

		int start = (page.getPageNo() - 1) * page.getPageSize() + 1;
		int end = start + page.getPageSize() - 1;
		if (end > page.getEntityCount())
			end = page.getEntityCount();

		List<Map<String, Object>> entities = new ArrayList<Map<String, Object>>();
		for (int i = start; i <= end; i++) {
			Map<String, Object> entity = new HashMap<String, Object>();
			entity.put("key", parameter.get("p1") + "-" + i);
			entities.add(entity);
		}
		page.setEntities(entities);
	}

	public List<Map<String, String>> getLookupData1() {
		List<Map<String, String>> entities = new ArrayList<Map<String, String>>();
		for (int i = 1; i <= 100; i++) {
			Map<String, String> entity = new HashMap<String, String>();
			entity.put("key", "key-" + i);
			entity.put("value", "value-" + i);
			entities.add(entity);
		}
		return entities;
	}
}
