package com.bstek.dorado.jdbc.model;

import java.util.List;

import com.bstek.dorado.jdbc.DbTableTrigger;
import com.bstek.dorado.jdbc.sql.CurdSqlGenerator;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public interface DbTable extends DbElement {

	List<AbstractColumn> getAllColumns();
	
	AbstractColumn getColumn(String name);
	
	DbTableTrigger getTrigger();
	
	CurdSqlGenerator getCurdSqlGenerator();
}
