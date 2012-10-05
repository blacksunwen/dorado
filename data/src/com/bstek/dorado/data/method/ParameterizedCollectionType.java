package com.bstek.dorado.data.method;

import java.lang.reflect.Type;
import java.util.Collection;

import com.bstek.dorado.util.Assert;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-10-2
 */
public class ParameterizedCollectionType implements Type {
	private Class<Collection<?>> collectionType;
	private Class<?> elementType;

	public ParameterizedCollectionType(Class<Collection<?>> collectionType,
			Class<?> elementType) {
		Assert.notNull(collectionType);
		Assert.notNull(elementType);
		this.collectionType = collectionType;
		this.elementType = elementType;
	}

	public Class<Collection<?>> getCollectionType() {
		return collectionType;
	}

	public Class<?> getElementType() {
		return elementType;
	}
}
