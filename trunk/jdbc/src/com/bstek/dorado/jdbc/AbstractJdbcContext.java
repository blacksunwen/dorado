package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.model.JdbcEnviroment;

/**
 * JDBC操作的上下文。当{@link JdbcDataProvider}或{@link JdbcDataResolver}执行时产生这个对象
 * 
 * @author mark
 * 
 */
public abstract class AbstractJdbcContext {

	private JdbcEnviroment enviroment;
	private Object parameter;

	public AbstractJdbcContext(JdbcEnviroment enviroment, Object parameter) {
		this.enviroment = enviroment;
		this.setParameter(parameter);
	}
	
	public JdbcEnviroment getJdbcEnviroment() {
		return enviroment;
	}

	public Object getParameter() {
		return parameter;
	}

	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}

}
