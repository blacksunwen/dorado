package com.bstek.dorado.jdbc;

import java.util.List;

import com.bstek.dorado.data.resolver.DataItems;

/**
 * 执行DataResolver时的上下文对象
 * @author mark
 *
 */
public class JdbcDataResolverContext extends AbstractJdbcContext {

	private DataItems dataItems;
	private List<JdbcDataResolverItem> resolverItems;
	private Object returnValue;
	
	public JdbcDataResolverContext(JdbcEnviroment enviroment, Object parameter,
			DataItems dataItems, List<JdbcDataResolverItem> resolverItems) {
		super(enviroment, parameter);
		
		this.dataItems = dataItems;
		this.resolverItems = resolverItems;
	}

	public JdbcDataResolverContext(Object parameter, DataItems dataItems, 
			List<JdbcDataResolverItem> resolverItems) {
		this(null, parameter, dataItems, resolverItems);
	}

	public DataItems getDataItems() {
		return dataItems;
	}

	public void setDataItems(DataItems dataItems) {
		this.dataItems = dataItems;
	}

	public List<JdbcDataResolverItem> getResolverItems() {
		return resolverItems;
	}

	public void setResolverItems(List<JdbcDataResolverItem> resolverItems) {
		this.resolverItems = resolverItems;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}
	
}
