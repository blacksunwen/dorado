package com.bstek.dorado.sample.ofc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;

@Component
public class RadarDataProvider {
	@DataProvider
	public Collection<Map<String, Number>> getBlueData() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double values[] = new double[]{3,3,3,1.5,1,2,3};
		
		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("value", values[i]);		
			list.add(map);
		}
		
		return list;
	}
	
	@DataProvider
	public Collection<Map<String, Number>> getOrangeData() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double values[] = new double[]{2,2,2,2,2,2,2};
		
		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("value", values[i]);		
			list.add(map);
		}
		
		return list;
	}
}
