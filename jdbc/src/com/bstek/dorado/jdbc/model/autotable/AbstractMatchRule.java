package com.bstek.dorado.jdbc.model.autotable;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class AbstractMatchRule implements MatchRule {
	private AutoTable autoTable;
	private boolean available = true;
	private boolean not = false;
	
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	public AutoTable getAutoTable() {
		return autoTable;
	}

	public void setAutoTable(AutoTable autoTable) {
		this.autoTable = autoTable;
	}

	public void setNot(boolean not) {
		this.not = not;
	}

	public boolean isNot() {
		return not;
	}

}
