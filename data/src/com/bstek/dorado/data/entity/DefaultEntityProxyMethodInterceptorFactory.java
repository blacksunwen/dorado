/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.data.entity;

import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;

import com.bstek.dorado.data.type.EntityDataType;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-20
 */
public class DefaultEntityProxyMethodInterceptorFactory implements
		EntityProxyMethodInterceptorFactory {

	public MethodInterceptor[] createInterceptors(EntityDataType dataType,
			Class<?> classType, Object entity) throws Exception {
		MethodInterceptor mi;
		if (Map.class.isAssignableFrom(classType)) {
			mi = new MapEntityInterceptor(dataType);
		} else {
			mi = new BeanEntityInterceptor(dataType, classType);
		}
		return new MethodInterceptor[] { mi };
	}

}
