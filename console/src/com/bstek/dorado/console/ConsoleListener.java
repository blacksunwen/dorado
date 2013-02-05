package com.bstek.dorado.console;

import javax.servlet.ServletContextEvent;

import com.bstek.dorado.console.system.log.console.SystemOutTailWork;
import com.bstek.dorado.web.listener.DelegatingServletContextListener;

/**
 * Dorado Console Listener 启动控制台监听
 * 
 * @author Alex Tong (mailto:alex.tong@bstek.com)
 * @since 2012-12-07
 */
public class ConsoleListener extends DelegatingServletContextListener {

	@Override
	public int getOrder() {
		return 0;
	}

	public void contextDestroyed(ServletContextEvent event) {
	}

	public void contextInitialized(ServletContextEvent event) {
		SystemOutTailWork outTailWork = SystemOutTailWork.getInstance();
		outTailWork.startWork();
		Setting.setListenerActiveState(true);
	}

}
