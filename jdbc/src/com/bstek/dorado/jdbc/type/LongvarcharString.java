package com.bstek.dorado.jdbc.type;

import java.util.Map;

import com.bstek.dorado.jdbc.support.JdbcConstants;

public class LongvarcharString extends AbstractString {

	public LongvarcharString() {
		super("LONGVARCHAR");
	}

	@Override
	public boolean is(Map<String, String> columnMeta) {
		boolean is = super.is(columnMeta);
		if (!is) {
			//check for mssql
			if ("-16".equals(columnMeta.get(JdbcConstants.DATA_TYPE)) && 
					"ntext".equalsIgnoreCase(columnMeta.get(JdbcConstants.TYPE_NAME))) {
				return true;
			}
			
			return false;
		} else {
			return is;
		}
	}
}
