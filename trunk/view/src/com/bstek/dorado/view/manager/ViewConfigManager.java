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
package com.bstek.dorado.view.manager;

/**
 * 视图管理器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 20, 2008
 */
public interface ViewConfigManager {

	/**
	 * 
	 * @param viewNamePattern
	 * @param viewConfigFactory
	 */
	void registerViewConfigFactory(String viewNamePattern,
			Object viewConfigFactory);

	/**
	 * @param viewName
	 * @return
	 */
	Object getViewConfigFactory(String viewName);

	/**
	 * 根据视图的名称返回相应的视图对象。
	 * 
	 * @param viewName
	 *            视图的名称。
	 * @return 视图对象。
	 * @throws Exception
	 */
	ViewConfig getViewConfig(String viewName) throws Exception;
}
