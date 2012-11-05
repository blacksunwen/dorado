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

package com.bstek.dorado.view.widget;

import java.util.EventListener;

/**
 * 容器事件监听器的接口。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 3, 2008
 */
public interface ContainerListener extends EventListener {
	/**
	 * “添加子控件”的事件。
	 * @param event 事件描述对象
	 */
	void childAdded(ContainerEvent event);

	/**
	 * “移除子控件”的事件。
	 * @param event 事件描述对象
	 */
	void childRemoved(ContainerEvent event);
}
