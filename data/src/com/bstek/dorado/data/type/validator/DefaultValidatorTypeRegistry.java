package com.bstek.dorado.data.type.validator;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-27
 */
public class DefaultValidatorTypeRegistry implements ValidatorTypeRegistry {

	private Map<String, ValidatorTypeRegisterInfo> typeMap = new LinkedHashMap<String, ValidatorTypeRegisterInfo>();

	public void registerType(ValidatorTypeRegisterInfo registryInfo) {
		typeMap.put(registryInfo.getType(), registryInfo);
	}

	public ValidatorTypeRegisterInfo getTypeRegisterInfo(String type) {
		return typeMap.get(type);
	}

	public Collection<ValidatorTypeRegisterInfo> getTypes() {
		return typeMap.values();
	}

}
