package com.bstek.dorado.jdbc.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.config.definition.DefaultDefinitionManager;

public abstract class AbstractDbmDefinitionManager extends DefaultDefinitionManager<DbElementDefinition> implements DbmDefinitionManager {
	private List<JdbcConfigLoader> configs = new ArrayList<JdbcConfigLoader>();

	private JdbcEnviromentManager enviromentManager;
	
	public JdbcEnviromentManager getEnviromentManager() {
		return enviromentManager;
	}

	public void setEnviromentManager(JdbcEnviromentManager enviromentManager) {
		this.enviromentManager = enviromentManager;
	}
	
	public JdbcConfigLoader[] getConfigs() {
		return configs.toArray(new JdbcConfigLoader[0]);
	}

	public void register(DbModel dbm) {
		Collection<DbElementDefinition> defs = dbm.listAll();
		for (DbElementDefinition def: defs) {
			this.register(def);
		}
	}

	protected void register(DbElementDefinition def) {
		this.registerDefinition(def.getName(), def);
		def.getProperties().put("enviromentManager", enviromentManager);
	}

	public void refresh() {
		try {
			this.doRefresh();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void register(JdbcConfigLoader loader) {
		configs.add(loader);
	}

	abstract protected void doRefresh() throws Exception;

	@Override
	public String toString() {
		String msg = "Registered DbElements: [";
		msg += StringUtils.join(this.getDefinitions().keySet(), ',');
		msg += "]";
		return msg;
	}
}
