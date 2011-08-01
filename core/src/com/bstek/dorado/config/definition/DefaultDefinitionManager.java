package com.bstek.dorado.config.definition;

import java.util.Hashtable;
import java.util.Map;

/**
 * 默认的配置声明对象管理器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 7, 2008
 */
public class DefaultDefinitionManager<T extends Definition> implements
		DefinitionManager<T> {
	private Map<String, T> definitions = new Hashtable<String, T>();
	private DefinitionManager<T> parent;

	public DefaultDefinitionManager() {
	}

	public DefaultDefinitionManager(DefinitionManager<T> parent) {
		this.parent = parent;
	}

	public DefinitionManager<T> getParent() {
		return parent;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void registerDefinition(String name, T definition) {
		if (definition instanceof DefinitionManagerAware) {
			((DefinitionManagerAware) definition).setDefinitionManager(this);
		}
		definitions.put(name, definition);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T unregisterDefinition(String name) {
		T definition = definitions.remove(name);
		if (definition instanceof DefinitionManagerAware) {
			((DefinitionManagerAware) definition).setDefinitionManager(this);
		}
		return definition;
	}

	public T getDefinition(String name) {
		T definition = definitions.get(name);
		if (definition == null && parent != null) {
			definition = parent.getDefinition(name);
		}
		return definition;
	}

	public Map<String, T> getDefinitions() {
		return definitions;
	}

	public void clearAllDefinitions() {
		definitions.clear();
	}

}
