package com.bstek.dorado.console;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.core.EngineStartupListener;

/**
 * Dorado Console Starter
 * 
 * @author Alex Tong (mailto:alex.tong@bstek.com)
 * @since 2012-12-27
 */

public class Starter extends EngineStartupListener {
	private static Log logger = LogFactory.getLog(Starter.class);

	@Override
	public void onStartup() throws Exception {
		Setting.setStartTime(System.currentTimeMillis());
		logger.debug("dorado console starting...");
		Setting.getConsoleLogin();
		logger.debug("dorado console started");
	}

}
