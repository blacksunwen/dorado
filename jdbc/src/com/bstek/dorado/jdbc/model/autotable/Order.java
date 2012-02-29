package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.jdbc.sql.SqlConstants.NullsDirection;
import com.bstek.dorado.jdbc.sql.SqlConstants.OrderDirection;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
@XmlNode(
	definitionType = "com.bstek.dorado.jdbc.model.autotable.OrderDefinition"
)
public class Order {
	
	private String fromTable;

	private String columnName;
	
	private OrderDirection direction;
	
	private NullsDirection nullsDirection;
	
	private boolean available = true;
	
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	public String getFromTable() {
		return fromTable;
	}
	public void setFromTable(String fromTable) {
		this.fromTable = fromTable;
	}
	
	public void setColumn(String column) {
		this.columnName = column;
	}
	public String getColumn() {
		return this.columnName;
	}

	public OrderDirection getDirection() {
		return direction;
	}
	public void setDirection(OrderDirection direction) {
		this.direction = direction;
	}

	public NullsDirection getNullsDirection() {
		return nullsDirection;
	}
	public void setNullsDirection(NullsDirection nullsDirection) {
		this.nullsDirection = nullsDirection;
	}
}
