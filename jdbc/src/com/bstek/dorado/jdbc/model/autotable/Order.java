package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.jdbc.sql.SqlConstants.NullsModel;
import com.bstek.dorado.jdbc.sql.SqlConstants.OrderModel;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
@XmlNode(
	definitionType = "com.bstek.dorado.jdbc.model.autotable.OrderDefinition"
)
public class Order {
	
	private String tableAlias;

	private String columnName;
	
	private OrderModel orderModel;
	
	private NullsModel nullsModel;
	
	private boolean available = true;
	
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	public String getTableAlias() {
		return tableAlias;
	}

	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}

	public void setColumnName(String column) {
		this.columnName = column;
	}
	
	public String getColumnName() {
		return this.columnName;
	}

	public OrderModel getOrderModel() {
		return orderModel;
	}

	public void setOrderModel(OrderModel model) {
		this.orderModel = model;
	}

	public NullsModel getNullsModel() {
		return nullsModel;
	}

	public void setNullsModel(NullsModel nullsModel) {
		this.nullsModel = nullsModel;
	}
}
