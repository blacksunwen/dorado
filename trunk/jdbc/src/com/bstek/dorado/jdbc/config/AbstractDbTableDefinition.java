package com.bstek.dorado.jdbc.config;


/**
 * 抽象的{@link com.bstek.dorado.jdbc.model.DbTable}的定义对象
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractDbTableDefinition extends DbElementDefinition {
	private boolean autoCreateDataType = false;
	private boolean autoCreateDataProvider = false;

	/**
	 * 是否自动创建DataProvider
	 * @return
	 */
	public boolean isAutoCreateDataProvider() {
		return autoCreateDataProvider;
	}

	public void setAutoCreateDataProvider(boolean autoCreateDataProvider) {
		this.autoCreateDataProvider = autoCreateDataProvider;
	}

	/**
	 * 是否自动创建DataType
	 * @return
	 */
	public boolean isAutoCreateDataType() {
		return autoCreateDataType;
	}

	public void setAutoCreateDataType(boolean autoCreateDataType) {
		this.autoCreateDataType = autoCreateDataType;
	}
}
