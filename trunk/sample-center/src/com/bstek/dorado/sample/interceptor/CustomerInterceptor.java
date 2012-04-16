package com.bstek.dorado.sample.interceptor;

import java.util.Collection;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.sample.dao.CustomerDao;
import com.bstek.dorado.sample.entity.Customer;

@Component
public class CustomerInterceptor {

	@Resource
	private CustomerDao customerDao;

	@DataProvider
	public Collection<Customer> getAll() {
		return customerDao.getAll();
	}

	@DataProvider
	public Collection<Customer> findCustomersByCompanyName(String namePattern) {
		if (StringUtils.isEmpty(namePattern)) {
			return customerDao.getAll();
		} else {
			return customerDao.find(Restrictions.like("companyName",
					namePattern, MatchMode.ANYWHERE));
		}
	}

	@DataProvider
	public void findCustomersByCompanyName(Page<Customer> page,
			String namePattern) {
		if (StringUtils.isEmpty(namePattern)) {
			customerDao.getAll(page);
		} else {
			customerDao.find(page, Restrictions.like("companyName",
					namePattern, MatchMode.ANYWHERE));
		}
	}
}
