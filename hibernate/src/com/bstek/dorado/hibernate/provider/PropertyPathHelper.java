package com.bstek.dorado.hibernate.provider;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.BasePropertyDef;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.util.Assert;

/**
 * 操作EntityDataType的帮助类，根据字段路径找到属性路径
 * @author mark
 *
 */
public class PropertyPathHelper {

	private EntityDataType entityDataType;
	private Map<String, String> pairMap;
	
	public PropertyPathHelper(EntityDataType entityDataType) {
		this.entityDataType = entityDataType;
		this.reset();
	}
	
	public String getPropertyPath(final String fieldPath) throws Exception {
		String propertyPath = pairMap.get(fieldPath);
		if (propertyPath != null) {
			return propertyPath;
		}
		
		Map<String, PropertyDef> propertyDefs = entityDataType.getPropertyDefs();
		PropertyDef propertyDef = propertyDefs.get(fieldPath);
		if (propertyDef != null) {
			if (!(propertyDef instanceof BasePropertyDef)) 
				throw new IllegalArgumentException("the type of property '" + fieldPath + "' is not BasePropertyDef.");
			BasePropertyDef baseProperty = (BasePropertyDef) propertyDef;
			return returnValue(StringUtils.defaultIfEmpty(baseProperty.getPropertyPath(), fieldPath), fieldPath);
		} else {
			String[] tokens = StringUtils.split(fieldPath, '.');
			if (tokens.length == 1) {
				throw new IllegalArgumentException("can not find any PropertyDef named '" + fieldPath + "'.");
			}
			EntityDataType currentEntityDataType = this.entityDataType;
			for (int i=0; i<tokens.length-1; i++) {
				String token = tokens[i];
				if (currentEntityDataType.isAutoCreatePropertyDefs()) {
					currentEntityDataType.createPropertyDefinitons();
				}
				propertyDef = currentEntityDataType.getPropertyDefs().get(token);
				Assert.notNull(propertyDef, "can not find any PropertyDef named '" + fieldPath + "'.");
				
				if (!(propertyDef instanceof BasePropertyDef)) {
					throw new IllegalArgumentException("the type of property '" + fieldPath + "' is not BasePropertyDef.");
				} else {
					BasePropertyDef baseProperty = (BasePropertyDef) propertyDef;
					tokens[i] = StringUtils.defaultIfEmpty(baseProperty.getPropertyPath(), token);
					
					if (!(propertyDef.getDataType() instanceof EntityDataType)) {
						throw new IllegalArgumentException("can not find any PropertyDef named '" + fieldPath + "'.");
					} else {
						currentEntityDataType = (EntityDataType)propertyDef.getDataType();
					}
				}
			}
			
			String lastToken = tokens[tokens.length - 1];
			if (currentEntityDataType.isAutoCreatePropertyDefs()) {
				currentEntityDataType.createPropertyDefinitons();
			}
			propertyDef = currentEntityDataType.getPropertyDef(lastToken);
			Assert.notNull(propertyDef, "can not find any PropertyDef named '" + fieldPath + "'.");
			
			if (!(propertyDef instanceof BasePropertyDef)) {
				throw new IllegalArgumentException("the type of property '" + fieldPath + "' is not BasePropertyDef.");
			} else {
				BasePropertyDef baseProperty = (BasePropertyDef) propertyDef;
				tokens[tokens.length - 1] = StringUtils.defaultIfEmpty(baseProperty.getPropertyPath(), lastToken);
			}
			
			return returnValue(StringUtils.join(tokens, '.'), fieldPath);
		}
	}
	
	private String returnValue(String value, String fieldPath) {
		this.pairMap.put(fieldPath, value);
		return value;
	}
	
	private void reset() {
		try {
			if (entityDataType.isAutoCreatePropertyDefs()) {
				entityDataType.createPropertyDefinitons();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		pairMap = new HashMap<String, String>();
	}
}
