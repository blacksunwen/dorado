package com.bstek.dorado.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.bstek.dorado.data.type.DataType;

/**
 * 数据库相关的数据类型
 * 
 * @author mark.li@bstek.com
 *
 */
public interface JdbcType {

	/**
	 * 获取名称
	 * @return
	 */
	String getName();
	
	/**
	 * 对应与数据库的TYPE_NAME
	 * @return
	 */
	String getTypeName();
	
	/**
	 * JDBC表示的数据类型代码
	 * @see java.sql.Types
	 * @return
	 */
	int getSqlType();
	
	/**
	 * 对于数字类型数据的精度
	 * @return
	 */
	Integer getScale();
	
	/**
	 * 对应于dorado的{@link DataType}
	 * @return
	 */
	DataType getDataType();
	
	/**
	 * 从ResultSet中获取Java对象
	 * @param rs
	 * @param columnName
	 * @return
	 */
	Object fromDB(ResultSet rs, String columnName) throws SQLException;
	
	/**
	 * 将数据库数据对象映射成Java对象
	 * @param obj
	 * @return
	 */
	Object fromDB(Object obj);
	
	/**
	 * 将Java对象映射成数据库数据对象
	 * @param obj
	 * @return
	 */
	Object toDB(Object obj);
	
	/**
	 * 根据数据库中列属性，判断是否是该JdbcType
	 * @param columnMeta
	 * @return
	 */
	boolean is(Map<String,String> columnMeta);
}
