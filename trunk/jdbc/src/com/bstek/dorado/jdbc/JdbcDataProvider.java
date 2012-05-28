package com.bstek.dorado.jdbc;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.data.provider.AbstractDataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.AggregationDataType;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.support.DataProviderContext;
import com.bstek.dorado.jdbc.support.QueryOperation;
import com.bstek.dorado.util.Assert;

/**
 * JDBC模块的{@link com.bstek.dorado.data.provider.DataProvider}
 * 
 * @author mark.li@bstek.com
 * 
 */
@XmlNode(
	fixedProperties = "type=jdbc"
)
public class JdbcDataProvider extends AbstractDataProvider {

	private String tableName;
	private boolean autoFilter = false;
	private JdbcEnviroment jdbcEnviroment;
	
	@IdeProperty(highlight=1, editor="jdbc:refrence:Table")
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@XmlProperty(parser="spring:dorado.jdbc.jdbcEnviromentParser")
	public JdbcEnviroment getJdbcEnviroment() {
		return jdbcEnviroment;
	}

	public void setJdbcEnviroment(JdbcEnviroment jdbcEnviroment) {
		this.jdbcEnviroment = jdbcEnviroment;
	}
	
	public void setAutoFilter(boolean autoFilter) {
		this.autoFilter = autoFilter;
	}
	
	@ClientProperty(escapeValue="false")
	public boolean isAutoFilter() {
		return this.autoFilter;
	}
	
	@Override
	protected Object internalGetResult(Object parameter, DataType resultDataType)
			throws Exception {
		QueryOperation operation = createOperation(parameter, null, resultDataType);
		return operation.getJdbcDao().query(operation);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void internalGetResult(Object parameter, Page<?> page,
			DataType resultDataType) throws Exception {
		QueryOperation operation = createOperation(parameter, (Page<Record>) page, resultDataType);
		operation.getJdbcDao().query(operation);
	}

	protected QueryOperation createOperation(Object parameter, Page<Record> page, DataType resultDataType) {
		Assert.notEmpty(tableName, "tableName must not be empty.");
		
		DataProviderContext jCtx = new DataProviderContext(getJdbcEnviroment(), parameter, page);
		jCtx.setAutoFilter(this.isAutoFilter());
		EntityDataType entityDataType = null;
		if (resultDataType instanceof EntityDataType) {
			entityDataType = (EntityDataType)entityDataType;
		} else if (resultDataType instanceof AggregationDataType) {
			AggregationDataType aggregationDataType = (AggregationDataType)resultDataType;
			DataType elementDataType = aggregationDataType.getElementDataType();
			if (elementDataType instanceof EntityDataType) {
				entityDataType = (EntityDataType)elementDataType;
			}
		}
		if (entityDataType != null) {
			jCtx.setDataType(entityDataType);
		}
		
		DbTable table = JdbcUtils.getDbTable(tableName);
		QueryOperation operation = new QueryOperation(table, jCtx);
		
		return operation;
	}
}
