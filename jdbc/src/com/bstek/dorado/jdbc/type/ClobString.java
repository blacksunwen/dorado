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
		if (dbValue instanceof Clob) {
			Clob clob = (Clob)dbValue;
			try {
				dbValue = clob.getSubString(1, (int) clob.length());
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		
		return super.fromDB(dbValue);
	}

	@Override
	public Object toDB(Object obj) {
		return super.toDB(obj);
	}

}
