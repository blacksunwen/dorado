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
package com.bstek.dorado.data.type.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import com.bstek.dorado.util.clazz.ClassUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-27
 */
public class ValidatorTypeRegister implements InitializingBean {
	private static final Log logger = LogFactory
			.getLog(ValidatorTypeRegister.class);

	private ValidatorTypeRegistry validatorTypeRegistry;
	private String type;
	private String classType;

	public void setValidatorTypeRegistry(
			ValidatorTypeRegistry validatorTypeRegistry) {
		this.validatorTypeRegistry = validatorTypeRegistry;
	}

	/**
	 * 返回类型名。
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置类型名。
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 返回Validator的Class类型。
	 */
	public String getClassType() {
		return classType;
	}

	/**
	 * 设置Validator的Class类型。
	 */
	public void setClassType(String classType) {
		this.classType = classType;
	}

	public void afterPropertiesSet() throws Exception {
		try {
			Class<?> cl = ClassUtils.forName(classType);
			ValidatorTypeRegisterInfo registerInfo = new ValidatorTypeRegisterInfo(
					type, cl);
			validatorTypeRegistry.registerType(registerInfo);
		} catch (ClassNotFoundException e) {
			logger.equals(e);
		}
	}
}
