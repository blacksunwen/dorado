package com.bstek.dorado.core.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认的Bean工厂的注册管理器实现类。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Dec 26, 2007
 */
public class DefaultBeanFactoryRegistry implements BeanFactoryRegistry {
	private String defaultPrefix;
	private Map<String, BeanFactory> beanFactoryMap = new HashMap<String, BeanFactory>();

	/**
	 * 设置包含Bean工厂注册信息的Map。
	 */
	public void setBeanFactories(List<BeanFactory> beanFactories) {
		this.beanFactoryMap.clear();
		for (BeanFactory factory : beanFactories) {
			registerBeanFactory(factory);
		}
	}

	/**
	 * 返回默认的Bean描述前缀。
	 */
	public String getDefaultPrefix() {
		return defaultPrefix;
	}

	/**
	 * 设置默认的Bean描述前缀。<br>
	 * 即当Bean的描述信息并不包含任何前缀信息时，将采用此属性设定的值来决定决定的处理方式。
	 */
	public void setDefaultPrefix(String defaultPrefix) {
		this.defaultPrefix = defaultPrefix;
	}

	public void registerBeanFactory(BeanFactory beanFactory) {
		beanFactoryMap.put(beanFactory.getBeanNamePrefix(), beanFactory);
	}

	public BeanFactory getBeanFactory(String prefix) {
		if (prefix == null) {
			prefix = defaultPrefix;
		}
		return beanFactoryMap.get(prefix);
	}
}
