package com.bstek.dorado.jdbc.model.autotable;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public interface MatchRule {

	AutoTable getAutoTable();
	
	void setAutoTable(AutoTable autoTable);
	
	boolean isAvailable();
	
	void setAvailable(boolean available);
	
	boolean isNot();
	
	void setNot(boolean not);
}
