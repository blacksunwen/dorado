package com.bstek.dorado.jdbc.type;

import java.util.Map;

import com.bstek.dorado.jdbc.support.JdbcConstants;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
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

	@Override
	public boolean is(Map<String, String> columnMeta) {
		boolean is = super.is(columnMeta);
		if (!is) {
			String typeName = columnMeta.get(JdbcConstants.TYPE_NAME);
			String dataType = columnMeta.get(JdbcConstants.DATA_TYPE);
			//check for mssql
			if ("-9".equalsIgnoreCase(dataType) && "time".equalsIgnoreCase(typeName)) {
				return true;
			}
			
			return false;
		} else {
			return is;
		}
	}
}
