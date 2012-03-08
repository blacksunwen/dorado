package com.bstek.dorado.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class CharBoolean extends AbstractBoolean{

	private Set<String> trueValues = new HashSet<String>();
	private Set<String> falseValues = new HashSet<String>();
	
	private String defaultTrueValue = "1";
	private String defaultFalseValue = "0";
	
	public CharBoolean() {
		super("CHAR");
	}
	
	public Set<String> getTrueValues() {
		return trueValues;
	}

	public void setTrueValues(Set<String> trueValues) {
		this.trueValues = trueValues;
	}

	public Set<String> getFalseValues() {
		return falseValues;
	}

	public void setFalseValues(Set<String> falseValues) {
		this.falseValues = falseValues;
	}

	public String getDefaultTrueValue() {
		return defaultTrueValue;
	}

	public void setDefaultTrueValue(String defaultTrueValue) {
		this.defaultTrueValue = defaultTrueValue;
	}

	public String getDefaultFalseValue() {
		return defaultFalseValue;
	}

	public void setDefaultFalseValue(String defaultFalseValue) {
		this.defaultFalseValue = defaultFalseValue;
	}

	@Override
	public Object toDB(Object obj) {
		Boolean b = (Boolean) super.fromDB(obj);
		if (b != null) {
			if (b.booleanValue()) {
				return defaultTrueValue; 
			} else {
				return defaultFalseValue;
			}
		} else {
			return null;
		}
	}

	@Override
	public Object fromDB(ResultSet rs, String columnName) throws SQLException {
		Object value = rs.getString(columnName);
		return fromDB(value);
	}

	@Override
	public Object fromDB(Object obj) {
		if (obj == null) {
			return null;
		}
		
		String objStr = obj.toString();
		if (trueValues.contains(objStr)) {
			return Boolean.TRUE;
		} 
		if (falseValues.contains(objStr)){
			return Boolean.FALSE;
		}
		
		throw new IllegalArgumentException("unknown value [" + objStr + "], " +
				"it must be in [" + trueValues + "] or [" + falseValues + "]");
	}

	@Override
	public boolean is(Map<String, String> columnMeta) {
		if (super.is(columnMeta) && "1".equals(columnMeta.get("COLUMN_SIZE"))) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (trueValues.isEmpty()) {
			trueValues.add(defaultTrueValue);
			trueValues.add("Y");
			trueValues.add("y");
			trueValues.add("T");
			trueValues.add("t");
		}
		
		if (falseValues.isEmpty()) {
			falseValues.add(defaultFalseValue);
			falseValues.add("N");
			falseValues.add("n");
			falseValues.add("F");
			falseValues.add("f");
		}
		
		super.afterPropertiesSet();
	}
}
