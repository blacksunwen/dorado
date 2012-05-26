package com.bstek.dorado.sample.widget.grid;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.hibernate.HibernateUtils;
import com.bstek.dorado.sample.dao.OrderDao;
import com.bstek.dorado.sample.entity.Order;

@Component
public class ServerSideFilterAndSort {
	@Resource
	private OrderDao orderDao;

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	@DataProvider
	public void getAll(Page<Order> page, Criteria criteria) throws Exception {
		DetachedCriteria detachedCriteria = DetachedCriteria
				.forClass(Order.class);
		if (criteria != null) {
			orderDao.find(page,
					HibernateUtils.createFilter(detachedCriteria, criteria));
		} else {
			orderDao.find(page);
		}
	}
}
