package com.bstek.dorado.data.type;

import java.util.Map;

import com.bstek.dorado.common.event.ClientEventSupported;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.data.type.validator.MessageState;

/**
 * 实体类型的通用接口。
 * <p>
 * 实体类型一般包含java.util.Map、java.util.Properties、Bean这几种， 他们的共同点是具有属性或键值的概念。<br>
 * 实体类型可以通过{@link #getPropertyDefs()}中的属性定义对象来对其中的部分属性的操作方式进行定义。
 * </p>
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 13, 2007
 */
public interface EntityDataType extends MutableDataType, ClientEventSupported {
	/**
	 * 返回属性声明的集合。
	 */
	Map<String, PropertyDef> getPropertyDefs();

	/**
	 * 向属性集合中添加一个属性。
	 * 
	 * @param propertyDef
	 *            属性声明对象。
	 * @return 属性声明对象。
	 */
	PropertyDef addPropertyDef(PropertyDef propertyDef);

	/**
	 * 根据名称返回一个属性声明对象。
	 * 
	 * @param propertyName
	 *            属性名。
	 * @return 属性声明对象。
	 */
	PropertyDef getPropertyDef(String propertyName);

	boolean isAcceptUnknownProperty();

	void setAcceptUnknownProperty(boolean acceptUnknownProperty);

	boolean isAutoCreatePropertyDefs();

	void setAutoCreatePropertyDefs(boolean autoCreatePropertyDefs);

	MessageState getAcceptValidationState();

	void setAcceptValidationState(MessageState acceptValidationState);

	void createPropertyDefinitons() throws Exception;

	String getDefaultDisplayProperty();

	void setDefaultDisplayProperty(String defaultDisplayProperty);
}
