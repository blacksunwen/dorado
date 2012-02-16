package com.bstek.dorado.jdbc;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.data.provider.AbstractDataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.util.Assert;

/**
 * JDBC模块的{@link com.bstek.dorado.data.provider.DataProvider}
 * 
 * @author mark
 * 
 */
@XmlNode(fixedProperties = "type=jdbc")
public class JdbcDataProvider extends AbstractDataProvider {

	private String tableName;

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
	
	@Override
	protected Object internalGetResult(Object parameter, DataType resultDataType)
			throws Exception {
		Assert.notEmpty(tableName, "tableName must not be empty.");
		
		QueryArg arg = new QueryArg();
		arg.setJdbcEnviroment(getJdbcEnviroment());
		arg.setParameter(parameter);
		
		return JdbcUtils.query(tableName, arg);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void internalGetResult(Object parameter, Page<?> page,
			DataType resultDataType) throws Exception {
		Assert.notEmpty(tableName, "tableName must not be empty.");
		QueryArg arg = new QueryArg();
		arg.setJdbcEnviroment(getJdbcEnviroment());
		arg.setParameter(parameter);
		arg.setPage((Page<Record>)page);
		
		JdbcUtils.query(tableName, arg);
	}

}
