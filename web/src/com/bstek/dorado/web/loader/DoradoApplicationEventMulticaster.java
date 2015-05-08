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

package com.bstek.dorado.web.loader;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2015-5-8
 */
public class DoradoApplicationEventMulticaster extends
		SimpleApplicationEventMulticaster {
	@SuppressWarnings("rawtypes")
	public DoradoApplicationEventMulticaster() {
		addApplicationListener(new ApplicationListener() {
			public void onApplicationEvent(ApplicationEvent event) {
				if (event instanceof ContextClosedEvent) {
					DoradoLoader.getInstance().destroy();
				}
			}
		});
	}
}
