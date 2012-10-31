/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
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
					if (listener1.getOrder() != listener2.getOrder()) {
						return listener1.getOrder() - listener2.getOrder();
					} else {
						return listener1.hashCode() - listener2.hashCode();
					}
				}
			});

	private EngineStartupListenerManager() {
	}

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
