package com.bstek.dorado.vidorsupport.output;

import java.io.IOException;
import java.util.Arrays;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

public class StringArrayField extends AbstractField<String[]> {

	public StringArrayField(String name) {
		super(name, null);
	}

	public StringArrayField(String name, String[] ignoredValue) {
		super(name, ignoredValue);
	}

	
	public boolean shouldOutput() {
		String[] value = this.getValue();
		String[] ignoredValue = this.getIgnoredValue();
		
		if (value == null || value.length == 0) 
			return false;
		else if (ignoredValue == null)
			return true;
		else 	
			return !Arrays.equals(value, ignoredValue); 
	}
	
	
	public void output(OutputContext context) {
		String name = this.getName();
		String[] value = this.getValue();
		
		JsonGenerator jsonGenerator = context.getJsonGenerator();
		try {
			context.getJsonGenerator().writeArrayFieldStart(name);
			for (int i=0; i<value.length; i++) {
				String text = value[i];
				jsonGenerator.writeString(text);
			}
			context.getJsonGenerator().writeEndArray();
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
