package com.bstek.dorado.sample.ofc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;

@Component
public class InvestmentDataProvider {
	@DataProvider
	public Collection<Map<String, Number>> getScatterData1() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double xs[] = new double[]{4.2, 2.8, 6.2, 1, 1.2, 4.4, 8.5, 6.9, 9.9, 0.9, 8.8, 3.2, 1.1, 4.8, 5.8, 3.5, 2.9, 0.8, 8.9, 0.9, 5.3, 1.4, 8.1, 9.8, 8.8, 3.5, 4.9, 6.5, 4.8, 3, 6.2, 1.8, 5.8, 6.9, 3.2, 9, 8.4, 1.9, 0.6, 8.2, 3.8, 8, 2.7, 1.8, 8.9, 1.6, 0.8, 5.3, 3, 3.4, 7.2, 1.6, 8.7, 5.3, 3.3, 0.7, 9.2, 1.7, 1.8, 2, 7.4, 8.8, 5.2, 9.8, 2.5, 1.3, 6.4, 8.8, 1.9, 6.9, 4.9, 9.4, 1.8, 0.4, 3.2};
		double ys[] = new double[]{193.2, 33.6, 24.8, 14, 26.4, 114.4, 323, 289.8, 287.1, 9, 140.8, 150.4, 39.6, 172.8, 278.4, 52.5, 84.1, 18.4, 26.7, 27, 148.4, 22.4, 137.7, 401.8, 114.4, 28, 117.6, 195, 76.8, 48, 192.2, 12.6, 168.2, 179.4, 60.8, 18, 336, 39.9, 20.4, 65.6, 102.6, 40, 64.8, 61.2, 62.3, 3.2, 31.2, 58.3, 54, 44.2, 129.6, 46.4, 400.2, 185.5, 125.4, 25.2, 101.2, 11.9, 14.4, 16, 118.4, 343.2, 130, 196, 112.5, 52, 179.2, 114.4, 70.3, 6.9, 205.8, 413.6, 14.4, 2.4, 121.6};
		
		for (int i = 0, j = xs.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("x", xs[i]);	
			map.put("y", ys[i]);
			list.add(map);
		}
		
		return list;
	}
	
	@DataProvider
	public Collection<Map<String, Number>> getScatterData2() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double xs[] = new double[]{1.4, 1.6, 4.7, 8.9, 3, 2, 8.5, 6.9, 1.3, 7.1, 4.3, 1.4, 5, 9.9, 3.9, 1.3, 5.9, 5.9, 0.7, 4, 9.8, 8.3, 4.9, 3.9, 1.7, 6.3, 4.7, 1.3, 7.6, 4.4, 5.3, 8};
		double ys[] = new double[]{32.2, 27, 230.3, 160.2, 24, 94, 399.5, 289.8, 15.6, 333.7, 98.9, 4.8, 230, 445.5, 70.2, 5.4, 271.4, 177, 15.4, 24, 431.2, 132.8, 161.7, 187.2, 42.5, 233.1, 159.8, 16.9, 235.6, 202.4, 169.6, 144};
		
		for (int i = 0, j = xs.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("x", xs[i]);	
			map.put("y", ys[i]);
			list.add(map);
		}
		
		return list;
	}
}
