package com.bstek.dorado.jdbc;

import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;

/**
 * {@link JdbcDataProvider}执行时的上下文
 * 
 * @author mark
 * 
 */
public class JdbcDataProviderContext extends AbstractJdbcContext {
	private Page<Record> page;
	
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
