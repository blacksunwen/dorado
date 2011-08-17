/**
 * 
 */
package com.bstek.dorado.data.config.definition;

import org.springframework.beans.factory.InitializingBean;

import com.bstek.dorado.data.listener.GenericObjectListener;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-8
 */
public class GenericObjectListenerRegister implements InitializingBean {
	private GenericObjectListener<?> listener;

	public void setListener(GenericObjectListener<?> listener) {
		this.listener = listener;
	}

	public void afterPropertiesSet() throws Exception {
		if (listener != null) {
			GenericObjectListenerRegistry.addListener(listener);
		}
	}
}
