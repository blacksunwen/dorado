/**
 * 
 */
package com.bstek.dorado.data.config.definition;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.bstek.dorado.data.listener.GenericObjectListener;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-8
 */
public class GenericObjectListenerRegister implements BeanFactoryPostProcessor {
	private GenericObjectListener<?> listener;

	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		if (listener != null) {
			GenericObjectListenerRegistry.addListener(listener);
		}
	}

	public void setListener(GenericObjectListener<?> listener) {
		this.listener = listener;
	}

}
