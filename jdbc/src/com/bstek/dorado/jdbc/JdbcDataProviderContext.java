package com.bstek.dorado.jdbc;

import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;

/**
 * {@link JdbcDataProvider}执行时的上下文
 * 
 * @author mark.li@bstek.com
 * 
 */
public class JdbcDataProviderContext extends AbstractJdbcContext {
	private Page<Record> page;
	
	public JdbcDataProviderContext() {
		this(null, null, null);
	}

	public JdbcDataProviderContext(JdbcEnviroment enviroment){
		this(enviroment, null, null);
	}
	
	public JdbcDataProviderContext(JdbcEnviroment enviroment, Object parameter) {
		this(enviroment, parameter, null);
	}
	
	public JdbcDataProviderContext(JdbcEnviroment enviroment, Object parameter, Page<Record> page) {
		super(enviroment, parameter);
		if (page == null) {
			page = new Page<Record>(0,0);
		}
		this.page = page;
	}

	public Page<Record> getPage() {
		return page;
	}
	
}
