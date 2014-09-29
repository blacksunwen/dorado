package com.bstek.dorado.vidorsupport.rule;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

import com.bstek.dorado.vidorsupport.output.OutputContext;

public class EnumEditorMeta extends AbstractEditorMeta {
	private String[] enumValues;
	
	public EnumEditorMeta(String name, String[] enumValues) {
		super(name, Treaty.Editor.ENUM);
		this.enumValues = enumValues;
	}
	
	public String[] getEnumValues() {
		return enumValues;
	}

	@Override
	public void output(OutputContext context) {
		JsonGenerator jsonGenerator = context.getJsonGenerator();
		try {
			jsonGenerator.writeStartObject();
			jsonGenerator.writeStringField("type", getType());
			jsonGenerator.writeFieldName("enumValues");
			jsonGenerator.writeStartArray();
			for (String value: enumValues) {
				jsonGenerator.writeString(value);
			}
			jsonGenerator.writeEndArray();
			jsonGenerator.writeEndObject();
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
