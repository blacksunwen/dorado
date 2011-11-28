package com.bstek.dorado.jdbc.model.storedprogram;

public class ReturnParameter extends ProgramParameter {

	public static final String PARAM_NAME = "return";
	
	@Override
	public Type getType() {
		return Type.OUT;
	}

	@Override
	public String getName() {
		return PARAM_NAME;
	}

	@Override
	public void setName(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setType(Type type) {
		throw new UnsupportedOperationException();
	}

}
