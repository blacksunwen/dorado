package com.bstek.dorado.sample.interceptor;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.sample.dao.OrderDao;
import com.bstek.dorado.sample.entity.Order;

@Component
public class OrderInterceptor {

	@Resource
	private OrderDao orderDao;

	@DataProvider
	public void getAll(Page<Order> page) {
		orderDao.find(page, "from Order");
	}

	@DataProvider
	public void getOrdersByEmployeeId(Page<Order> page, long employeeId) {
		orderDao.find(page, "from Order where employeeId=" + employeeId);
	}

	@DataProvider
	public void query(Page<Order> page, Map<String, Object> parameter) {
		Criteria criteria = orderDao.createCriteria();
		if (parameter != null) {
			if (parameter.get("orderDate1") != null) {
				criteria.add(Restrictions.ge("orderDate",
						parameter.get("orderDate1")));
			}
			if (parameter.get("orderDate2") != null) {
				criteria.add(Restrictions.le("orderDate",
						parameter.get("orderDate2")));
			}
			if (parameter.get("customerId") != null) {
				criteria.add(Restrictions.eq("customer.id",
						parameter.get("customerId")));
			}
			if (parameter.get("employeeId") != null) {
				criteria.add(Restrictions.eq("employee.id",
						parameter.get("employeeId")));
			}
		}
		orderDao.find(page, criteria);
	}

	@DataResolver
	@Transactional
	public void saveAll(Collection<Order> orders) {
		orderDao.persistEntities(orders);
	}
}
