package com.bstek.dorado.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * Dorado引擎的启动过程监听器。
 * <p>
 * 在目前的默认实现方式中，只要将EngineStartupListener的实现类注册到Spring的配置中，
 * Dorado引擎就会在启动之后自动的激活所有的EngineStartupListener的实现类。
 * </p>
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 9, 2007
 */
public abstract class EngineStartupListener implements BeanFactoryPostProcessor {
	private int priority = 100;

	/**
	 * 当Dorado引擎被启动时触发的动作。
	 * @throws Exception
	 */
	public abstract void onStartup() throws Exception;

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		EngineStartupListenerManager.register(this);
	}
}
