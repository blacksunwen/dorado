/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.view.registry;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bstek.dorado.util.clazz.ClassTypeRegistry;
import com.bstek.dorado.view.widget.layout.Layout;

/**
 * 默认的布局管理器类型的注册管理器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 16, 2008
 */
public class DefaultLayoutTypeRegistry implements LayoutTypeRegistry {
	private String defaultType;
	private ClassTypeRegistry<LayoutTypeRegisterInfo> classTypeRegistry = new ClassTypeRegistry<LayoutTypeRegisterInfo>();
	private Map<String, LayoutTypeRegisterInfo> registerInfoMap = new LinkedHashMap<String, LayoutTypeRegisterInfo>();

	public String getDefaultType() {
		return defaultType;
	}

	public void setDefaultType(String defaultType) {
		this.defaultType = defaultType;
	}

	public synchronized void registerType(LayoutTypeRegisterInfo registerInfo) {
		registerInfoMap.put(registerInfo.getType().toLowerCase(), registerInfo);
		classTypeRegistry.registerType(registerInfo.getClassType(),
				registerInfo);
	}

	public synchronized LayoutTypeRegisterInfo getRegisterInfo(String type) {
		return registerInfoMap.get(type.toLowerCase());
	}

	public synchronized LayoutTypeRegisterInfo getRegisterInfo(
			Class<? extends Layout> classType) {
		return classTypeRegistry
				.getMatchingValue(classType);
	}

	public synchronized Collection<LayoutTypeRegisterInfo> getRegisterInfos() {
		return registerInfoMap.values();
	}

}
