package com.bstek.dorado.jdbc;

import java.util.List;

import com.bstek.dorado.jdbc.key.KeyGenerator;
import com.bstek.dorado.jdbc.model.autotable.FromTable;
import com.bstek.dorado.jdbc.model.autotable.Order;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlConstants.JoinModel;
import com.bstek.dorado.jdbc.type.JdbcType;

public interface Dialect {

	String joinToken(JoinModel joinModel, FromTable leftFromTable,
			String[] leftColumnNames, FromTable rightFromTable,
			String[] rightColumnNames);
	
	String orderToken(Order order);
	
	//--
	String toSQL(SelectSql selectSql);
	String toCountSQL(SelectSql selectSql);
	String toCountSQL(String sql);
	
	//--
	boolean isNarrowSupport();
	String narrowSql(SelectSql selectSql, int maxResults, int firstResult);
	
	//--
	boolean isSequenceSupport();
	String sequenceSql(String sequenceName);
	
	//--
	JdbcType getJdbcType(String name);
	List<JdbcType> getJdbcTypes();
	
	//--
	KeyGenerator<Object> getKeyGenerator(String name);
	List<KeyGenerator<Object>> getKeyGenerators();
}
