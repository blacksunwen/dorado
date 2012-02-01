package com.bstek.dorado.console.syslog;

import java.util.EventObject;

class Event extends EventObject {

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
