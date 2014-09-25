package com.bstek.dorado.vidorsupport.internal.output;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;

public class StringField extends AbstractField<String> {

	public StringField(String name) {
		this(name, null);
	}
	
	public StringField(String name, String ignoredValue) {
		super(name, ignoredValue);
	}
	
	@Override
	public boolean shouldOutput() {
		if (super.shouldOutput()) {
			return getValue().length() > 0;
		} else {
			return false;
		}
	}

	@Override
	public void output(OutputContext context) {
		String name = this.getName();
		String value = this.getValue();
		
		try {
			context.getJsonGenerator().writeStringField(name, value);
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
