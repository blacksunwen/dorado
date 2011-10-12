package com.bstek.dorado.jdbc;

import com.bstek.dorado.data.resolver.AbstractDataResolver;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.util.Assert;


/**
 * JDBC模块的{@link com.bstek.dorado.data.resolver.DataResolver}
 * 
 * @author mark
 * 
 */
public class JdbcDataResolver extends AbstractDataResolver {

	private String dbElement;

	public String getDbElement() {
		return dbElement;
	}

	public void setDbElement(String dbElement) {
		this.dbElement = dbElement;
	}

	@Override
	protected Object internalResolve(DataItems dataItems, Object parameter)
			throws Exception {
		String elementName = this.getDbElement();
		Assert.notEmpty(elementName);

		// DbElement dbElement = JdbcUtils.getDbElement(elementName);

		return null;
	}

}
