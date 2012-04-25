package com.bstek.dorado.jdbc;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.data.provider.AbstractDataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.support.JdbcDataProviderContext;
import com.bstek.dorado.jdbc.support.JdbcDataProviderOperation;
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
		JdbcDataProviderOperation operation = createOperation(parameter, null);
		return operation.getJdbcEnviroment().getJdbcDao().query(operation);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void internalGetResult(Object parameter, Page<?> page,
			DataType resultDataType) throws Exception {
		JdbcDataProviderOperation operation = createOperation(parameter, (Page<Record>) page);
		operation.getJdbcEnviroment().getJdbcDao().query(operation);
	}

	protected JdbcDataProviderOperation createOperation(Object parameter, Page<Record> page) {
		Assert.notEmpty(tableName, "tableName must not be empty.");
		
		JdbcDataProviderContext jCtx = new JdbcDataProviderContext(getJdbcEnviroment(), parameter, page);
		jCtx.setAutoFilter(isAutoFilter());
		DbTable table = JdbcUtils.getDbTable(tableName);
		JdbcDataProviderOperation operation = new JdbcDataProviderOperation(table, jCtx);
		
		return operation;
	}
}
