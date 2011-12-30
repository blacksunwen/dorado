package com.bstek.dorado.jdbc.config;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import com.bstek.dorado.config.definition.DefaultDefinitionManager;
import com.bstek.dorado.jdbc.model.DbElementDefinition;

public class DefaultDmbManager extends  DefaultDefinitionManager<DbElementDefinition> implements DbmManager {

	private Set<DbModel> dbmSet = new LinkedHashSet<DbModel>();
	
	public void registerDbm(DbModel dbm) {
		dbmSet.add(dbm);
		Collection<DbElementDefinition> defs = dbm.listAll();
		for (DbElementDefinition def: defs) {
			this.registerDefinition(def.getName(), def);
		}
	}

	public Collection<DbModel> listAllDbms() {
		return dbmSet;
	}

	public void clearDbms() {
		dbmSet.clear();
	}
}
