/**
 * 
 */
package com.bstek.dorado.core.io;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * 
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-3-22
 */
public class LocationTranformerRegister implements BeanFactoryPostProcessor {
	private String protocal;
	private LocationTransformer transformer;

	public void setProtocal(String protocal) {
		this.protocal = protocal;
	}

	public void setTransformer(LocationTransformer transformer) {
		this.transformer = transformer;
	}

	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		LocationTransformerHolder.getPathTransformers().put(protocal,
				transformer);
	}
}
