package com.bstek.dorado.console.syslog;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

class LogBuffer implements EventListener {

	private List<LogLine> lines = new ArrayList<LogLine>(100);
	
	synchronized
	public void onPush(Event event) {
		LogLine line = event.getObject();
		lines.add(line);
	}
	
	synchronized
	public boolean isEmpty() {
		return lines.isEmpty();
	}
	
	synchronized
	public List<LogLine> getLastLines() {
		if (lines.isEmpty()) {
			return null;
		} else {
			List<LogLine> last = lines;
			lines = new ArrayList<LogLine>(100);
			return last;
		}
	}

}
