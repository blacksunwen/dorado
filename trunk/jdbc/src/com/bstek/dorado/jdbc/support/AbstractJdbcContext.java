package com.bstek.dorado.jdbc.support;

import com.bstek.dorado.data.variant.MetaData;
import com.bstek.dorado.jdbc.JdbcEnviroment;

/**
 * 抽象的JDBC操作的上下文
 * 
 * @author mark.li@bstek.com
 * 
 */
public abstract class AbstractJdbcContext {

	private JdbcEnviroment enviroment;
	private Object parameter;
	private MetaData metaData;
	
	public AbstractJdbcContext(JdbcEnviroment enviroment, Object parameter) {
		this.enviroment = enviroment;
		this.parameter= parameter;
	}
	
	public JdbcEnviroment getJdbcEnviroment() {
		return enviroment;
	}

	public void setJdbcEnviroment(JdbcEnviroment enviroment) {
		this.enviroment = enviroment;
	}
	
	public Object getParameter() {
		return parameter;
	}

	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}

	public MetaData getMetaData() {
		if (metaData == null) {
			metaData = new MetaData();
		}
		
		return metaData;
	}
	
}
