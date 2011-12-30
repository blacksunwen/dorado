package com.bstek.dorado.jdbc.model;

import java.util.List;

import com.bstek.dorado.jdbc.sql.CurdSqlGenerator;

public interface DbTable extends DbElement {

	List<Column> getAllColumns();
	
	Column getColumn(String name);
	
	TableTrigger getTrigger();
	
	CurdSqlGenerator getCurdSqlGenerator();
}
