package com.bstek.dorado.sample.ofc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;

@Component
public class SampleDataProvider {
	@DataProvider
	public Collection<Map<String, Object>> getPieData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		
		double values[] = new double[]{ 51852, 88168, 73897, 93933, 19289, 79623, 48262, 29162, 96878, 81241, 40652, 37581, 2882, 746, 7155, 12072, 45608, 72570, 44799, 71887, 78170 };
		String labels[] = new String[]{"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V"};
		
		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Object>();
			map.put("value", values[i]);	
			map.put("label", labels[i]);
			list.add(map);
		}
		
		return list;
	}
	
	@DataProvider
	public Collection<Map<String, Number>> getAreaData() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double values[] = new double[]{3242, 3171, 700, 1287, 1856, 1126, 987, 1610, 903, 928};
		
		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("value", values[i]);		
			list.add(map);
		}
		
		return list;
	}
}
