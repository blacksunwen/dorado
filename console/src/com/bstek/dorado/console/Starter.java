package com.bstek.dorado.console;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.console.syslog.SystemLog;
import com.bstek.dorado.core.EngineStartupListener;

public class Starter extends EngineStartupListener {

	private static Log logger = LogFactory.getLog(Starter.class);
	
	@Override
	public void onStartup() throws Exception {
		logger.info("dorado console starting...");
		
		SystemLog.getInstance().startWork();
		ConsoleUtils.getConsoleLogin();
		
		logger.info("dorado console started");
	}

}
