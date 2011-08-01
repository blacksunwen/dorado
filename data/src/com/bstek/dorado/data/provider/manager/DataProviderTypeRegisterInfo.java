package com.bstek.dorado.data.provider.manager;

import com.bstek.dorado.config.Parser;

/**
 * DataProvider的类型注册信息。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 27, 2008
 * @see com.bstek.dorado.data.provider.manager.DataProviderTypeRegistry
 * @see com.bstek.dorado.data.provider.manager.DataProviderTypeRegister
 */
public class DataProviderTypeRegisterInfo {
	private String type;
	private Class<?> classType;
	private Parser parser;

	/**
	 * @param type
	 *            DataProvider的类型名
	 * @param classType
	 *            DataProvider的Class类型
	 */
	public DataProviderTypeRegisterInfo(String type, Class<?> classType) {
		this.type = type;
		this.classType = classType;
	}

	/**
	 * 返回DataProvider的类型名
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置DataProvider的类型名
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 返回DataProvider的Class类型。
	 */
	public Class<?> getClassType() {
		return classType;
	}

	/**
	 * 设置DataProvider的Class类型。
	 */
	public void setClassType(Class<?> classType) {
		this.classType = classType;
	}

	/**
	 * 返回对应的配置解析器。
	 */
	public Parser getParser() {
		return parser;
	}

	/**
	 * 设置对应的配置解析器。
	 */
	public void setParser(Parser parser) {
		this.parser = parser;
	}
}
