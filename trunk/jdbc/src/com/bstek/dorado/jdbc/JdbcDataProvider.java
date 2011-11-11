package com.bstek.dorado.jdbc;

import com.bstek.dorado.data.provider.AbstractDataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.DbElementTrigger;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.util.Assert;

/**
 * JDBC模块的{@link com.bstek.dorado.data.provider.DataProvider}
 * 
 * @author mark
 * 
 */
public class JdbcDataProvider extends AbstractDataProvider {

	private String dbElement;

	public String getDbElement() {
		return dbElement;
	}

	public void setDbElement(String dbElement) {
		this.dbElement = dbElement;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Object internalGetResult(Object parameter, DataType resultDataType)
			throws Exception {
		Page page = new Page(0, 0);
		this.internalGetResult(parameter, page, resultDataType);

		return page.getEntities();
	}

	@Override
	protected void internalGetResult(Object parameter, Page<?> page,
			DataType resultDataType) throws Exception {
		String elementName = this.getDbElement();
		Assert.notEmpty(elementName, "dbElement must not be null.");

		DbElement dbElement = JdbcUtils.getDbElement(elementName);
		JdbcDataProviderContext rCtx = new JdbcDataProviderContext(dbElement.getJdbcEnviroment(),parameter, page);

		JdbcDataProviderOperation operation = new JdbcDataProviderOperation(dbElement,
				rCtx);
		
		Assert.isTrue(dbElement instanceof DbTable, "["+dbElement.getName()+"] is not table.");

		DbTable table = (DbTable)dbElement;
		DbElementTrigger trigger = table.getTrigger();
		if (trigger != null) {
			trigger.doQuery(operation);
		} else {
			operation.execute();
		}
	}

}
