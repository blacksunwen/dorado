package com.bstek.dorado.vidorsupport.plugin.internal;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bstek.dorado.util.Assert;
import com.bstek.dorado.vidorsupport.plugin.iapi.INamed;
import com.bstek.dorado.vidorsupport.plugin.iapi.IRepository;

public abstract class AbstractRepository<T extends INamed> implements IRepository<T> {

	private Map<String, T> modelMap = this.createMap();
	
	
	public void register(T t) {
		modelMap.put(t.getName(), t);
	}

	
	public void deregister(T t) {
		modelMap.remove(t.getName());
	}
	
	@SuppressWarnings("unchecked")
	public <X extends T> X deregister(String name) {
		return (X)modelMap.remove(name);
	}

	public Collection<T> list() {
		return modelMap.values();
	}
	
	public Collection<String> names() {
		return modelMap.keySet();
	}

	@SuppressWarnings("unchecked")
	public <X extends T> X get(String name) {
		Assert.notEmpty(name, "value of name argument must not be empty.");
		X x = (X)modelMap.get(name);
		if (x == null) {
			throw new IllegalArgumentException(name);
		}
		return x;
	}

	public boolean has(String name) {
		return modelMap.containsKey(name);
	}

	public void empty() {
		modelMap = this.createMap();
	}

	public boolean isEmpty() {
		return modelMap.isEmpty();
	}

	public int size() {
		return modelMap.size();
	}

	protected Map<String, T> createMap() {
		return new LinkedHashMap<String, T>();
	}
}
