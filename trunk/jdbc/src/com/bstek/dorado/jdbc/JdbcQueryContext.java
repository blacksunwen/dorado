package com.bstek.dorado.jdbc;

import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.JdbcEnviroment;
import com.bstek.dorado.jdbc.sql.SqlUtils;

public class JdbcQueryContext {

	private static final ThreadLocal<JdbcQueryContext> 
		LOCAL = new ThreadLocal<JdbcQueryContext>();
	
	public static JdbcQueryContext getInstance() {
		JdbcQueryContext c = LOCAL.get();
		if (c == null) {
			throw new IllegalStateException("no RuntimeContext exists.");
		}
		return c;
	}
	
	public static void clear() {
		LOCAL.remove();
	}
	
	public static JdbcQueryContext newInstance(Object parameter) {
		JdbcQueryContext c = LOCAL.get();
		if (c != null) {
			throw new IllegalStateException("A RuntimeContext already exists.");
		} else {
			c = new JdbcQueryContext(parameter);
			LOCAL.set(c);
		}
		return c;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static JdbcQueryContext newInstance(Object parameter, Page page) {
		JdbcQueryContext c = LOCAL.get();
		if (c != null) {
			throw new IllegalStateException("A RuntimeContext already exists.");
		} else {
			c = new JdbcQueryContext(parameter, page);
			LOCAL.set(c);
		}
		
		return c;
	}
	
	private JdbcEnviroment enviroment;
	private Object parameter;
	private JdbcParameterSource parameterSource;
	private Page<Record> page = new Page<Record>(0,0);
	
	private JdbcQueryContext(Object parameter) {
		super();
		this.parameter = parameter;
		this.parameterSource = SqlUtils.createJdbcParameter(parameter);
	}
	
	private JdbcQueryContext(Object parameter, Page<Record> page) {
		this(parameter);
		this.page = page;
	}
	
	public Object getParameter() {
		return parameter;
	}
	
	public JdbcParameterSource getJdbcParameterSource() {
		return parameterSource;
	}
	
	public Page<Record> getPage() {
		return page;
	}

	public JdbcEnviroment getJdbcEnviroment() {
		return enviroment;
	}

	public void setJdbcEnviroment(JdbcEnviroment enviroment) {
		this.enviroment = enviroment;
	}

}
