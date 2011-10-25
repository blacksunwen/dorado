package com.bstek.dorado.jdbc;

import com.bstek.dorado.data.provider.Page;

/**
 * {@link JdbcDataProvider}执行时的上下文
 * 
 * @author mark
 * 
 */
public class JdbcDataProviderContext extends AbstractJdbcContext {

	@SuppressWarnings("rawtypes")
	public JdbcDataProviderContext(JdbcEnviroment enviroment, Object parameter, Page page) {
		super(enviroment, parameter);
		if (page == null) {
			page = new Page(0,0);
		}
		this.page = page;
	}

	@SuppressWarnings("rawtypes")
	private Page page;

	@SuppressWarnings("rawtypes")
	public Page getPage() {
		return page;
	}

}
