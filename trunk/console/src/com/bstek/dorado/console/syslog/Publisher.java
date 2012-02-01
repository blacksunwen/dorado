package com.bstek.dorado.console.syslog;

import java.util.ArrayList;
import java.util.List;

abstract class Publisher<T extends LogBuffer> {

	protected List<T> listeners = new ArrayList<T>();
	
	abstract protected T create();
	
	protected String listenerId(T t) {
		return Integer.toHexString(System.identityHashCode(t));
	}
	
	T find(String lid) {
		synchronized (listeners) {
			for (T listener: listeners) {
				String listenerId = listenerId(listener);
				if (listenerId.equals(lid)) {
					return listener;
				}
			}
		}
		
		return null;
	}
	
	void register(T listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	void unregister(T listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
	void publish(Event event) {
		synchronized (listeners) {
			if (listeners.isEmpty()) return;
			
			for (T listener: listeners) {
				try {
					listener.onPush(event);
				} catch (Throwable t) {
					this.onPushError(t, listener, event);
				}
			}
		}
	}
	
	protected void onPushError(Throwable t, T listener, Event event) {
		
	}
}
