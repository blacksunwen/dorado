package com.bstek.dorado.jdbc;

import java.util.ArrayList;
import java.util.List;

public class JdbcDataResolverItem {

	private String name;
	
	private String dbElement;
	
	private String referencedKeyProperties;
	
	private String foreignKeyProperties;
	
	private List<JdbcDataResolverItem> items = new ArrayList<JdbcDataResolverItem>();
	
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

	public String getReferencedKeyProperties() {
		return referencedKeyProperties;
	}

	public void setReferencedKeyProperties(String refrencedParentProperties) {
		this.referencedKeyProperties = refrencedParentProperties;
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
