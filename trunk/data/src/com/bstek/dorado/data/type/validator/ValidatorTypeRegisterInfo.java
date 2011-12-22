package com.bstek.dorado.data.type.validator;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-27
 */
public class ValidatorTypeRegisterInfo {
	private String type;
	private Class<?> classType;

	/**
	 * @param type
	 *            Validator的类型名
	 * @param classType
	 *            Validator的Class类型
	 */
	public ValidatorTypeRegisterInfo(String type, Class<?> classType) {
		this.type = type;
		this.classType = classType;
	}

	public String getType() {
		return type;
	}

	public Class<?> getClassType() {
		return classType;
	}

}
