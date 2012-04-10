package com.bstek.dorado.data.provider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-6-18
 */
public class Criteria {
	List<Criterion> criterions = new ArrayList<Criterion>();
	List<Order> orders = new ArrayList<Order>();

	public void addCriterion(Criterion criterion) {
		criterions.add(criterion);
	}

	public List<Criterion> getCriterions() {
		return criterions;
	}

	public void addOrder(Order order) {
		orders.add(order);
	}

	public List<Order> getOrders() {
		return orders;
	}
}
