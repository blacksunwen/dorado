package com.bstek.dorado.jdbc.type;

import java.sql.Clob;
import java.sql.SQLException;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class ClobString extends AbstractString {

	public ClobString() {
		super("CLOB");
	}

	@Override
	public Object fromDB(Object obj) {
		Object dbValue = obj;
		if (dbValue == null) {
			return null;
		}
		if (dbValue instanceof String){
			return (String)dbValue;
		}
		Clob clob = (Clob)dbValue;
		try {
			return clob.getSubString(1, (int) clob.length());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object toDB(Object obj) {
		return super.toDB(obj);
	}

}
