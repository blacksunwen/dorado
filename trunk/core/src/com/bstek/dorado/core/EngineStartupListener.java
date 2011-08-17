package com.bstek.dorado.core;

import org.springframework.beans.factory.InitializingBean;

/**
 * Dorado引擎的启动过程监听器。
 * <p>
 * 在目前的默认实现方式中，只要将EngineStartupListener的实现类注册到Spring的配置中，
 * Dorado引擎就会在启动之后自动的激活所有的EngineStartupListener的实现类。
 * </p>
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 9, 2007
 */
public abstract class EngineStartupListener implements InitializingBean {
	private int order = 999;

	/**
	 * 当Dorado引擎被启动时触发的动作。
	 * 
	 * @throws Exception
	 */
	public abstract void onStartup() throws Exception;

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void afterPropertiesSet() throws Exception {
		EngineStartupListenerManager.register(this);
	}
}
