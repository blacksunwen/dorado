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
