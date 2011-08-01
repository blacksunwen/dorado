package com.bstek.dorado.data.model;

import java.util.List;

public class Department {
	private String id;
	private String name;
	private List<Department> departments;
	private List<Employee> employees;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		if (this.employees != null) {
			for (Employee employee : this.employees) {
				employee.setDepartment(null);
			}
		}

		this.employees = employees;

		if (employees != null) {
			for (Employee employee : employees) {
				employee.setDepartment(this);
			}
		}
	}

}
