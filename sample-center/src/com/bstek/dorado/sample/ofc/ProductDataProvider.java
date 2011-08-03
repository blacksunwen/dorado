package com.bstek.dorado.sample.ofc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;

@Component
public class ProductDataProvider {
	@DataProvider
	public Collection<Map<String, Object>> getStackData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> stackMap = null;
		Map<String, Object> valueMap = null;
		List<Map<String, Object>> values = null;
		
		double stacks[][] = new double[][]{
			{25601, 57401, 45000}, {20148, 41941, 44835}, {17372, 45263, 18722}, {35407, 117320, 77557}, {38105, 114845, 92633}
		};
		
		for (int i = 0, k = stacks.length; i < k; i++) {
			stackMap = new HashMap<String, Object>();
			values = new ArrayList<Map<String, Object>>();
			double stack[] = stacks[i];
			for (int j = 0, l = stack.length; j < l; j++) {
				valueMap = new HashMap<String, Object>();
				valueMap.put("value", stack[j]);
				values.add(valueMap);
			}
			stackMap.put("values", values);		
			list.add(stackMap);
		}
		
		return list;
	}
}
