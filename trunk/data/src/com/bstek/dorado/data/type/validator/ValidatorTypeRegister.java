package com.bstek.dorado.data.type.validator;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

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
			Class<?> cl = ClassUtils.getClass(classType);
			ValidatorTypeRegisterInfo registerInfo = new ValidatorTypeRegisterInfo(
					type, cl);
			validatorTypeRegistry.registerType(registerInfo);
		} catch (ClassNotFoundException e) {
			logger.equals(e);
		}
	}
}
