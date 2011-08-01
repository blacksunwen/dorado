package com.bstek.dorado.data.provider.manager;

import java.util.Collection;

/**
 * DataProvider类型的注册管理类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Dec 18, 2007
 * @see com.bstek.dorado.data.provider.DataProvider
 */
public interface DataProviderTypeRegistry {
	/**
	 * 返回默认的DataProvider的类型名。
	 */
	String getDefaultType();

	/**
	 * 注册一种DataProvider类型。
	 * 
	 * @param registryInfo
	 *            DataProvider的类型注册信息。
	 */
	void registerType(DataProviderTypeRegisterInfo registryInfo);

	/**
	 * 根据组件类型名称返回相应的DataProvider类型注册信息。
	 * 
	 * @param type
	 *            类型名称。
	 * @return DataProvider的类型注册信息。
	 */
	DataProviderTypeRegisterInfo getTypeRegistryInfo(String type);

	/**
	 * 返回所有DataProvider类型的注册信息。
	 */
	Collection<DataProviderTypeRegisterInfo> getTypes();

}
