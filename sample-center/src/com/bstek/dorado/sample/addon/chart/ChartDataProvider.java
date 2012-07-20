package com.bstek.dorado.sample.addon.chart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;

@Component
public class ChartDataProvider {

	@DataProvider
	public Collection<Map<String, Number>> getLineData() {
		double values1[] = new double[] { 36, 71, 85, 92, 101, 116, 164, 180,
				192, 262, 319, 489, 633, 904, 1215, 1358, 1482, 1666, 1811,
				2051, 2138, 2209, 2247, 2301 };
		double values2[] = new double[] { 23, 40, 62, 118, 130, 139, 158, 233,
				297, 379, 503, 687, 746, 857, 973, 1125, 1320, 1518, 1797,
				1893, 2010, 2057, 2166, 2197 };
		double values3[] = new double[] { 37, 45, 70, 79, 168, 337, 374, 431,
				543, 784, 1117, 1415, 2077, 2510, 3025, 3383, 3711, 4016, 4355,
				4751, 5154, 5475, 5696, 5801 };
		double values4[] = new double[] { 54, 165, 175, 190, 212, 241, 308,
				401, 481, 851, 1250, 2415, 2886, 3252, 3673, 4026, 4470, 4813,
				4961, 5086, 5284, 5391, 5657, 5847 };
		double values5[] = new double[] { 111, 120, 128, 140, 146, 157, 190,
				250, 399, 691, 952, 1448, 1771, 2316, 2763, 3149, 3637, 4015,
				4262, 4541, 4837, 5016, 5133, 5278 };
		double values6[] = new double[] { 115, 141, 175, 189, 208, 229, 252,
				440, 608, 889, 1334, 1637, 2056, 2600, 3070, 3451, 3918, 4140,
				4296, 4519, 4716, 4881, 5092, 5249 };
		double values7[] = new double[] { 98, 1112, 1192, 1219, 1264, 1282,
				1365, 1433, 1559, 1823, 1867, 2198, 1112, 1192, 1219, 2264,
				2282, 2365, 2433, 2559, 2823, 2867, 2867, 2867, };

		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		for (int i = 0, j = values1.length; i < j; i++) {
			Map<String, Number> map = new HashMap<String, Number>();
			map.put("value1", values1[i]);
			map.put("value2", values2[i]);
			map.put("value3", values3[i]);
			map.put("value4", values4[i]);
			map.put("value5", values5[i]);
			map.put("value6", values6[i]);
			map.put("value7", values7[i]);
			list.add(map);
		}
		return list;
	}

	@DataProvider
	public Collection<Map<String, Number>> getColumnData() {
		double values2006[] = new double[] { 25601.34, 20148.82, 17372.76,
				35407.15, 38105.68 };
		double values2007[] = new double[] { 57401.85, 41941.19, 45263.37,
				117320.16, 114845.27 };
		double values2008[] = new double[] { 45000.65, 44835.76, 18722.18,
				77557.31, 92633.68 };

		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		for (int i = 0, j = values2006.length; i < j; i++) {
			Map<String, Number> map = new HashMap<String, Number>();
			map.put("value2006", values2006[i]);
			map.put("value2007", values2007[i]);
			map.put("value2008", values2008[i]);
			list.add(map);
		}

		return list;
	}

	@DataProvider
	public Collection<Map<String, Object>> getPieData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;

		double values[] = new double[] { 19289, 23897, 31852, 58168, 73933 };
		String labels[] = new String[] { "B", "C", "D", "E", "F" };

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

		double values[] = new double[] { 3242, 3171, 700, 1287, 1856, 1126,
				987, 1610, 903, 928 };

		for (int i = 0, j = values.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("value", values[i]);
			list.add(map);
		}

