package com.bstek.dorado.jdbc.model.autotable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.jdbc.model.AbstractTable;
import com.bstek.dorado.jdbc.model.AbstractColumn;
import com.bstek.dorado.util.Assert;

@XmlNode(
	parser = "spring:dorado.jdbc.autoTableParser",
	definitionType = "com.bstek.dorado.jdbc.model.autotable.AutoTableDefinition",
	subNodes = {
		@XmlSubNode(
			wrapper = @XmlNodeWrapper(nodeName="FromTables", fixed = true),
			propertyName = "fromTables",
			propertyType = "List<com.bstek.dorado.jdbc.model.autotable.FromTable>"
		),
		@XmlSubNode(
			wrapper = @XmlNodeWrapper(nodeName = "JoinTables"),
			propertyName = "joinTables",
			propertyType = "List<com.bstek.dorado.jdbc.model.autotable.JoinTable>"
		),
		@XmlSubNode(
			wrapper = @XmlNodeWrapper(nodeName = "Columns", fixed = true),
			propertyName = "Jdbc_AutoTableColumns",
			propertyType = "List<com.bstek.dorado.jdbc.model.autotable.AutoTableColumn>"
		),
		@XmlSubNode(
			wrapper = @XmlNodeWrapper(nodeName = "Orders"),
			propertyName = "orders",
			propertyType = "List<com.bstek.dorado.jdbc.model.autotable.Order>"
		)
	}
)
public class AutoTable extends AbstractTable {

	public static final String TYPE = "AutoTable";
	
	private Map<String, FromTable> fromTables = new LinkedHashMap<String, FromTable>();
	
	private List<JoinTable> joinTables = new ArrayList<JoinTable>(5);
	
	private List<Order> orders = new ArrayList<Order>(5);

	private Where where;

	private String mainTableAlias;
	
	public void addFromTable(FromTable fromTable) {
		String tableAlias = fromTable.getTableAlias();
		Assert.notEmpty(tableAlias, "[" + this.getName() + "] tableAlias must not be null");
		Assert.isTrue(!fromTables.containsKey(tableAlias), "[" + this.getName() + "] duplicate fromTable '" + tableAlias + "'");
		
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
		Assert.notNull(fromTable, "[" + getName()+ "] " + "No FromTable named [" + alias + "]");
		return fromTable;
	}

	public List<JoinTable> getJoinTables() {
		return joinTables;
	}

	@XmlSubNode
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
	}
	
	public String getType() {
		return TYPE;
	}

	public FromTable getMainFromTable() {
		if (StringUtils.isEmpty(mainTableAlias)) {
			return null;
		} else {
			return this.getFromTable(mainTableAlias);
		}
	}

	public void setMainTableAlias(String mainTableAlias) {
		this.mainTableAlias = mainTableAlias;
	}
	
	public String getMainTableAlias() {
		return this.mainTableAlias;
	}

	@Override
	public void addColumn(AbstractColumn column) {
		Assert.notNull(column);
		
		if (column instanceof AutoTableColumn) {
			super.addColumn(column);
		} else {
			throw new IllegalArgumentException(getType() + " [" + getName()+ "] " + "Unknown column class [" + column.getClass() + "]");
		}
	}

	@Override
	protected String getDefaultSQLGeneratorServiceName() {
		return "spring:dorado.jdbc.autoTableSqlGenerator";
	}
	
}
