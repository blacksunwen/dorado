package com.bstek.dorado.jdbc.model.storedprogram;

public class StoredFunction extends StoredProgram {

	private ReturnParameter returnParameter;
	
	public ReturnParameter getReturnParameter() {
		return returnParameter;
	}

	public void setReturnParameter(ReturnParameter returnParameter) {
		this.returnParameter = returnParameter;
	}

	@Override
	public String getType() {
		return "StoredFunction";
	}

}
