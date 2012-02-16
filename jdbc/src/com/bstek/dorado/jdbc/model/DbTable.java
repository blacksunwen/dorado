package com.bstek.dorado.jdbc.model;

import java.util.List;

import com.bstek.dorado.jdbc.TableTrigger;
import com.bstek.dorado.jdbc.sql.CurdSqlGenerator;

public interface DbTable extends DbElement {

	List<AbstractColumn> getAllColumns();
	
	AbstractColumn getColumn(String name);
	
	TableTrigger getTrigger();
	
	CurdSqlGenerator getCurdSqlGenerator();
}
