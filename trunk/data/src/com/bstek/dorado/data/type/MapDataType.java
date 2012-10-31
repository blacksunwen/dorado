/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
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
