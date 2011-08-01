package com.bstek.dorado.data.type;

/**
 * Map类型的通用接口。
 * <p>
 * 用于封装Map类数据的数据类型。java.util.HashMap、java.util.Properties等对象都可以通过此类型来描述。
 * </p>
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 13, 2007
 */
public interface MapDataType extends EntityDataType {
	/**
	 * 返回键对应的DataType。
	 */
	DataType getKeyDataType();

	/**
	 * 设置键对应的DataType。
	 */
	void setKeyDataType(DataType keyType);

	/**
	 * 返回键值对应的DataType。
	 */
	DataType getValueDataType();

	/**
	 * 设置键值对应的DataType。
	 */
	void setValueDataType(DataType valueType);
}
