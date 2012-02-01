package com.bstek.dorado.console.syslog;

public class ExpirablePublisher extends Publisher<ExpirableLogBuffer> {

	@Override
	void publish(Event event) {
		checkExpirables();
		super.publish(event);
	}
	
	public void checkExpirables() {
		synchronized (listeners) {
			if (listeners.isEmpty()) return;
			
			for (int i=0; i<listeners.size();) {
				ExpirableLogBuffer listener = listeners.get(i);
				if (listener.isExpired()) {
					listeners.remove(i);
				} else {
					i++;
				}
			}
		}
	}

	@Override
	protected ExpirableLogBuffer create() {
		return new ExpirableLogBuffer();
	}
	
}
