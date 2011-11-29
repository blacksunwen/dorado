package com.bstek.dorado.jdbc.type;

public class TimeDate extends AbstractDate {

	public TimeDate() {
		super("TIME");
	}

	@SuppressWarnings("deprecation")
	@Override
	public Object fromDB(Object obj) {
		java.util.Date javaDate = (java.util.Date)super.fromDB(obj);
		if (javaDate == null) {
			return null;
		}
		
		return new java.sql.Time(javaDate.getHours(), javaDate.getMinutes(), javaDate.getSeconds());
	}

	@Override
	public Object toDB(Object obj) {
		return super.toDB(obj);
	}

}
