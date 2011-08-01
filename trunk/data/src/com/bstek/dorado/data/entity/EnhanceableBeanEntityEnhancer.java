package com.bstek.dorado.data.entity;

import com.bstek.dorado.data.type.EntityDataType;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-20
 */
public class EnhanceableBeanEntityEnhancer extends AbstractBeanEntityEnhancer {

	public EnhanceableBeanEntityEnhancer(EntityDataType dataType,
			Class<?> beanType) throws Exception {
		super(dataType, beanType);
	}

}
