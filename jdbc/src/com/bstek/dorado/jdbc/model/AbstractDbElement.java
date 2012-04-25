package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.config.JdbcEnviromentManager;

/**
 * @author mark.li@bstek.com
 *
 */
@XmlNode(implTypes = "com.bstek.dorado.jdbc.model.*")
public abstract class AbstractDbElement implements DbElement {

	private String name;
	
	private JdbcEnviroment env;
	
	private JdbcEnviromentManager enviromentManager;
	
	public JdbcEnviromentManager getEnviromentManager() {
		return enviromentManager;
	}

	public void setEnviromentManager(JdbcEnviromentManager enviromentManager) {
		this.enviromentManager = enviromentManager;
	}

	@IdeProperty(highlight=1)
	@XmlProperty(attributeOnly=true)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlProperty(parser="spring:dorado.jdbc.jdbcEnviromentParser")
	@IdeProperty(visible=false)
	public JdbcEnviroment getJdbcEnviroment() {
		if (env != null) {
			return env;
		} else {
			return getEnviromentManager().getDefault();
		}
	}

	public void setJdbcEnviroment(JdbcEnviroment env) {
		this.env = env;
	}

}
