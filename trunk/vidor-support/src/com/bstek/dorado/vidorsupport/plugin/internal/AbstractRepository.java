package com.bstek.dorado.vidorsupport.plugin.internal;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bstek.dorado.util.Assert;
import com.bstek.dorado.vidorsupport.plugin.iapi.INamed;
import com.bstek.dorado.vidorsupport.plugin.iapi.IRepository;

public abstract class AbstractRepository<T extends INamed> implements IRepository<T> {

	private Map<String, T> modelMap = this.createMap();
	
	@Override
	public void register(T t) {
		modelMap.put(t.getName(), t);
	}

	@Override
	public void deregister(T t) {
		modelMap.remove(t.getName());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <X extends T> X deregister(String name) {
		return (X)modelMap.remove(name);
	}

	@Override
	public Collection<T> list() {
		return modelMap.values();
	}
	
	@Override
	public Collection<String> names() {
		return modelMap.keySet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X extends T> X get(String name) {
		Assert.notEmpty(name, "value of name argument must not be empty.");
		X x = (X)modelMap.get(name);
		if (x == null) {
			throw new IllegalArgumentException(name);
		}
		return x;
	}

	@Override
	public boolean has(String name) {
		return modelMap.containsKey(name);
	}

	@Override
	public void empty() {
		modelMap = this.createMap();
	}

	@Override
	public boolean isEmpty() {
		return modelMap.isEmpty();
	}

	@Override
	public int size() {
		return modelMap.size();
	}

	protected Map<String, T> createMap() {
		return new LinkedHashMap<String, T>();
	}
}
