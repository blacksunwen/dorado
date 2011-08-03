package com.bstek.dorado.sample.ofc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;

@Component
public class TagsDataProvider {
	@DataProvider
	public Collection<Map<String, Number>> getColumnData() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;
		
		double values[] = new double[]{9, 6, 7, 9, 5, 7, 6, 9, 7};
		
		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("value", values[i]);		
			list.add(map);
		}
		
		return list;
	}
	
	@DataProvider
	public Collection<Map<String, Object>> getTagsData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> valueMap = null;
		
		valueMap = new HashMap<String, Object>();
		valueMap.put("x", 0);
		valueMap.put("y", 9);
		list.add(valueMap);
		
		valueMap = new HashMap<String, Object>();
		valueMap.put("x", 1);
		valueMap.put("y", 6);
		valueMap.put("alignY", "below");
		list.add(valueMap);
		
		valueMap = new HashMap<String, Object>();
		valueMap.put("x", 2);
		valueMap.put("y", 7);
		list.add(valueMap);
		
		valueMap = new HashMap<String, Object>();
		valueMap.put("x", 3);
		valueMap.put("y", 9);
		list.add(valueMap);
		
		valueMap = new HashMap<String, Object>();
		valueMap.put("x", 4);
		valueMap.put("y", 5);
		list.add(valueMap);
		
		valueMap = new HashMap<String, Object>();
		valueMap.put("x", 5);
		valueMap.put("y", 7);
		valueMap.put("font", "Arial");
		valueMap.put("bold", true);
		list.add(valueMap);
		
		valueMap = new HashMap<String, Object>();
		valueMap.put("x", 6);
		valueMap.put("y", 6);
		list.add(valueMap);
		
		valueMap = new HashMap<String, Object>();
		valueMap.put("x", 7);
		valueMap.put("y", 9);
		list.add(valueMap);
		
		valueMap = new HashMap<String, Object>();
		valueMap.put("x", 5);
		valueMap.put("y", 13);
		valueMap.put("color", "#0000F0");
		valueMap.put("underline", true);
		valueMap.put("text", "<a href=\"http://moo.com\">Test</a>");
		list.add(valueMap);
		
		valueMap = new HashMap<String, Object>();
		valueMap.put("x", 1);
		valueMap.put("y", 12.2);
		valueMap.put("color", "#0000F0");
		valueMap.put("underline", true);
		valueMap.put("text", "<a href=\"#\">Call JS</a>");
		list.add(valueMap);
		
		valueMap = new HashMap<String, Object>();
		valueMap.put("x", 2);
		valueMap.put("y", 78);
		valueMap.put("axis", "right");
		valueMap.put("padY", 16);
		valueMap.put("fontSize", 20);
		valueMap.put("rotate", 0);
		valueMap.put("color", "#00BB00");
		valueMap.put("font", "Arial Black");
		valueMap.put("underline", true);
		valueMap.put("text", "Click Tag to\nGoogle!!");
		list.add(valueMap);
		
		return list;
	}
}
