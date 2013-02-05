package com.bstek.dorado.console;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.console.login.ConsoleLogin;
import com.bstek.dorado.core.Context;

/**
 * Dorado Console Setting
 * 
 * @author Alex Tong (mailto:alex.tong@bstek.com)
 * @since 2012-12-27
 */
public final class Setting {
	private static Log logger = LogFactory.getLog(Setting.class);

	private static ConsoleLogin consoleLogin;
	private static long startTime;
	private static boolean listenerActiveState;

	public static ConsoleLogin getConsoleLogin() {
		if (consoleLogin == null) {
			Context ctx = Context.getCurrent();
			ConsoleLogin configed = null;
			try {
				configed = (ConsoleLogin) ctx.getServiceBean("consoleLogin");
				consoleLogin = configed;
			} catch (Exception e) {
				logger.error(e, e);
			}
		}
		return consoleLogin;
	}

	public static long getStartTime() {
		return startTime;
	}

	public static void setStartTime(long startTime) {
		Setting.startTime = startTime;
	}

	public static boolean getListenerActiveState() {
		return listenerActiveState;
	}

	public static void setListenerActiveState(boolean listenerActiveState) {
		Setting.listenerActiveState = listenerActiveState;
	}

	public static void setConsoleLogin(ConsoleLogin consoleLogin) {
		Setting.consoleLogin = consoleLogin;
	}

}
