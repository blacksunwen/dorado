package com.bstek.dorado.jdbc;

import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;

public class QueryArg {
	Object parameter;
	Page<Record> page = new Page<Record>(0, 0);;
	JdbcEnviroment jdbcEnviroment;
	
	public Object getParameter() {
		return parameter;
	}
	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}
	public Page<Record> getPage() {
		return page;
	}
	public void setPage(Page<Record> page) {
		this.page = page;
	}
	public JdbcEnviroment getJdbcEnviroment() {
		return jdbcEnviroment;
	}
	public void setJdbcEnviroment(JdbcEnviroment jdbcEnviroment) {
		this.jdbcEnviroment = jdbcEnviroment;
	}
	
}
