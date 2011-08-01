package com.bstek.dorado.view.registry;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.config.Parser;
import com.bstek.dorado.view.widget.Component;

/**
 * 组件类型注册信息。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 21, 2008
 */
public abstract class ComponentTypeRegisterInfo {
	private String name;
	private Class<? extends Component> classType;
	private String category;
	private Parser parser;

	public ComponentTypeRegisterInfo(String name) {
		this.name = name;
	}

	/**
	 * 返回组件名。
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param classType
	 */
	public void setClassType(Class<? extends Component> classType) {
		this.classType = classType;
		if (StringUtils.isEmpty(name)) {
			name = ClassUtils.getShortClassName(classType);
		}
	}

	/**
	 * 返回组件的Class类型。
	 */
	public Class<? extends Component> getClassType() {
		return classType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * 返回该种组件使用的配置信息解析器。
	 */
	public Parser getParser() {
		return parser;
	}

	/**
	 * 设置该种组件使用的配置信息解析器。
	 */
	public void setParser(Parser parser) {
		this.parser = parser;
	}
}
