package com.bstek.dorado.jdbc.type;

import java.util.Map;

import com.bstek.dorado.jdbc.JdbcConstants;

public class VarcharString extends AbstractString {

	public VarcharString() {
		super("VARCHAR");
	}

	@Override
	public boolean is(Map<String, String> columnMeta) {
		boolean is = super.is(columnMeta);
		if (!is) {
			//check for mssql
			if ("-9".equals(columnMeta.get(JdbcConstants.DATA_TYPE)) && 
					"nvarchar".equalsIgnoreCase(columnMeta.get(JdbcConstants.TYPE_NAME))) {
				return true;
			}
			
			return false;
		} else {
			return is;
		}
	}

}
