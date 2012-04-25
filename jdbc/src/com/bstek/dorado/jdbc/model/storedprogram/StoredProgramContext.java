package com.bstek.dorado.jdbc.model.storedprogram;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.support.AbstractJdbcContext;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class StoredProgramContext extends AbstractJdbcContext {

	private MapSqlParameterSource parameterSource = new MapSqlParameterSource();
	private Object returnValue;
	
	public StoredProgramContext(JdbcEnviroment enviroment, Object parameter) {
		super(enviroment, parameter);
	}

	public MapSqlParameterSource getSqlParameterSource() {
		return parameterSource;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}
	
}
