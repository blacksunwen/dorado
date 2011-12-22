package com.bstek.dorado.view.registry;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.Component;

/**
 * 用于配置在Spring文件中的组件类型信息的注册器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 22, 2008
 */
public abstract class ComponentTypeRegister implements InitializingBean,
		BeanFactoryAware, BeanNameAware {
	private static final Log logger = LogFactory
			.getLog(ComponentTypeRegister.class);

	private BeanFactory beanFactory;
	private String beanName;
	private String name;
	private String classType;
	private String category;
	private ComponentTypeRegistry componentTypeRegistry;

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getBeanName() {
		return beanName;
	}

	/**
	 * 设置组件类型注册管理器。
	 */
	public void setComponentTypeRegistry(
			ComponentTypeRegistry componentTypeRegistry) {
		this.componentTypeRegistry = componentTypeRegistry;
	}

	public ComponentTypeRegistry getComponentTypeRegistry() {
		return componentTypeRegistry;
	}

	/**
	 * 设置组件名称。
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * 设置组件的Class类型。
	 */
	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getClassType() {
		return classType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	protected abstract ComponentTypeRegisterInfo createRegisterInfo(String name);

	@SuppressWarnings("unchecked")
	protected ComponentTypeRegisterInfo getRegisterInfo() throws Exception {
		if (StringUtils.isEmpty(name)) {
			int i = beanName.lastIndexOf(".");
			if (i >= 0)
				name = beanName.substring(i + 1);
		}

		Class<? extends Component> cl = null;
		if (StringUtils.isNotEmpty(classType)) {
			cl = ClassUtils.getClass(classType);
		}

		Widget widget = null;
		if (cl != null) {
			widget = cl.getAnnotation(Widget.class);
			// 此段逻辑带来不便，当子控件中没有定义@Widget时，会自动继承父类的@Widget，导致父控件被覆盖。
			// 屏蔽此代码后，@Widget.name()事实上已成为冗余属性。
			// if (widget != null && StringUtils.isEmpty(name)
			// && StringUtils.isNotEmpty(widget.name())) {
			// name = widget.name();
			// }
		}

		ComponentTypeRegisterInfo registerInfo = createRegisterInfo(name);
		registerInfo.setClassType(cl);
		if (widget != null) {
			registerInfo.setCategory(widget.category());
		}
		return registerInfo;
	}

	public void afterPropertiesSet() throws Exception {
		try {
			ComponentTypeRegisterInfo registerInfo = getRegisterInfo();
			componentTypeRegistry.registerType(registerInfo);
		} catch (Exception e) {
			logger.warn(e, e);
		}
	}
}
