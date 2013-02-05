package com.bstek.dorado.console.system.log;

import java.util.EventObject;

public class Event extends EventObject {

	private static final long serialVersionUID = 2592537553806095543L;

	private LogLine syslog;
	
	public Event(Object monitor, LogLine syslog) {
		super(monitor);
		this.syslog = syslog;
	}
	
	public LogLine getObject() {
		return syslog;
	}

}
