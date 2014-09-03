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

package com.bstek.dorado.data.type.manager;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2014-9-3
 */
public class DataTypeTypeRegistry {
	private Map<String, DataTypeTypeRegisterInfo> typeMap = new LinkedHashMap<String, DataTypeTypeRegisterInfo>();
	private String defaultType;

	public void setDefaultType(String defaultType) {
		this.defaultType = defaultType;
	}

	public String getDefaultType() {
		return defaultType;
	}

	public void registerType(DataTypeTypeRegisterInfo registryInfo) {
		typeMap.put(registryInfo.getType(), registryInfo);
	}

	public DataTypeTypeRegisterInfo getTypeRegistryInfo(String type) {
		if (StringUtils.isEmpty(type)) {
			type = defaultType;
		}
		return typeMap.get(type);
	}

	public Collection<DataTypeTypeRegisterInfo> getTypes() {
		return typeMap.values();
	}
}
