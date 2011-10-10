package com.bstek.dorado.jdbc.model.autotable;

public interface MatchRule {

	AutoTable getAutoTable();
	
	void setAutoTable(AutoTable autoTable);
	
	boolean isAvailable();
	
	void setAvailable(boolean available);
	
	boolean isNot();
	
	void setNot(boolean not);
}
