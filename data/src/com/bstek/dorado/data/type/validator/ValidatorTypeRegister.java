package com.bstek.dorado.data.type.validator;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import com.bstek.dorado.config.Parser;
import com.bstek.dorado.data.config.xml.GenericObjectParser;

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
	private Parser parser;

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

	/**
	 * 返回对应的配置解析器。
	 */
	public Parser getParser() {
		return parser;
	}

	/**
	 * 设置对应的配置解析器。
	 */
	public void setParser(Parser parser) {
		this.parser = parser;
	}

	public void afterPropertiesSet() throws Exception {
		try {
			Class<?> cl = ClassUtils.getClass(classType);
			ValidatorTypeRegisterInfo registerInfo = new ValidatorTypeRegisterInfo(
					type, cl);
			if (parser instanceof GenericObjectParser) {
				((GenericObjectParser) parser).setDefaultImpl(classType);
			}
			registerInfo.setParser(parser);
			validatorTypeRegistry.registerType(registerInfo);
		} catch (ClassNotFoundException e) {
			logger.equals(e);
		}
	}
}
