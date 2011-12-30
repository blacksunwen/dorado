package com.bstek.dorado.jdbc.model.autotable;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.jdbc.sql.SqlConstants.NullsModel;
import com.bstek.dorado.jdbc.sql.SqlConstants.OrderModel;
import com.bstek.dorado.util.Assert;

@XmlNode(
	definitionType = "com.bstek.dorado.jdbc.model.autotable.OrderDefinition"
)
public class Order {
	
	private String tableAlias;

	private String columnName;
	
	private OrderModel orderModel;
	
	private NullsModel nullsModel;
	
	private AutoTable autoTable;
	
	private boolean available = true;
	
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	public AutoTableColumn getSelfColumn() {
		Assert.notNull(autoTable);
		Assert.notEmpty(columnName);
		
		AutoTableColumn column = (AutoTableColumn)autoTable.getColumn(columnName);
		return column;
	}

	public FromTable getFromTable() {
		Assert.notNull(autoTable);
		if (StringUtils.isEmpty(tableAlias)) {
			return null;
		} else {
			return autoTable.getFromTable(tableAlias);
		}
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

	public AutoTable getAutoTable() {
		return autoTable;
	}

	public void setAutoTable(AutoTable autoTable) {
		this.autoTable = autoTable;
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
