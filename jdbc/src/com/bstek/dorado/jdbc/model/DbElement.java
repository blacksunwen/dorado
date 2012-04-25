package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.jdbc.JdbcEnviroment;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public interface DbElement {

	JdbcEnviroment getJdbcEnviroment();
	
	void setJdbcEnviroment(JdbcEnviroment env);
	
	String getName();
	
	String getType();

}
