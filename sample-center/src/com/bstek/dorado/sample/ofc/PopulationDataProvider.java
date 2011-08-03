package com.bstek.dorado.sample.ofc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;

@Component
public class PopulationDataProvider {
	@DataProvider
	public Collection<Map<String, Number>> getAreaData() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double values[] = new double[]{0.87, 0.87, 0.87, 0.844, 0.834, 0.825};
		
		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("value", values[i]);		
			list.add(map);
		}
		
		return list;
	}
	
	@DataProvider
	public Collection<Map<String, Number>> getLineData() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double values[] = new double[]{1.42, 1.42, 1.41, 1.414, 1.413, 1.414};
		
		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("value", values[i]);		
			list.add(map);
		}
		
		return list;
	}
	
	@DataProvider
	public Collection<Map<String, Number>> getBarData() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double values[] = new double[]{275562673, 278558081, 280562489, 290342551, 290342551, 293027112};
		
		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("value", values[i] / 1000000);		
			list.add(map);
		}
		
		return list;
	}
}
