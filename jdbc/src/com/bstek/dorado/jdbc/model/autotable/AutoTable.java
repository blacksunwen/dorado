package com.bstek.dorado.jdbc.model.autotable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.model.AbstractDbElement;
import com.bstek.dorado.jdbc.model.Column;
import com.bstek.dorado.util.Assert;

public class AutoTable extends AbstractDbElement {

	private Map<String, FromTable> fromTables = new LinkedHashMap<String, FromTable>();
	
	private List<JoinTable> joinTables = new ArrayList<JoinTable>();
	
	private Where where;
	
	private List<Order> orders = new ArrayList<Order>();

	private String mainTableAlias;
	
	public void addFromTable(FromTable fromTable) {
		this.fromTables.put(fromTable.getTableAlias(), fromTable);
	}
	
	public void addJoinTable(JoinTable joinTable) {
		this.joinTables.add(joinTable);
	}

	public List<FromTable> getFromTables() {
		return new ArrayList<FromTable>(fromTables.values());
	}
	
	public FromTable getFromTable(String alias) {
		FromTable fromTable = fromTables.get(alias);
		Assert.notNull(fromTable, getType() + " [" + getName()+ "] " + "No FromTable named [" + alias + "]");
		return fromTable;
	}

	public List<JoinTable> getJoinTables() {
		return joinTables;
	}

	public Where getWhere() {
		return where;
	}

	public void setWhere(Where where) {
		this.where = where;
	}

	public List<Order> getOrders() {
		return orders;
	}
	
	public void addOrder(Order order) {
		this.orders.add(order);
		order.setAutoTable(this);
	}

	@Override
	public Type getType() {
		return Type.AutoTable;
	}

	public FromTable getMainTable() {
		Assert.notEmpty(mainTableAlias, getType() + " [" + getName()+ "] " + "mainTableAlias must not be null.");
		
		return this.getFromTable(mainTableAlias);
	}

	public void setMainTableAlias(String mainTableAlias) {
		this.mainTableAlias = mainTableAlias;
	}
	
	public String getMainTableAlias() {
		return this.mainTableAlias;
	}

	@Override
	public void addColumn(Column column) {
		Assert.notNull(column);
		
		if (column instanceof AutoTableColumn) {
			AutoTableColumn c = (AutoTableColumn)column;
			super.addColumn(column);
			c.setAutoTable(this);
		} else {
			throw new IllegalArgumentException(getType() + " [" + getName()+ "] " + "Unknown column class [" + column.getClass() + "]");
		}
	}

	@Override
	protected String getColumnKey(Column column) {
		AutoTableColumn atc = (AutoTableColumn)column;
		String columnAlias = atc.getColumnAlias();
		if (StringUtils.isNotEmpty(columnAlias)) {
			return columnAlias;
		} else {
			return super.getColumnKey(column);
		}
	}
	
}
