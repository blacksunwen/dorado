/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.view.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2014-1-25
 */
public abstract class LongTask implements Callable<Object> {
	private List<TaskMessageListener> taskMessageListeners;
	private TaskStateInfo stateInfo = new TaskStateInfo(TaskState.waiting);
	private boolean continueExecution = true;

	public synchronized void addMessageListener(TaskMessageListener listener) {
		if (taskMessageListeners == null) {
			taskMessageListeners = new ArrayList<TaskMessageListener>();
		}
		taskMessageListeners.add(listener);
	}

	public synchronized void removeMessageListener(TaskMessageListener listener) {
		if (taskMessageListeners != null) {
			taskMessageListeners.remove(listener);
		}
	}

	protected void fireStateChange(TaskStateInfo state) {
		if (taskMessageListeners != null) {
			for (TaskMessageListener listener : taskMessageListeners
					.toArray(new TaskMessageListener[0])) {
				listener.onStateChange(this, state);
			}
		}
	}

	protected void fireLog(TaskLog log) {
		if (taskMessageListeners != null) {
			for (TaskMessageListener listener : taskMessageListeners
					.toArray(new TaskMessageListener[0])) {
				listener.onLogAppend(this, log);
			}
		}
	}

	public TaskStateInfo getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(TaskStateInfo stateInfo) {
		this.stateInfo = stateInfo;
		fireStateChange(stateInfo);
	}

	public void appendLog(TaskLog log) {
		fireLog(log);
	}

	public boolean shouldContinue() {
		return continueExecution;
	}

	public void abort() {
		setStateInfo(new TaskStateInfo(TaskState.aborting));
		continueExecution = false;
	}
}
