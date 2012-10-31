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
package com.bstek.dorado.view.config.definition;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Context;

public class AssembledComponentExpressionObject {
	private static final String MAX_ID_KEY = AssembledComponentExpressionObject.class
			.getName() + ".maxId";

	private Map<String, Object> properties;
	private Map<String, String> idMap = new HashMap<String, String>();

	public AssembledComponentExpressionObject(Map<String, Object> properties) {
		this.properties = properties;
	}

	public Object prop(String property) {
		return (properties != null) ? properties.get(property) : null;
	}

	void setRealId(String id, String realId) {
		idMap.put(id, realId);
	}

	public String id(String id) {
		String realId = idMap.get(id);
		if (StringUtils.isNotEmpty(realId)) {
			return realId;
		}

		Context context = Context.getCurrent();
		Integer maxId = (Integer) context.getAttribute(MAX_ID_KEY);
		int sn = 0;
		if (maxId != null) {
			sn = maxId.intValue();
		}
		sn++;
		realId = id + '_' + sn;
		context.setAttribute(MAX_ID_KEY, new Integer(sn));
		idMap.put(id, realId);
		return realId;
	}
}
