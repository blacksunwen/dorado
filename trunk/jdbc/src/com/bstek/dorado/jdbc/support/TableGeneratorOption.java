package com.bstek.dorado.jdbc.support;

import com.bstek.dorado.jdbc.JdbcEnviroment;

public class TableGeneratorOption {

	private boolean generateCatalog = true;
	private boolean generateSchema = true;
	private JdbcEnviroment jdbcEnviroment;
	
	public boolean isGenerateCatalog() {
		return generateCatalog;
	}
	public void setGenerateCatalog(boolean generateCatalog) {
		this.generateCatalog = generateCatalog;
	}
	public boolean isGenerateSchema() {
		return generateSchema;
	}
	public void setGenerateSchema(boolean generateSchema) {
		this.generateSchema = generateSchema;
	}
	public JdbcEnviroment getJdbcEnviroment() {
		return jdbcEnviroment;
	}
	public void setJdbcEnviroment(JdbcEnviroment jdbcEnviroment) {
		this.jdbcEnviroment = jdbcEnviroment;
	}
	
}
