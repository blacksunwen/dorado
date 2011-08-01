package com.bstek.dorado.data.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.beanutils.PropertyUtils;

import com.bstek.dorado.core.Constants;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class TestDataHolder {

	private static List<Department> demainTestData1;
	private static List mapTestData1;
	private static Map<String, Employee> demainTestData2;
	private static Map<String, Employee> demainTestData3;

	private static JSONArray getTestData1() throws IOException {
		InputStreamReader isr = new InputStreamReader(
				TestDataHolder.class
						.getResourceAsStream("/com/bstek/dorado/data/model/test-data1.js"),
				Constants.DEFAULT_CHARSET);
		try {
			BufferedReader reader = new BufferedReader(isr);
			String l;
			StringBuffer sb = new StringBuffer();
			while ((l = reader.readLine()) != null) {
				sb.append(l).append('\n');
			}
			return JSONArray.fromObject(sb.toString());
		} finally {
			isr.close();
		}
	}

	private static JSONArray getTestData2() throws IOException {
		InputStreamReader isr = new InputStreamReader(
				TestDataHolder.class
						.getResourceAsStream("/com/bstek/dorado/data/model/test-data2.js"),
				Constants.DEFAULT_CHARSET);
		try {
			BufferedReader reader = new BufferedReader(isr);
			String l;
			StringBuffer sb = new StringBuffer();
			while ((l = reader.readLine()) != null) {
				sb.append(l).append('\n');
			}
			return JSONArray.fromObject(sb.toString());
		} finally {
			isr.close();
		}
	}

	public static List<Department> getDomainTestData1() throws IOException {
		if (demainTestData1 == null) {
			JSONArray jsonArray = getTestData1();

			Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
			classMap.put("departments", Department.class);
			classMap.put("employees", Employee.class);

			JsonConfig cfg = new JsonConfig();
			cfg.setClassMap(classMap);

			demainTestData1 = new ArrayList<Department>();
			for (Object el : jsonArray) {
				demainTestData1.add((Department) JSONObject.toBean(
						(JSONObject) el, new Department(), cfg));
			}
		}
		return demainTestData1;
	}

	public static List getMapTestData1() throws IOException {
		if (mapTestData1 == null) {
			JSONArray jsonArray = getTestData1();

			Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
			classMap.put("departments", HashMap.class);
			classMap.put("employees", HashMap.class);

			JsonConfig cfg = new JsonConfig();
			cfg.setClassMap(classMap);

			mapTestData1 = new ArrayList();
			for (Object el : jsonArray) {
				mapTestData1.add(JSONObject.toBean((JSONObject) el,
						new HashMap(), cfg));
			}
		}
		return mapTestData1;
	}

	private static int index = 0;

	public static void updateDomainTestData1(List<Department> departments) {
		Department department = departments.get(0);
		department = department.getDepartments().get(1);
		department = department.getDepartments().get(1);

		List<Employee> employees = department.getEmployees();
		Employee employee = employees.get(1);
		employee.setSalary(employee.getSalary() + 1);

		index++;
		employee = new Employee();
		employee.setId("NEW_E" + index);
		employee.setName("Name" + index);
		employees.add(employee);

		demainTestData1 = departments;
	}

	public static Collection<Employee> getDomainTestData2() throws IOException {
		if (demainTestData2 == null) {
			JSONArray jsonArray = getTestData2();

			demainTestData2 = new LinkedHashMap<String, Employee>();
			for (Object el : jsonArray) {
				Employee employee = (Employee) JSONObject.toBean(
						(JSONObject) el, Employee.class);
				demainTestData2.put(employee.getId(), employee);
			}
		}
		return new ArrayList<Employee>(demainTestData2.values());
	}

	public static void updateDomainTestData2(List<Employee> employees)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		for (Iterator<Employee> it = employees.iterator(); it.hasNext();) {
			Employee employee = it.next();
			EntityState state = EntityUtils.getState(employee);
			if (state == EntityState.DELETED) {
				demainTestData2.remove(employee.getId());
				it.remove();
			} else if (state == EntityState.MODIFIED) {
				Employee e = demainTestData2.get(employee.getId());
				employee.setSalary(employee.getSalary() + 1);
				if (e != null) {
					PropertyUtils.copyProperties(e, employee);
				}
				EntityUtils.setState(employee, EntityState.NONE);
			} else if (state == EntityState.NEW) {
				Employee e = new Employee();
				PropertyUtils.copyProperties(e, employee);
				demainTestData2.put(e.getId(), e);
				EntityUtils.setState(employee, EntityState.NONE);
			}
		}
	}

	public static Collection<Employee> getDomainTestData3() throws IOException {
		if (demainTestData3 == null) {
			demainTestData3 = new LinkedHashMap<String, Employee>();
			for (int i = 0; i < 100; i++) {
				Employee employee = new Employee();
				employee.setId("E" + i);
				employee.setName("NAME_" + i);
				employee.setSalary((float) Math.random() * 10000);
				employee.setSex(Math.random() > 0.5);
				demainTestData3.put(employee.getId(), employee);
			}
		}
		return new ArrayList<Employee>(demainTestData3.values());
	}

	public static void getDomainTestData3(Page page) throws IOException {
		List<Employee> employees = (List<Employee>) getDomainTestData3();
		page.setEntities(employees.subList(page.getFirstEntityIndex(),
				page.getLastEntityIndex()));
		page.setEntityCount(employees.size());
	}

}