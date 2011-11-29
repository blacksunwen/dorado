package com.bstek.dorado.jdbc.type;

import java.util.Map;

public class CharBoolean extends AbstractBoolean {

	private String trueValue = "0";
	private String falseValue = "1";
	
	public CharBoolean() {
		super("CHAR");
	}

	public String getTrueValue() {
		return trueValue;
	}

	public void setTrueValue(String trueValue) {
		this.trueValue = trueValue;
	}

	public String getFalseValue() {
		return falseValue;
	}

	public void setFalseValue(String falseValue) {
		this.falseValue = falseValue;
	}

	@Override
	public Object toDB(Object obj) {
		Boolean b = (Boolean) super.fromDB(obj);
		if (b != null) {
			if (b.booleanValue()) {
				return trueValue; 
			} else {
				return falseValue;
			}
		} else {
			return null;
		}
	}

	@Override
	public Object fromDB(Object obj) {
		if (obj == null) {
			return null;
		}
		
		String objStr = obj.toString();
		if (trueValue.equalsIgnoreCase(objStr)) {
			return Boolean.TRUE;
		} 
		if (falseValue.equalsIgnoreCase(objStr)){
			return Boolean.FALSE;
		}
		
		throw new IllegalArgumentException("unknown value [" + objStr + "], " +
				"it must be in [" + trueValue + "," + falseValue + "]");
	}

	@Override
	public boolean is(Map<String, String> columnMeta) {
		if (super.is(columnMeta) && "1".equals(columnMeta.get("COLUMN_SIZE"))) {
			return true;
		} else {
			return false;
		}
	}
	
}
