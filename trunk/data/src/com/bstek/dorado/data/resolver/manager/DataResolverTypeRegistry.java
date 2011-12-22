package com.bstek.dorado.data.resolver.manager;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 默认的DataResolver类型的注册管理类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Dec 18, 2007
 */
public class DataResolverTypeRegistry {
	private Map<String, DataResolverTypeRegisterInfo> typeMap = new LinkedHashMap<String, DataResolverTypeRegisterInfo>();
	private String defaultType;

	/**
	 * 设置默认的DataResolver的类型名。<br>
	 * 如果用户在调用的{@link #getTypeRegistryInfo(String)}方法时，
	 * 没有指定DataResolver的类型名（传递null值）， 那么管理器将使用此属性指定的DataResolver的类型名来完成内部的处理。
	 * 
	 * @param defaultType
	 *            默认的DataResolver的类型名
	 */
	public void setDefaultType(String defaultType) {
		this.defaultType = defaultType;
	}

	public String getDefaultType() {
		return defaultType;
	}

	public void registerType(DataResolverTypeRegisterInfo registryInfo) {
		typeMap.put(registryInfo.getType(), registryInfo);
	}

	public DataResolverTypeRegisterInfo getTypeRegistryInfo(String type) {
		if (StringUtils.isEmpty(type)) {
			type = defaultType;
		}
		return typeMap.get(type);
	}

	public Collection<DataResolverTypeRegisterInfo> getTypes() {
		return typeMap.values();
	}
}
