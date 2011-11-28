package com.bstek.dorado.jdbc;

import com.bstek.dorado.data.provider.AbstractDataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.util.Assert;

/**
 * JDBC模块的{@link com.bstek.dorado.data.provider.DataProvider}
 * 
 * @author mark
 * 
 */
public class JdbcDataProvider extends AbstractDataProvider {

	private String tableName;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	protected Object internalGetResult(Object parameter, DataType resultDataType)
			throws Exception {
		Assert.notEmpty(tableName, "tableName must not be empty.");
		return JdbcUtils.query(tableName, parameter);
	}

	@Override
	protected void internalGetResult(Object parameter, Page<?> page,
			DataType resultDataType) throws Exception {
		Assert.notEmpty(tableName, "tableName must not be empty.");
		JdbcUtils.query(tableName, parameter, page);
	}

}
