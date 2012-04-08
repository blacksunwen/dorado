package com.bstek.dorado.hibernate.utils;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.bstek.dorado.data.provider.filter.SimpleFilterCriterion;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;

public final class CriteriaUtils {
	private CriteriaUtils() {
	}

	public static List<Criterion> getCriterions(
			com.bstek.dorado.data.provider.Criteria providerCriteria) {
		if (providerCriteria.getCriterions().isEmpty()) {
			return null;
		}

		List<Criterion> criterions = new ArrayList<Criterion>();
		for (com.bstek.dorado.data.provider.Criterion providerCriterion : providerCriteria
				.getCriterions()) {
			Criterion criterion = null;
			if (providerCriterion instanceof SimpleFilterCriterion) {
				SimpleFilterCriterion c = (SimpleFilterCriterion) providerCriterion;
				String property = c.getProperty();
				Object value = c.getValue();
				if (value instanceof String) {
					criterion = Restrictions.like(property, (String) value,
							MatchMode.ANYWHERE);
				} else {
					criterion = Restrictions.eq(property, value);
				}
			} else if (providerCriterion instanceof SingleValueFilterCriterion) {
				SingleValueFilterCriterion c = (SingleValueFilterCriterion) providerCriterion;
				FilterOperator filterOperator = c.getFilterOperator();
				String property = c.getProperty();
				Object value = c.getValue();

				if (FilterOperator.eq.equals(filterOperator)) {
					criterion = Restrictions.eq(property, value);
				} else if (FilterOperator.gt.equals(filterOperator)) {
					criterion = Restrictions.gt(property, value);
				} else if (FilterOperator.lt.equals(filterOperator)) {
					criterion = Restrictions.lt(property, value);
				} else if (FilterOperator.ge.equals(filterOperator)) {
					criterion = Restrictions.ge(property, value);
				} else if (FilterOperator.le.equals(filterOperator)) {
					criterion = Restrictions.le(property, value);
				} else if (FilterOperator.like.equals(filterOperator)
						|| value instanceof String) {
					criterion = Restrictions.like(property, (String) value,
							MatchMode.ANYWHERE);
				} else {
					criterion = Restrictions.eq(property, value);
				}
			}

			if (criterion != null) {
				criterions.add(criterion);
			}
		}
		return criterions;
	}

	public static List<Order> getOrders(
			com.bstek.dorado.data.provider.Criteria providerCriteria) {
		if (providerCriteria.getOrders().isEmpty()) {
			return null;
		}

		List<Order> orders = new ArrayList<Order>();
		for (com.bstek.dorado.data.provider.Order providerOrder : providerCriteria
				.getOrders()) {
			Order order = null;
			if (providerOrder.isDesc()) {
				order = Order.desc(providerOrder.getProperty());
			} else {
				order = Order.asc(providerOrder.getProperty());
			}

			if (order != null) {
				orders.add(order);
			}
		}
		return orders;
	}

	public static Criteria getCriteria(Session session,
			Class<?> persistentClass,
			com.bstek.dorado.data.provider.Criteria providerCriteria) {
		if (providerCriteria == null) {
			return null;
		}

		Criteria criteria = session.createCriteria(persistentClass);

		List<Criterion> criterions = getCriterions(providerCriteria);
		if (criterions != null) {
			for (Criterion criterion : criterions) {
				criteria.add(criterion);
			}
		}

		List<Order> orders = getOrders(providerCriteria);
		if (orders != null) {
			for (Order order : orders) {
				criteria.addOrder(order);
			}
		}
		return criteria;
	}
}
