package com.bstek.dorado.console.syslog;


public abstract class AbstractWork {

	private ExpirablePublisher publisher = new ExpirablePublisher();
	
	public ExpirablePublisher getPublisher() {
		return publisher;
	}
	
	private boolean onwork = false;
	
	synchronized
	public boolean isOnWork() {
		return onwork;
	}
	
	synchronized
	public void startWork() {
		if (!onwork) {
			doStartWork();
			onwork = true;
		}
	}
	
	synchronized
	public void stopWork() {
		if (onwork) {
			doStopWork();
			onwork = false;
		}
	}
	
	protected abstract void doStartWork();
	
	protected abstract void doStopWork();
}
