package com.bstek.dorado.jdbc.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.JdbcTypeManager;
import com.bstek.dorado.jdbc.ide.AbstractDbColumnRuleTemplateInitializer;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.util.Assert;

public class DefaultJdbcTypeManager implements JdbcTypeManager {

	private Map<String, JdbcType> types = new HashMap<String, JdbcType>();
	
	@Override
	public void register(JdbcType jdbcType) {
		types.put(jdbcType.getName(), jdbcType);
	}

	@Override
	public JdbcType[] list() {
		return types.values().toArray(new JdbcType[0]);
	}

	@Override
	public JdbcType get(String name) {
		JdbcType jdbcType = types.get(name);
		Assert.notNull(jdbcType, "no any JdbcType named [" + name + "]");
		
		return jdbcType;
	}

	@Override
	public boolean has(String name) {
		return types.containsKey(name);
	}

	@Override
	public String toString() {
		String msg = "Registered JdbcTypes: "+ AbstractDbColumnRuleTemplateInitializer.DATA_TYPES_STORE_KEY +" [";
		JdbcType[] types = list();
		List<String> names = new ArrayList<String>();
		for (JdbcType type: types) {
			names.add(type.getName());
		}
		
		msg += StringUtils.join(names, ',');
		msg += "]";
		
		return msg;
	}
	
}
