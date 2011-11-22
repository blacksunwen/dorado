package com.bstek.dorado.jdbc;

import java.util.ArrayList;
import java.util.List;

/**
 * 与UpdateAction的Item对应的Item
 * @author mark
 *
 */
public class JdbcDataResolverItem {

	private String name;
	
	private String dbElement;
	
	private String parentKeyProperties;
	
	private String foreignKeyProperties;
	
	private List<JdbcDataResolverItem> items = new ArrayList<JdbcDataResolverItem>(5);
	
	public String getDbElement() {
		return dbElement;
	}

	public void setDbElement(String dbElement) {
		this.dbElement = dbElement;
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

	public List<JdbcDataResolverItem> getItems() {
		return this.items;
	}
}
