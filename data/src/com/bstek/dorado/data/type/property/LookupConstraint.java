package com.bstek.dorado.data.type.property;

import org.apache.commons.lang.ObjectUtils;

import com.bstek.dorado.core.el.Expression;

/**
 * 执行数据参照时的约束条件。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Dec 17, 2007
 */
public class LookupConstraint {
	private String lookupKeyProperty;
	private String keyProperty;
	private Object keyValue;

	/**
	 * 返回约束条件对应的属性名。
	 */
	public String getLookupKeyProperty() {
		return lookupKeyProperty;
	}

	public void setLookupKeyProperty(String lookupKeyProperty) {
		this.lookupKeyProperty = lookupKeyProperty;
	}

	/**
	 * 返回用于从当前数据实体中提取约束值时使用的属性名。
	 */
	public String getKeyProperty() {
		return keyProperty;
	}

	/**
	 * 设置用于从当前数据实体中提取约束值时使用的属性名。
	 */
	public void setKeyProperty(String keyProperty) {
		this.keyProperty = keyProperty;
	}

	/**
	 * 返回约束值。
	 */
	public Object getKeyValue() {
		if (keyValue instanceof Expression) {
			return ((Expression) keyValue).evaluate();
		} else {
			return keyValue;
		}
	}

	/**
	 * 设置约束值。
	 */
	public void setKeyValue(Object keyValue) {
		this.keyValue = keyValue;
	}

	@Override
	public int hashCode() {
		int hashCode = lookupKeyProperty.hashCode() >> 4;
		if (keyValue != null) {
			hashCode += keyValue.hashCode();
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LookupConstraint) {
			if (lookupKeyProperty
					.equals(((LookupConstraint) obj).lookupKeyProperty)) {
				return ObjectUtils.equals(keyValue,
						((LookupConstraint) obj).keyValue);
			}
		}
		return false;
	}
}
