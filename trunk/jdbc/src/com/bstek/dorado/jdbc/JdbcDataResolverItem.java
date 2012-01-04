package com.bstek.dorado.jdbc;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;

/**
 * 与UpdateAction的Item对应的Item
 * @author mark
 *
 */
@XmlNode(nodeName="Item")
public class JdbcDataResolverItem {

	private String name;
	
	private String tableName;
	
	private String parentKeyProperties;
	
	private String foreignKeyProperties;
	
	private List<JdbcDataResolverItem> items = new ArrayList<JdbcDataResolverItem>(5);
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentKeyProperties() {
		return parentKeyProperties;
	}

	public void setParentKeyProperties(String refrencedParentProperties) {
		this.parentKeyProperties = refrencedParentProperties;
	}

	public String getForeignKeyProperties() {
		return foreignKeyProperties;
	}

	public void setForeignKeyProperties(String refrencedProperties) {
		this.foreignKeyProperties = refrencedProperties;
	}

	@XmlSubNode
	public List<JdbcDataResolverItem> getItems() {
		return this.items;
	}
}
