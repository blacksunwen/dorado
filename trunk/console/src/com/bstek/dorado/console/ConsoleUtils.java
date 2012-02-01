package com.bstek.dorado.console;

import com.bstek.dorado.core.Context;

public final class ConsoleUtils {

	private static ConsoleLogin consoleLogin;
	
	public static ConsoleLogin getConsoleLogin(){
		if (consoleLogin == null) {
			Context ctx = Context.getCurrent();
			ConsoleLogin configed = null;
			try {
				configed = (ConsoleLogin)ctx.getServiceBean("consoleLogin");
				consoleLogin = configed;
			} catch (Exception e) {
				//logger.error("", e);
			}
			
			if (consoleLogin == null) {
				consoleLogin = new DefaultConsoleLogin();
			}
		}
		return consoleLogin;
	}
	
}
