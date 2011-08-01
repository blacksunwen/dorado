package com.bstek.dorado.view.registry;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bstek.dorado.util.clazz.ClassTypeRegistry;
import com.bstek.dorado.view.widget.layout.Layout;

/**
 * 默认的布局管理器类型的注册管理器。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 16, 2008
 */
public class DefaultLayoutTypeRegistry implements LayoutTypeRegistry {
	private String defaultType;
	private ClassTypeRegistry classTypeRegistry = new ClassTypeRegistry();
	private Map<String, LayoutTypeRegisterInfo> registerInfoMap = new LinkedHashMap<String, LayoutTypeRegisterInfo>();

	public String getDefaultType() {
		return defaultType;
	}

	public void setDefaultType(String defaultType) {
		this.defaultType = defaultType;
	}

	public void registerType(LayoutTypeRegisterInfo registerInfo) {
		registerInfoMap.put(registerInfo.getType().toLowerCase(), registerInfo);
		classTypeRegistry.registerType(registerInfo.getClassType(),
				registerInfo);
	}

	public LayoutTypeRegisterInfo getRegisterInfo(String type) {
		return registerInfoMap.get(type.toLowerCase());
	}

	public LayoutTypeRegisterInfo getRegisterInfo(
			Class<? extends Layout> classType) {
		return (LayoutTypeRegisterInfo) classTypeRegistry
				.getMatchingValue(classType);
	}

	public Collection<LayoutTypeRegisterInfo> getRegisterInfos() {
		return registerInfoMap.values();
	}

}