package com.bstek.dorado.console.syslog;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.web.DoradoContext;

public class SystemLogMonitor {

	private String listenerIdKey = "LISTENER_ID_KEY";
	
	@DataProvider
	public List<LogLine> last() {
		DoradoContext ctx = DoradoContext.getCurrent();
		String listenerId = (String)ctx.getAttribute(DoradoContext.VIEW, listenerIdKey);
		
		SystemLog sysLog = SystemLog.getInstance();
		synchronized (sysLog) {
			if (!sysLog.isOnWork()) {
				sysLog.startWork();
			}
		}
		
		ExpirablePublisher publisher = sysLog.getPublisher();
		
		ExpirableLogBuffer logBuffer = null;
		if (StringUtils.isNotEmpty(listenerId)) {
			logBuffer = publisher.find(listenerId);
		}
		
		if (logBuffer == null) {
			logBuffer = publisher.create();
			publisher.register(logBuffer);
		}
		
		if (StringUtils.isEmpty(listenerId)) {
			listenerId = publisher.listenerId(logBuffer);
			ctx.setAttribute(DoradoContext.VIEW, listenerIdKey, listenerId);
		}
		
		return logBuffer.getLastLines();
	}
	
}
