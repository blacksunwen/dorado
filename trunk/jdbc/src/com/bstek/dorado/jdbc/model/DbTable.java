package com.bstek.dorado.jdbc.model;

import java.util.List;

public interface DbTable extends DbElement {

	List<Column> getAllColumns();
	
	Column getColumn(String name);
	
	TableTrigger getTrigger();
}
