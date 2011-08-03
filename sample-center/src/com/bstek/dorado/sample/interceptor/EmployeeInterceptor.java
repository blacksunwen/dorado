package com.bstek.dorado.sample.interceptor;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.sample.dao.EmployeeDao;
import com.bstek.dorado.sample.entity.Employee;

@Component
public class EmployeeInterceptor {

	@Resource
	private EmployeeDao employeeDao;

	@DataProvider
	public Collection<Employee> getAll() {
		return employeeDao.getAll();
	}

	@DataProvider
	public void getAll(Page<Employee> page) {
		employeeDao.getAll(page);
	}

	@DataProvider
	public Collection<Employee> getEmployeesBySuperior(Long parameter) {
		return employeeDao
				.find("from Employee where reportsTo.id=" + parameter);
	}

	@DataProvider
	public Map<String, String> getTitlesOfCourtesy() {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		mapValue.put("Mr.", "Mister");
		mapValue.put("Mrs.", "Mistress");
		mapValue.put("Ms.", "Miss");
		mapValue.put("Dr.", "Doctor");
		return mapValue;
	}

	@DataResolver
	@Transactional
	public void saveAll(Collection<Employee> employees) {
		employeeDao.persistEntities(employees);
	}
}
