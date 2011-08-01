package com.bstek.dorado.view.registry;

import java.util.Collection;

/**
 * 组件类型信息的注册管理器。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 21, 2008
 */
public interface ComponentTypeRegistry {
	/**
	 * 注册一种组件。
	 * @param componentTypeRegisterInfo 组件类型的注册信息。
	 */
	void registerType(ComponentTypeRegisterInfo componentTypeRegisterInfo);

	/**
	 * 根据组件类型名称返回相应的组件类型的注册信息。
	 * @param name 组件类型名称。
	 * @return 组件类型的注册信息。
	 */
	ComponentTypeRegisterInfo getRegisterInfo(String name);

	/**
	 * 根据组件的Class类型返回的组件类型的注册信息。
	 * @param classType 组件的Class类型。
	 * @return 组件类型的注册信息。
	 */
	ComponentTypeRegisterInfo getRegisterInfo(Class<?> classType);

	/**
	 * 返回所有组件类型的注册信息的集合。
	 */
	Collection<ComponentTypeRegisterInfo> getRegisterInfos();
}
