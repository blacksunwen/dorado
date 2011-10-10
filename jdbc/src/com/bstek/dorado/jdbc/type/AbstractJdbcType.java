package com.bstek.dorado.jdbc.type;

import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.util.DataUtils;
import com.bstek.dorado.jdbc.JdbcConstants;

public abstract class AbstractJdbcType implements JdbcType {

	private String   name;
	private int      jdbcCode;
	private String   jdbcName;	
	private DataType dataType;
	private String   dataTypeName;
	
	public AbstractJdbcType(String name, String jdbcName, String dataTypeName) {
		this.setName(name);
		this.setJdbcName(jdbcName);
		this.setDataTypeName(dataTypeName);
	}
	
	public AbstractJdbcType(String jdbcName, String dataTypeName) {
		this(jdbcName + "-" + dataTypeName, jdbcName, dataTypeName);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public int getJdbcCode() {
		return jdbcCode;
	}
	
	public String getJdbcName() {
		return jdbcName;
	}
	public String setJdbcName(String jdbcName) {
		Number jdbcTypeCode = JdbcConstants.JDBC_TYPE_CONSTANTS.asNumber(jdbcName);
		this.jdbcCode = jdbcTypeCode.intValue();
		return this.jdbcName = jdbcName;
	}

	public DataType getDataType() {
		if (dataType == null) {
			try {
				dataType = DataUtils.getDataType(dataTypeName);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
	}

	public Object fromDB(Object obj) {
		return getDataType().fromObject(obj);
	}

	public Object toDB(Object obj) {
		return obj;
	}
	
}
