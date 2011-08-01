package com.bstek.dorado.data.entity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.beans.BeanMap;

import com.bstek.dorado.data.type.EntityDataType;

public abstract class AbstractMapEntityEnhancer extends EntityEnhancer {

	public AbstractMapEntityEnhancer(EntityDataType dataType) {
		super(dataType);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Set<String> doGetPropertySet(Object entity) {
		Map<String, Object> exProperties = getExProperties(false);
		if (exProperties == null || exProperties.isEmpty()) {
			return ((Map) entity).keySet();
		} else {
			Set<String> propertySet = new HashSet<String>(
					((Map) entity).keySet());
			propertySet.addAll(exProperties.keySet());
			return propertySet;
		}
	}

	protected boolean isExProperty(Object entity, String property) {
		if (entity instanceof BeanMap) {
			return ((BeanMap) entity).containsKey(property);
		} else {
			return false;
		}
	}

	@Override
	public Object readProperty(Object entity, String property,
			boolean ignoreInterceptors) throws Throwable {
		boolean isExProp = isExProperty(entity, property);
		Object result = internalReadProperty(entity, property, isExProp);
		if (ignoreInterceptors) {
			return result;
		} else {
			return interceptReadMethod(entity, property, result, isExProp);
		}
	}

	@Override
	public void writeProperty(Object entity, String property, Object value)
			throws Throwable {
		boolean isExProp = isExProperty(entity, property);
		if (interceptWriteMethod(entity, property, value, isExProp)) {
			internalWriteProperty(entity, property, value, isExProp);
		}
	}

}