package com.bstek.dorado.vidorsupport.output;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;

public class BooleanField extends AbstractField<Boolean> {

	public BooleanField(String name) {
		this(name, Boolean.FALSE);
	}
	
	public BooleanField(String name, Boolean ignoredValue) {
		super(name, ignoredValue);
		this.setValue(ignoredValue);
	}
	
	public void output(OutputContext context) {
		String name = this.getName();
		Boolean value = this.getValue();
		try {
			context.getJsonGenerator().writeBooleanField(name, value);
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
