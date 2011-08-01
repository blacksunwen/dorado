package com.bstek.dorado.data.type;

import com.bstek.dorado.common.Namable;

/**
 * 原始的DataType。<br>
 * 此接口一般不提供给用户直接使用，主要用于方便内部代码对DataType的name、matchType等属性进行设置。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 17, 2008
 */
public interface RudeDataType extends DataType, Namable {
	/**
	 * 设置该DataType相匹配的Java类型。
	 */
	void setMatchType(Class<?> matchType);

	/**
	 * 设置该DataType相匹配的可实例化Java类型。
	 */
	void setCreationType(Class<?> creationType);
}
