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

	public static Constants JDBC_TYPE_CONSTANTS = new Constants(Types.class);

	public static final String AUTO_CREATE_COLUMNS = "autoCreateColumns";
	
	public static final String IDENTITY = "IDENTITY";
	
	public static final String COLUMN_NAME = "COLUMN_NAME";

	public static final String DATA_TYPE = "DATA_TYPE";
	
	public static final String YES = "YES";
	
	public static final String NO = "NO";

	
	public static final String IS_KEY = "IS_KEY";
	
	public static final String IS_AUTOINCREMENT = "IS_AUTOINCREMENT";
	
	public static int getTypeValue(String name) {
		return JDBC_TYPE_CONSTANTS.asNumber(name).intValue();
	}
	
	public static String getTypeName(int value) {
		return JDBC_TYPE_CONSTANTS.toCode(value, "");
	}
}
