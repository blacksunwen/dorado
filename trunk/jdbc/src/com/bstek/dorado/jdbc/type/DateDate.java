package com.bstek.dorado.jdbc.type;

import java.util.Map;

import com.bstek.dorado.jdbc.support.JdbcConstants;

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
		
		int year = javaDate.getYear();
		int month = javaDate.getMonth();
		int date = javaDate.getDate();
		return new java.sql.Date(year, month, date);
	}

	@Override
	public Object toDB(Object obj) {
		return super.toDB(obj);
	}

	@Override
	public boolean is(Map<String, String> columnMeta) {
		boolean is = super.is(columnMeta);
		if (!is) {
			String typeName = columnMeta.get(JdbcConstants.TYPE_NAME);
			String dataType = columnMeta.get(JdbcConstants.DATA_TYPE);
			//check for mssql
			if ("-9".equalsIgnoreCase(dataType) && "date".equalsIgnoreCase(typeName)) {
				return true;
			}
			
			return false;
		} else {
			return is;
		}
	}
	
}
