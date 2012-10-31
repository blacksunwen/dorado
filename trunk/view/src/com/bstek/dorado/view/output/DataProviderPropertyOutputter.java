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
package com.bstek.dorado.view.output;

import com.bstek.dorado.data.provider.DataProvider;

/**
 * 数据提供者的输出器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Oct 14, 2008
 */
public class DataProviderPropertyOutputter extends ObjectPropertyOutputter {

	@Override
	public void output(Object object, OutputContext context) throws Exception {
		DataProvider dataProvider = (DataProvider) object;
		JsonBuilder json = context.getJsonBuilder();
		json.beginValue();
		context.getWriter().write(
				"dorado.DataProvider.create(\"" + dataProvider.getId() + "\")");
		json.endValue();
	}

}
