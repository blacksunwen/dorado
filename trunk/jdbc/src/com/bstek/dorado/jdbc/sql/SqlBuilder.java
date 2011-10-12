package com.bstek.dorado.jdbc.sql;

public class SqlBuilder {

	private StringBuilder sql = new StringBuilder();;
	
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
			sql.append(token + ' ');
		}
		
		return this;
	}
	
	public SqlBuilder bothSpace(String token) {
		sql.append(' ' + token + ' ');
		return this;
	}
	
	public SqlBuilder brackets(String token) {
		sql.append('(' + token + ')');
		return this;
	}
	
	public String build() {
		return this.sql.toString();
	}
}
