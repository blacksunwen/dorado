package com.bstek.dorado.hibernate.provider;

import java.util.ArrayList;
import java.util.List;

/**
 * Grid自动过滤功能的参数
 * @author mark
 *
 */
public class UserCriteria {

	private List<Parameter> parameters = new ArrayList<Parameter>();
	private List<Order> orders = new ArrayList<Order>();
	
	public List<Parameter> getFilterParameters() {
		return parameters;
	}
	public void addFilterParameter(String field, String expr) {
		Parameter p = new Parameter(field, expr);
		parameters.add(p);
	}
	
	public List<Order> getOrders() {
		return orders;
	}
	public void addOrder(Order order) {
		orders.add(order);
	}
	public void addOrder(String field,  boolean desc) {
		Order order = new Order(field, desc);
		addOrder(order);
	}
	
	public class Parameter {
		private String field;
		private String propertyPath;
		private String expr;
		
		public Parameter(String field) {
			this.field = field;
		}
		public Parameter(String field, String expr) {
			this.field = field;
			this.expr = expr;
		}
		
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		
		public String getPropertyPath() {
			return propertyPath;
		}
		public void setPropertyPath(String propertyPath) {
			this.propertyPath = propertyPath;
		}
		
		public String getExpr() {
			return expr;
		}
		public void setExpr(String value) {
			this.expr = value;
		}
	}
	
	public class Order {
		
		private String field;
		private String propertyPath;
		private boolean desc;
	
		public Order(String field, boolean desc) {
			this.field = field;
			this.desc = desc;
		}
		
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		
		public String getPropertyPath() {
			return propertyPath;
		}
		public void setPropertyPath(String propertyPath) {
			this.propertyPath = propertyPath;
		}

		public boolean isDesc() {
			return desc;
		}
		public void setDesc(boolean desc) {
			this.desc = desc;
		}
	}
}
