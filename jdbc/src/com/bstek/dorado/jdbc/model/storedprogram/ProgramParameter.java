package com.bstek.dorado.jdbc.model.storedprogram;

public class ProgramParameter extends AbstractParameter {
	
	public enum Type {
		IN, OUT, INOUT
	}
	
	public Type type = Type.IN;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
}
