package com.bstek.dorado.jdbc.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.KeyGeneratorManager;
import com.bstek.dorado.jdbc.ide.TableKeyColumnRuleTemplateInitializer;
import com.bstek.dorado.jdbc.model.table.KeyGenerator;
import com.bstek.dorado.util.Assert;

public class DefaultKeyGeneratorManager implements KeyGeneratorManager{

	@SuppressWarnings("rawtypes")
	private Map<String, KeyGenerator> elements = new HashMap<String, KeyGenerator>();
	
	@SuppressWarnings("rawtypes")
	public void register(KeyGenerator keyGenerator) {
		elements.put(keyGenerator.getName(), keyGenerator);
	}

	@SuppressWarnings("unchecked")
	public KeyGenerator<Object>[] list() {
		return elements.values().toArray(new KeyGenerator[0]);
	}

	@SuppressWarnings("unchecked")
	public <T>KeyGenerator<T> get(String name) {
		KeyGenerator<T> jdbcType = (KeyGenerator<T>)elements.get(name);
		Assert.notNull(jdbcType, "no any KeyGenerator named [" + name + "]");
		
		return jdbcType;
	}

	public boolean has(String name) {
		return elements.containsKey(name);
	}
	
	@Override
	public String toString() {
		String msg = "Registered KeyGenerators: " + TableKeyColumnRuleTemplateInitializer.KEY_GENERATORS_STORE_KEY+" [";
		KeyGenerator<Object>[] types = list();
		List<String> names = new ArrayList<String>();
		for (KeyGenerator<Object> type: types) {
			names.add(type.getName());
		}
		
		msg += StringUtils.join(names, ',');
		msg += "]";
		
		return msg;
	}
	
}