		return list;
	}

	@DataProvider
	public Collection<Map<String, Number>> getRadarData() {
		double valuesBlue[] = new double[] { 3, 3, 3, 1.5, 1, 2, 3 };
		double valuesOrange[] = new double[] { 2, 2, 2, 2, 2, 2, 2 };

		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		for (int i = 0, j = valuesBlue.length; i < j; i++) {
			Map<String, Number> map = new HashMap<String, Number>();
			map.put("valueBlue", valuesBlue[i]);
			map.put("valueOrange", valuesOrange[i]);
			list.add(map);
		}
		return list;
	}

	@DataProvider
	public Collection<Map<String, Object>> getStackData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> stackMap = null;
		Map<String, Object> valueMap = null;
		List<Map<String, Object>> values = null;

		double stacks[][] = new double[][] { { 25601, 57401, 45000 },
				{ 20148, 41941, 44835 }, { 17372, 45263, 18722 },
				{ 35407, 117320, 77557 }, { 38105, 114845, 92633 } };

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

	@DataProvider
	public Collection<Map<String, Number>> getDualYAxisData() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();

		double areas[] = new double[] { 0.15, 0.43, 0.87, 0.644, 0.934, 1.125 };
		double lines[] = new double[] { 0.92, 1.16, 1.39, 1.214, 1.03, 1.614 };
		double bars[] = new double[] { 275562673, 278558081, 280562489,
				288342551, 284342541, 293027112 };

		for (int i = 0, j = areas.length; i < j; i++) {
			Map<String, Number> map = new HashMap<String, Number>();
			map.put("area", areas[i]);
			map.put("line", lines[i]);
			map.put("bar", bars[i] / 1000000);
			list.add(map);
		}
		return list;
	}

	@DataProvider
	public Collection<Map<String, Number>> getScatterData1() {
		List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
		Map<String, Number> map = null;

		double xs[] = new double[] { 4.2, 2.8, 6.2, 1, 1.2, 4.4, 8.5, 6.9, 9.9,
				0.9, 8.8, 3.2, 1.1, 4.8, 5.8, 3.5, 2.9, 0.8, 8.9, 0.9, 5.3,
				1.4, 8.1, 9.8, 8.8, 3.5, 4.9, 6.5, 4.8, 3, 6.2, 1.8, 5.8, 6.9,
				3.2, 9, 8.4, 1.9, 0.6, 8.2, 3.8, 8, 2.7, 1.8, 8.9, 1.6, 0.8,
				5.3, 3, 3.4, 7.2, 1.6, 8.7, 5.3, 3.3, 0.7, 9.2, 1.7, 1.8, 2,
				7.4, 8.8, 5.2, 9.8, 2.5, 1.3, 6.4, 8.8, 1.9, 6.9, 4.9, 9.4,
				1.8, 0.4, 3.2 };
		double ys[] = new double[] { 193.2, 33.6, 24.8, 14, 26.4, 114.4, 323,
				289.8, 287.1, 9, 140.8, 150.4, 39.6, 172.8, 278.4, 52.5, 84.1,
				18.4, 26.7, 27, 148.4, 22.4, 137.7, 401.8, 114.4, 28, 117.6,
				195, 76.8, 48, 192.2, 12.6, 168.2, 179.4, 60.8, 18, 336, 39.9,
				20.4, 65.6, 102.6, 40, 64.8, 61.2, 62.3, 3.2, 31.2, 58.3, 54,
				44.2, 129.6, 46.4, 400.2, 185.5, 125.4, 25.2, 101.2, 11.9,
				14.4, 16, 118.4, 343.2, 130, 196, 112.5, 52, 179.2, 114.4,
				70.3, 6.9, 205.8, 413.6, 14.4, 2.4, 121.6 };

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

		double xs[] = new double[] { 1.4, 1.6, 4.7, 8.9, 3, 2, 8.5, 6.9, 1.3,
				7.1, 4.3, 1.4, 5, 9.9, 3.9, 1.3, 5.9, 5.9, 0.7, 4, 9.8, 8.3,
				4.9, 3.9, 1.7, 6.3, 4.7, 1.3, 7.6, 4.4, 5.3, 8 };
		double ys[] = new double[] { 32.2, 27, 230.3, 160.2, 24, 94, 399.5,
				289.8, 15.6, 333.7, 98.9, 4.8, 230, 445.5, 70.2, 5.4, 271.4,
				177, 15.4, 24, 431.2, 132.8, 161.7, 187.2, 42.5, 233.1, 159.8,
				16.9, 235.6, 202.4, 169.6, 144 };

		for (int i = 0, j = xs.length; i < j; i++) {
			map = new HashMap<String, Number>();
			map.put("x", xs[i]);
			map.put("y", ys[i]);
			list.add(map);
		}

		return list;
	}
}
