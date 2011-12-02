package com.bstek.dorado.jdbc;

import java.sql.Types;

import org.springframework.core.Constants;

/**
 * JDBC模块使用的常量
 * 
 * @author mark
 * 
 */
public class JdbcConstants {

	public static final String CONFIG_RELOAD_ELEMENT = "jdbc.autoReloadElement";

	public static final String TABLE_CATALOG = "TABLE_CATALOG";

	public static final String TABLE_CAT = "TABLE_CAT";
	
	public static final String TABLE_SCHEM = "TABLE_SCHEM";
	
	public static final String TABLE_NAME = "TABLE_NAME";
	
	public static final String TABLE_TYPE = "TABLE_TYPE";
	
	public static final String IDENTITY = "IDENTITY";
	
	public static final String IS_AUTOINCREMENT = "IS_AUTOINCREMENT";
	
	public static final String COLUMN_NAME = "COLUMN_NAME";

	public static final String COLUMN_LABEL = "COLUMN_LABEL";
	
	public static final String DATA_TYPE = "DATA_TYPE";
	
	public static final String TYPE_NAME = "TYPE_NAME";
	
	public static final String YES = "YES";
	
	public static final String NO = "NO";

	public static final String IS_KEY = "IS_KEY";
	
	public static final String ROWNUM_VAR = "ROWNUM_";
	
	private static class JdbcTypeConstants extends Constants{

		public JdbcTypeConstants() {
			super(Types.class);
		}
		
		public boolean hasObject(String name) {
			return this.getFieldCache().containsKey(name);
		}
	}
	
	private static JdbcTypeConstants JDBC_TYPE_CONSTANTS = new JdbcTypeConstants();
	
	public static int getTypeValue(String name) {
		return JDBC_TYPE_CONSTANTS.asNumber(name).intValue();
	}
	
	public static String getTypeName(int value) {
		return JDBC_TYPE_CONSTANTS.toCode(value, "");
	}
	
	public static boolean hasType(String name) {
		return JDBC_TYPE_CONSTANTS.hasObject(name);
	}
}
