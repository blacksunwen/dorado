package com.bstek.dorado.sample.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.sample.entity.Employee;
import com.bstek.dorado.sample.dao.EmployeeDao;

@Component
@Expose
public class Validators {
	@Resource
	private EmployeeDao employeeDao;

	public String checkAccountName(String parameter)
			throws InterruptedException {
		Thread.sleep(500);

		Employee employee = employeeDao.findUnique(
				"from Employee where firstName=?", parameter);

		if (employee == null) {
			return null;
		} else {
			return "帐户名\"" + parameter + "\"已经被人注册了 。";
		}
	}
}
