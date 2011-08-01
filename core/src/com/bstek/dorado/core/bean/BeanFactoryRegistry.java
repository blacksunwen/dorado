package com.bstek.dorado.core.bean;

/**
 * Bean工厂的注册管理器接口。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Dec 26, 2007
 */
public interface BeanFactoryRegistry {

	/**
	 * 注册一种的Bean工厂。
	 * @param beanFactory 要注册的Bean工厂
	 */
	void registerBeanFactory(BeanFactory beanFactory);

	/**
	 * 根据Bean的描述信息返回相应的Bean工厂。
	 * @param beanName Bean的描述信息
	 * @return 相应的Bean工厂
	 */
	BeanFactory getBeanFactory(String beanName);

}