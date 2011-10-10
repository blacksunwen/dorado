package com.bstek.dorado.jdbc.manager;

import com.bstek.dorado.config.definition.DefinitionManager;
import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.NamedObjectDefinition;
import com.bstek.dorado.jdbc.sql.SqlGenerator;

public interface JdbcModelManager {

	DefinitionManager<NamedObjectDefinition> getDefinitionManager();
	
	DbElement getDbElement(String name);
	
	ObjectParser getParser(DbElement.Type type);
	
	SqlGenerator<DbElement> getSqlGenerator(DbElement.Type type);
	
}
