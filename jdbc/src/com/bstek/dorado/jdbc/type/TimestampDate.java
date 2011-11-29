package com.bstek.dorado.jdbc.type;

public class TimestampDate extends AbstractDate {

	public TimestampDate() {
		super("TIMESTAMP");
	}

	@Override
	public Object fromDB(Object obj) {
		java.util.Date javaDate = (java.util.Date)super.fromDB(obj);
		if (javaDate == null) {
			return null;
		}
		
		return new java.sql.Timestamp(javaDate.getTime());
	}

	@Override
	public Object toDB(Object obj) {
		return super.toDB(obj);
	}

}
