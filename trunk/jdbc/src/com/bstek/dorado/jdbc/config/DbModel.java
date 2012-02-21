package com.bstek.dorado.jdbc.config;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.core.io.Resource;

@XmlNode(
	parser = "spring:dorado.jdbc.dbmParser",
	subNodes = {
		@XmlSubNode(propertyType="List<com.bstek.dorado.jdbc.model.table.Table>"),
		@XmlSubNode(propertyType="List<com.bstek.dorado.jdbc.model.sqltable.SqlTable>"),
		@XmlSubNode(propertyType="List<com.bstek.dorado.jdbc.model.autotable.AutoTable>"),
		@XmlSubNode(propertyType="List<com.bstek.dorado.jdbc.model.storedprogram.StoredProgram>")
	}
)
public class DbModel {

	private String jdbcEnviroment;

	private Resource resource;
	
	private Map<String, DbElementDefinition>  definitionMap = new LinkedHashMap<String, DbElementDefinition>();
	
	@XmlProperty(attributeOnly = true)
	@IdeProperty(highlight=1, editor="jdbc:list-envs")
	public String getJdbcEnviroment() {
		return jdbcEnviroment;
	}

	public void setJdbcEnviroment(String jdbcEnviroment) {
		this.jdbcEnviroment = jdbcEnviroment;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public void add(DbElementDefinition def) {
		definitionMap.put(def.getName(), def);
	}
	
	public void addAll(Collection<DbElementDefinition> defs) {
		for (DbElementDefinition def: defs) {
			add(def);
		}
	}
	
	public boolean has(String defName) {
		return definitionMap.containsKey(defName);
	}

	public DbElementDefinition get(String defName) {
		DbElementDefinition def = definitionMap.get(defName);
		if (def == null) {
			throw new IllegalArgumentException("no dbElement named [" + defName + "]");
		}
		return def;
	}
	
	public Collection<DbElementDefinition> listAll() {
		return definitionMap.values();
	}
}
