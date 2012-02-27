package com.bstek.dorado.jdbc.config;

import java.util.Collection;

import com.bstek.dorado.config.definition.DefaultDefinitionManager;

public abstract class AbstractDbmManager extends DefaultDefinitionManager<DbElementDefinition> implements DbmManager {

	private GlobalDbModelConfig[] configs = null;

	public GlobalDbModelConfig[] getConfigs() {
		return configs;
	}

	public void setConfigs(GlobalDbModelConfig[] configs) {
		this.configs = configs;
	}

	@Override
	public void register(DbModel dbm) {
		Collection<DbElementDefinition> defs = dbm.listAll();
		for (DbElementDefinition def: defs) {
			this.register(def);
		}
	}

	protected void register(DbElementDefinition def) {
		this.registerDefinition(def.getName(), def);
	}

	@Override
	public void refresh(GlobalDbModelConfig[] configs) {
		this.setConfigs(configs);
		try {
			this.doRefresh();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	abstract protected void doRefresh() throws Exception;
}
