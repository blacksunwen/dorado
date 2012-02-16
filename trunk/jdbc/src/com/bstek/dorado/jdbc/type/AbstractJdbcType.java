package com.bstek.dorado.jdbc.type;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.util.DataUtils;
import com.bstek.dorado.jdbc.support.JdbcConstants;

public abstract class AbstractJdbcType implements JdbcType {

	private String   name;
	private int      sqlType;
	private String   typeName;	
	private DataType dataType;
	private String   dataTypeName;
	private Integer  scale;
	
	public AbstractJdbcType(String name, String typeName, String dataTypeName) {
		this.setName(name);
		this.setTypeName(typeName);
		this.setDataTypeName(dataTypeName);
	}
	
	public AbstractJdbcType(String typeName, String dataTypeName) {
		this(typeName + "-" + dataTypeName, typeName, dataTypeName);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public int getSqlType() {
		return sqlType;
	}
	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}
	
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
		this.onSetTypeName();
	}
	
	protected void onSetTypeName() {
		int sqlType = JdbcConstants.getTypeValue(typeName);
		this.sqlType = sqlType;
	}
	
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
		this.dataTypeName = this.dataType.getId();
	}

	public void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
	}
	
	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
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

	public Object fromDB(Object obj) {
		return getDataType().fromObject(obj);
	}

	public Object toDB(Object obj) {
		return fromDB(obj);
	}
	
	public boolean is(Map<String,String> columnMeta) {
		String dataType = columnMeta.get(JdbcConstants.DATA_TYPE);
		if (StringUtils.isNotEmpty(dataType)) {
			int code = Integer.valueOf(dataType);
			if (code == this.getSqlType()) {
				return true;
			}
		}
		
		return false;
	}
}
