package com.bstek.dorado.vidorsupport.internal.output;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;

public class IntegerField extends AbstractField<Integer> {

	public IntegerField(String name) {
		this(name, null);
	}
	public IntegerField(String name, Integer ignoredValue) {
		super(name, ignoredValue);
	}

	@Override
	public void output(OutputContext context) {
		String name = this.getName();
		Integer value = this.getValue();
		
		try {
			context.getJsonGenerator().writeNumberField(name, value);
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
