package com.bstek.dorado.jdbc;

import com.bstek.dorado.data.provider.AbstractDataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.DbElementTrigger;
import com.bstek.dorado.jdbc.model.QueryOperation;
import com.bstek.dorado.util.Assert;

public class JdbcDataProvider extends AbstractDataProvider {

	private String dbElement;

	public String getDbElement() {
		return dbElement;
	}

	public void setDbElement(String dbElement) {
		this.dbElement = dbElement;
	}

	@Override
	protected Object internalGetResult(Object parameter, DataType resultDataType)
			throws Exception {
		String elementName = this.getDbElement();
		Assert.notEmpty(elementName);
		
		DbElement dbElement = JdbcUtils.getDbElement(elementName);
		try {
			JdbcQueryContext rCtx = JdbcQueryContext.newInstance(parameter);
			rCtx.setJdbcEnviroment(dbElement.getJdbcEnviroment());
			
			QueryOperation operation = new QueryOperation(dbElement);
			DbElementTrigger trigger = dbElement.getTrigger();
			if (trigger != null) {
				trigger.doQuery(operation);
			} else {
				operation.execute();
			}
			
			return rCtx.getPage().getEntities();
		} finally {
			JdbcQueryContext.clear();
		}
	}

	@Override
	protected void internalGetResult(Object parameter, Page<?> page,
			DataType resultDataType) throws Exception {
		String elementName = this.getDbElement();
		Assert.notEmpty(elementName);
		
		DbElement dbElement = JdbcUtils.getDbElement(elementName);
		try {
			JdbcQueryContext rCtx = JdbcQueryContext.newInstance(parameter, page);
			rCtx.setJdbcEnviroment(dbElement.getJdbcEnviroment());
			
			QueryOperation operation = new QueryOperation(dbElement);
			DbElementTrigger trigger = dbElement.getTrigger();
			if (trigger != null) {
				trigger.doQuery(operation);
			} else {
				operation.execute();
			}
		} finally {
			JdbcQueryContext.clear();
		}
	}

}
