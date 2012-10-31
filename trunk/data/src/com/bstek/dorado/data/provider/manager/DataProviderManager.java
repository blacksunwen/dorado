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
package com.bstek.dorado.data.provider.manager;

import com.bstek.dorado.data.config.definition.DataProviderDefinitionManager;
import com.bstek.dorado.data.provider.DataProvider;

/**
 * DataProvider管理类的通用接口。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 8, 2007
 * @see com.bstek.dorado.data.provider.DataProvider
 * @see com.bstek.dorado.data.config.definition.DataProviderDefinitionManager
 */
public interface DataProviderManager {
	/**
	 * 返回DataProvider配置声明管理器。
	 */
	DataProviderDefinitionManager getDataProviderDefinitionManager();

	/**
	 * 根据名称返回一个DataProvider。
	 * 
	 * @param name
	 *            DataProvider的名称。
	 * @throws Exception
	 */
	DataProvider getDataProvider(String name) throws Exception;

}
