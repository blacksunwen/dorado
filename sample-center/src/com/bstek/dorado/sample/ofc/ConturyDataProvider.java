package com.bstek.dorado.sample.ofc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;

@Component
public class ConturyDataProvider {

	@DataProvider
	public Collection<Map<String, Number>> getData1996() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double values[] = new double[]{25601.34, 20148.82, 17372.76, 35407.15, 38105.68};
		
		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("value", values[i]);		
			list.add(map);
		}
		
		return list;
	}
	
	@DataProvider
	public Collection<Map<String, Number>> getData1997() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double values[] = new double[]{57401.85, 41941.19 ,45263.37, 117320.16, 114845.27};
		
		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("value", values[i]);		
			list.add(map);
		}
		
		return list;
	}
	
	@DataProvider
	public Collection<Map<String, Number>> getData1998() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double values[] = new double[]{45000.65, 44835.76, 18722.18, 77557.31, 92633.68};
		
		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("value", values[i]);		
			list.add(map);
		}
		
		return list;
	}
}
