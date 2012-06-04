package com.bstek.dorado.jdbc.sql;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class SqlBuilder {

	private StringBuilder sql;
	
	public SqlBuilder(int c) {
		sql = new StringBuilder(c);
	}
	
	public SqlBuilder() {
		sql = new StringBuilder(128);
	}
	
	public SqlBuilder space() {
		sql.append(' ');
		return this;
	}
	
	public SqlBuilder append(String... tokens) {
		for (String token: tokens) {
			sql.append(token);
		}
		
		return this;
	}
	
	public SqlBuilder leftSpace(String... tokens) {
		for (String token: tokens) {
			sql.append(' ').append(token);
		}
		
		return this;
	}

	public SqlBuilder rightSpace(String... tokens) {
		for (String token: tokens) {
			sql.append(token).append(' ');
		}
		
		return this;
	}
	
	public SqlBuilder bothSpace(String token) {
		sql.append(' ').append(token).append(' ');
		return this;
	}
	
	public SqlBuilder brackets(String token) {
		sql.append('(').append(token).append(')');
		return this;
	}
	
	public SqlBuilder appendVar(String... varNames) {
		if (varNames.length == 1) {
			sql.append(':').append(varNames[0]);
		} else if (varNames.length > 1) {
			String name = varNames[0];
			sql.append(':').append(name);
			for (int i=1; i<varNames.length; i++) {
				name = varNames[i];
				sql.append(',').append(':').append(name);
			}
		}
		
		return this;
	}
	
	public SqlBuilder bracketsVar(String... varNames) {
		sql.append('(');
		this.appendVar(varNames);
		sql.append(')');
		return this;
	}
	
	public SqlBuilder appendColumn(String... columnNames) {
		if (columnNames.length == 1) {
			sql.append(columnNames[0]);
		} else if (columnNames.length > 1) {
			String name = columnNames[0];
			sql.append(name);
			for (int i=1; i<columnNames.length; i++) {
				name = columnNames[i];
				sql.append(',').append(name);
			}
		}
		
		return this;
	}
	
	public SqlBuilder bracktsColumn(String... columnNames) {
		sql.append('(');
		this.appendColumn(columnNames);
		sql.append(')');
		return this;
	}
	
	public String build() {
		return this.sql.toString();
	}
}
