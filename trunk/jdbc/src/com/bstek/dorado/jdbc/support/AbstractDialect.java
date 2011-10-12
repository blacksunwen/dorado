package com.bstek.dorado.jdbc.support;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.key.KeyGenerator;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlBuilder;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.util.Assert;

public abstract class AbstractDialect implements Dialect {

	private Map<String, JdbcType> jdbcTypeMap = new LinkedHashMap<String, JdbcType>();
	private LinkedHashMap<String, KeyGenerator<Object>> keyGeneratorMap = new LinkedHashMap<String, KeyGenerator<Object>>();
	
	public JdbcType getJdbcType(String name) {
		JdbcType jdbcType = jdbcTypeMap.get(name);
		Assert.notNull(jdbcType, "could not look up for JdbcType named [" + name + "].");
		return jdbcType;
	}
	
	public void setJdbcTypes(List<JdbcType> jdbcTypes) {
		jdbcTypeMap.clear();
		for (JdbcType jdbcType: jdbcTypes) {
			jdbcTypeMap.put(jdbcType.getName(), jdbcType);
		}
	}
	
	public List<JdbcType> getJdbcTypes() {
		return new ArrayList<JdbcType>(jdbcTypeMap.values());
	}
	
	public KeyGenerator<Object> getKeyGenerator(String name) {
		KeyGenerator<Object> kg = keyGeneratorMap.get(name);
		Assert.notNull(kg, "could not look up for KeyGenerator named [" + name + "].");
		return kg;
	}
	
	@SuppressWarnings("rawtypes")
	public void setKeyGenerators(List<KeyGenerator> kgs) {
		keyGeneratorMap.clear();
		for (KeyGenerator<Object> kg : kgs) {
			keyGeneratorMap.put(kg.getName(), kg);
		}
	}
	
	public List<KeyGenerator<Object>> getKeyGenerators() {
		return new ArrayList<KeyGenerator<Object>>(keyGeneratorMap.values());
	}
	
	public String toCountSQL(String sql) {
		SqlBuilder sqlBuilder = new SqlBuilder();
		sqlBuilder.rightSpace(KeyWord.SELECT, "COUNT(*)", KeyWord.FROM).brackets(sql);
		
		return sqlBuilder.build();
	}
	
	public String toSQL(SelectSql selectSql) {
		return selectSql.toSQL(this);
	}
	
	public String toCountSQL(SelectSql selectSql) {
		return selectSql.toCountSQL(this);
	}
	
}
