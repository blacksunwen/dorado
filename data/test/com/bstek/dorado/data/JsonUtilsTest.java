package com.bstek.dorado.data;

import java.util.Collection;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.JsonUtils;
import com.bstek.dorado.data.model.Department;
import com.bstek.dorado.data.model.Employee;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;

public class JsonUtilsTest extends DataContextTestCase {

	protected DataTypeManager getDataTypeManager() throws Exception {
		Context conetxt = Context.getCurrent();
		DataTypeManager dataTypeManager = (DataTypeManager) conetxt
				.getServiceBean("dataTypeManager");
		return dataTypeManager;
	}

	public void test1() throws Exception {
		DataTypeManager dataTypeManager = getDataTypeManager();
		EntityDataType employeeDataType = (EntityDataType) dataTypeManager
				.getDataType("domain.Employee");

		JSONObject jsonDepartment = new JSONObject();
		jsonDepartment.put("id", "D11");
		jsonDepartment.put("name", "PRODUCT");

		JSONObject jsonEmployee = new JSONObject();
		jsonEmployee.put("id", "0001");
		jsonEmployee.put("name", "Benny");
		jsonEmployee.put("salary", "88888");
		jsonEmployee.put("department", jsonDepartment);

		Employee employee = (Employee) JsonUtils.toJavaObject(jsonEmployee,
				employeeDataType);
		assertNotNull(employee);
		assertEquals("0001", employee.getId());
		assertEquals("Benny", employee.getName());
		assertEquals(88888F, employee.getSalary());

		Department department = employee.getDepartment();
		assertNotNull(department);
		assertEquals("D11", department.getId());
		assertEquals("PRODUCT", department.getName());
	}

	@SuppressWarnings("unchecked")
	public void test2() throws Exception {
		DataTypeManager dataTypeManager = getDataTypeManager();
		DataType employeesDataType = dataTypeManager
				.getDataType("[map.Employee]");

		JSONObject jsonEmployee;
		JSONArray jsonArray = new JSONArray();

		jsonEmployee = new JSONObject();
		jsonEmployee.put("id", "0001");
		jsonEmployee.put("name", "Tom");
		jsonArray.add(jsonEmployee);

		jsonEmployee = new JSONObject();
		jsonEmployee.put("id", "0002");
		jsonEmployee.put("name", "John");
		jsonArray.add(jsonEmployee);

		jsonEmployee = new JSONObject();
		jsonEmployee.put("id", "0003");
		jsonEmployee.put("name", "Mike");
		jsonArray.add(jsonEmployee);

		Collection<Employee> employees = (Collection<Employee>) JsonUtils
				.toJavaObject(jsonArray, employeesDataType);
		assertNotNull(employees);
		assertEquals(3, employees.size());
	}
}
