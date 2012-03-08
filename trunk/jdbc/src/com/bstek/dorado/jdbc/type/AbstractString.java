package com.bstek.dorado.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractString extends AbstractJdbcType {

	private boolean _trim = false;
	
	public AbstractString(String jdbcName) {
		super(jdbcName, "String");
	}

	public void setTrim(boolean trim) {
		this._trim = trim;
	}
	
	public boolean isTrim() {
		return this._trim;
	}

	@Override
	public Object fromDB(Object obj) {
		String value = (String)super.fromDB(obj);
		if (this._trim && StringUtils.isNotEmpty(value)) {
			return value.trim();
		} else {
			return value;
		}
	}

	@Override
	public Object toDB(Object obj) {
		String value = (String) super.toDB(obj);
		
		if (this._trim && StringUtils.isNotEmpty(value)) {
			return value.trim();
		} else {
			return value;
		}
	}

	@Override
	public Object fromDB(ResultSet rs, String columnName) throws SQLException {
		String value = rs.getString(columnName);
		return fromDB(value);
	}
}
