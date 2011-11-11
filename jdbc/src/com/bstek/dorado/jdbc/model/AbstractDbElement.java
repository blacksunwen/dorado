package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;

public abstract class AbstractDbElement implements DbElement {

	private String name;
	
	private JdbcEnviroment env;
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@ViewAttribute(visible=false)
	@Override
	public JdbcEnviroment getJdbcEnviroment() {
		if (env != null) {
			return env;
		} else {
			return JdbcUtils.getEnviromentManager().getDefault();
		}
	}

	@Override
	public void setJdbcEnviroment(JdbcEnviroment env) {
		this.env = env;
	}

}
