package com.bstek.dorado.console.system.log;

/**
 * @author Alex tong (mailto:alex.tong@bstek.com)
 * @since 2012-11-22
 */
public abstract class AbstractWork {

	private ExpirablePublisher publisher = new ExpirablePublisher();
	
	public ExpirablePublisher getPublisher() {
		return publisher;
	}
	
	private boolean onwork = false;
	/**
	 * 任务是否在执行状态
	 * @return
	 */
	synchronized
	public boolean isOnWork() {
		return onwork;
	}
	/**
	 * 开始执行任务
	 */
	synchronized
	public void startWork() {
		if (!onwork) {
			doStartWork();
			onwork = true;
		}
	}
	/**
	 * 停止任务
	 */
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
