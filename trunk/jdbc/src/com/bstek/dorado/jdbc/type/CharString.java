package com.bstek.dorado.jdbc.type;

import java.util.Map;

import com.bstek.dorado.jdbc.support.JdbcConstants;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class CharString extends AbstractString {

	public CharString() {
		super("CHAR");
	}

	@Override
	public boolean is(Map<String, String> columnMeta) {
		boolean is = super.is(columnMeta);
		if (!is) {
			//check for mssql
			if ("-15".equals(columnMeta.get(JdbcConstants.DATA_TYPE)) && 
					"nchar".equalsIgnoreCase(columnMeta.get(JdbcConstants.TYPE_NAME))) {
				return true;
			}
			
			return false;
		} else {
			return is;
		}
	}
}
