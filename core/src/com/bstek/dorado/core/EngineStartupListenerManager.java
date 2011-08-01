package com.bstek.dorado.core;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-27
 */
public abstract class EngineStartupListenerManager {
	private static final Log logger = LogFactory
			.getLog(EngineStartupListenerManager.class);

	private static final Collection<EngineStartupListener> listeners = new TreeSet<EngineStartupListener>(
			new Comparator<EngineStartupListener>() {
				public int compare(EngineStartupListener listener1,
						EngineStartupListener listener2) {
					if (listener1.getPriority() != listener2.getPriority()) {
						return listener1.getPriority()
								- listener2.getPriority();
					}
					else {
						return listener1.hashCode() - listener2.hashCode();
					}
				}
			});

	private EngineStartupListenerManager() {}

	public static void register(EngineStartupListener listener) {
		listeners.add(listener);
	}

	public static synchronized void notifyStartup() throws Exception {
		for (EngineStartupListener listener : listeners) {
			logger.info("Fire StartupListener [" + listener.getClass() + "].");
			listener.onStartup();
		}
		listeners.clear();
	}
}
