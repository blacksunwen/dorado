/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.data.provider;

import java.lang.reflect.Method;
import java.util.Collection;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.type.DataType;

public class DataProviderGetResultMethodInterceptor implements
		MethodInterceptor {
	public static final String METHOD_NAME = "getResult";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Method method = methodInvocation.getMethod();
		if (method.getName().equals(METHOD_NAME)) {
			Object[] arguments = methodInvocation.getArguments();
			DataProvider dataProvider = (DataProvider) methodInvocation
					.getThis();
			DataType resultDataType = null;
			if (method.getReturnType().equals(Object.class)) {
				if (arguments.length == 2) {
					resultDataType = (DataType) arguments[1];
				}
				if (resultDataType == null) {
					resultDataType = dataProvider.getResultDataType();
				}
				Object result = methodInvocation.proceed();
				if (result != null) {
					result = EntityUtils.toEntity(result, resultDataType);
				}
				return result;
			} else {
				Page page = null;
				if (arguments.length == 1) {
					page = (Page) arguments[0];
				} else if (arguments.length == 2) {
					page = (Page) arguments[1];
				} else if (arguments.length == 3) {
					page = (Page) arguments[1];
					resultDataType = (DataType) arguments[2];
				}
				if (resultDataType == null) {
					resultDataType = dataProvider.getResultDataType();
				}
				Object returnValue = methodInvocation.proceed();
				if (page != null) {
					Collection entities = page.getEntities();
					if (entities != null) {
						entities = (Collection) EntityUtils.toEntity(entities,
								resultDataType);
						page.setEntities(entities);
					}
				}
				return returnValue;
			}
		} else
			return methodInvocation.proceed();
	}

}
