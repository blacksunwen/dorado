package com.bstek.dorado.sample.ofc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;

@Component
public class SitehitsDataProvider {
	@DataProvider
	public Collection<Map<String, Number>> getLineData1() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double values[] = new double[]{36,71,85,92,101,116,164,180,192,262,319,489,633,904,1215,1358,1482,1666,1811,2051,2138,2209,2247,2301};
		
		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("value", values[i]);		
			list.add(map);
		}
		
		return list;
	}
	
	@DataProvider
	public Collection<Map<String, Number>> getLineData2() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double values[] = new double[]{23,40,62,118,130,139,158,233,297,379,503,687,746,857,973,1125,1320,1518,1797,1893,2010,2057,2166,2197};
		
		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("value", values[i]);		
			list.add(map);
		}
		
		return list;
	}
	
	@DataProvider
	public Collection<Map<String, Number>> getLineData3() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double values[] = new double[]{37,45,70,79,168,337,374,431,543,784,1117,1415,2077,2510,3025,3383,3711,4016,4355,4751,5154,5475,5696,5801};
		
		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("value", values[i]);		
			list.add(map);
		}
		
		return list;
	}
	
	@DataProvider
	public Collection<Map<String, Number>> getLineData4() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double values[] = new double[]{54,165,175,190,212,241,308,401,481,851,1250,2415,2886,3252,3673,4026,4470,4813,4961,5086,5284,5391,5657,5847};
		
		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("value", values[i]);		
			list.add(map);
		}
		
		return list;
	}
	
	@DataProvider
	public Collection<Map<String, Number>> getLineData5() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double values[] = new double[]{111,120,128,140,146,157,190,250,399,691,952,1448,1771,2316,2763,3149,3637,4015,4262,4541,4837,5016,5133,5278};
		
		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("value", values[i]);		
			list.add(map);
		}
		
		return list;
	}
	
	@DataProvider
	public Collection<Map<String, Number>> getLineData6() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double values[] = new double[]{115,141,175,189,208,229,252,440,608,889,1334,1637,2056,2600,3070,3451,3918,4140,4296,4519,4716,4881,5092,5249};
		
		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("value", values[i]);		
			list.add(map);
		}
		
		return list;
	}
	
	@DataProvider
	public Collection<Map<String, Number>> getLineData7() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double values[] = new double[]{98,1112,1192,1219,1264,1282,1365,1433,1559,1823,1867,2198,1112,1192,1219,2264,2282,2365,2433,2559,2823,2867,2867,2867,};
		
		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("value", values[i]);		
			list.add(map);
		}
		
		return list;
	}
}
