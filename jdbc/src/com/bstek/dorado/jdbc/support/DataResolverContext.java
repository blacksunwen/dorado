package com.bstek.dorado.jdbc.support;

import java.util.List;

import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.jdbc.JdbcDataResolverItem;
import com.bstek.dorado.jdbc.JdbcEnviroment;

/**
 * 执行DataResolver时的上下文对象
 * @author mark.li@bstek.com
 *
 */
public class DataResolverContext extends AbstractJdbcContext {

	private DataItems dataItems;
	private List<JdbcDataResolverItem> resolverItems;
	private Object returnValue;
	
	public DataResolverContext(JdbcEnviroment enviroment, Object parameter,
			DataItems dataItems, List<JdbcDataResolverItem> resolverItems) {
		super(enviroment, parameter);
		
		this.dataItems = dataItems;
		this.resolverItems = resolverItems;
	}

	public DataResolverContext(Object parameter, DataItems dataItems, 
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
