package com.bstek.dorado.data.entity;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-19
 */
public interface EnhanceableEntity {

	void setEntityEnhancer(EntityEnhancer entityEnhancer);

	EntityEnhancer getEntityEnhancer();

	Object internalReadProperty(String property) throws Exception;

	void internalWriteProperty(String property, Object value) throws Exception;
}
