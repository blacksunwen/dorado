package com.bstek.dorado.jdbc.type;

public class DateDate extends AbstractDate {

	public DateDate() {
		super("DATE");
	}

	@SuppressWarnings("deprecation")
	@Override
	public Object fromDB(Object obj) {
		java.util.Date javaDate = (java.util.Date)super.fromDB(obj);
		if (javaDate == null) {
			return null;
		}
		
		return new java.sql.Date(javaDate.getYear(), javaDate.getMonth(), javaDate.getDay());
	}

	@Override
	public Object toDB(Object obj) {
		return super.toDB(obj);
	}
}
