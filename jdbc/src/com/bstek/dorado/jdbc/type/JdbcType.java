package com.bstek.dorado.jdbc.type;

import com.bstek.dorado.data.type.DataType;

public interface JdbcType {

	String getName();
	
	String getTypeName();
	
	int getSqlType();
	
	Integer getScale();
	
	DataType getDataType();
	
	Object fromDB(Object obj);
	
	Object toDB(Object obj);
}
