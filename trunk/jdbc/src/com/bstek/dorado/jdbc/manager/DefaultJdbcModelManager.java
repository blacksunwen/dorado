package com.bstek.dorado.jdbc.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.config.definition.DefaultDefinitionManager;
import com.bstek.dorado.config.definition.DefinitionManager;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.jdbc.config.xml.TagedObjectParser;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.DbElement.Type;
import com.bstek.dorado.jdbc.model.DbElementCreationContext;
import com.bstek.dorado.jdbc.model.NamedObjectDefinition;
import com.bstek.dorado.jdbc.sql.SqlGenerator;
import com.bstek.dorado.util.Assert;

public class DefaultJdbcModelManager implements JdbcModelManager {

	private DefinitionManager<NamedObjectDefinition> definitionManager = new DefaultDefinitionManager<NamedObjectDefinition>();
	private Map<DbElement.Type, TagedObjectParser> objectParsers = new HashMap<DbElement.Type, TagedObjectParser>();
	private Map<DbElement.Type, SqlGenerator<DbElement>> sqlGenerators = new HashMap<DbElement.Type, SqlGenerator<DbElement>>();
	
	@Override
	public DefinitionManager<NamedObjectDefinition> getDefinitionManager() {
		return definitionManager;
	}
	
	public void setDefinitionManager(DefinitionManager<NamedObjectDefinition> definitionManager) {
		this.definitionManager = definitionManager;
	}
	
	public void setParsers(List<TagedObjectParser> parsers) {
		objectParsers.clear();
		for (TagedObjectParser parser: parsers) {
			String tagName = parser.getTagName();
			objectParsers.put(DbElement.Type.valueOf(tagName), parser);
		}
	}

	public void setSqlGenerators(List<SqlGenerator<DbElement>> generators) {
		sqlGenerators.clear();
		for (SqlGenerator<DbElement> generator: generators) {
			Type type = generator.getType();
			sqlGenerators.put(type, generator);
		}
	}
	
	@Override
	public DbElement getDbElement(String name) {
		ObjectDefinition definition = getDefinitionManager().getDefinition(name);
		Assert.notNull(definition, "no DbElement named [" + name + "].");
		
		DbElementCreationContext context = new DbElementCreationContext();
		try {
			return (DbElement)definition.create(context);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ObjectParser getParser(Type type) {
		Assert.notNull(type);
		TagedObjectParser parser = objectParsers.get(type);
		
		Assert.notNull(parser);
		return parser;
	}

	public SqlGenerator<DbElement> getSqlGenerator(DbElement.Type type) {
		Assert.notNull(type);
		SqlGenerator<DbElement> generator = sqlGenerators.get(type);

		Assert.notNull(generator);
		return generator;
	}
}
