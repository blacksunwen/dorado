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
package com.bstek.dorado.view;

/**
 * 视图功能相关的常量。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 22, 2008
 */
public abstract class Constants {
	private Constants() {}

	/**
	 * XML类型的视图配置文件。
	 */
	public final static String VIEW_CONFIG_TYPE_XML = "xml";

	/**
	 * 视图对象在{@link com.bstek.dorado.core.bean.ScopeManager}中进行管理时使用的名称前缀。
	 */
	public final static String SCOPE_VIEW_PREFIX = "View:";
}
