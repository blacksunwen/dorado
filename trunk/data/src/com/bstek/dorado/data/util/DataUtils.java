package com.bstek.dorado.data.util;

import java.lang.reflect.Type;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-9-26
 */
public abstract class DataUtils {
	private static DataTypeManager dataTypeManager;

	private static DataTypeManager getDataTypeManager() throws Exception {
		if (dataTypeManager == null) {
			dataTypeManager = (DataTypeManager) Context.getCurrent()
					.getServiceBean("dataTypeManager");
		}
		return dataTypeManager;
	}

	/**
	 * 根据DataType的名字返回相应的DataType。
	 * 
	 * @param name
	 *            DataType的名字
	 * @throws Exception
	 */
	public static DataType getDataType(String name) throws Exception {
		return getDataTypeManager().getDataType(name);
	}

	/**
	 * 根据注册信息来确定应该用哪种DataType来描述给定的Class类型。 即根据Java数据类型自动选择最为匹配的DataType。
	 * 
	 * @param type
	 *            给定的Class类型
	 * @throws Exception
	 */
	public static DataType getDataType(Type type) throws Exception {
		return getDataTypeManager().getDataType(type);
	}
}
