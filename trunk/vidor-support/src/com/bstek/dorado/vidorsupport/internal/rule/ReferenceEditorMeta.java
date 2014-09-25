package com.bstek.dorado.vidorsupport.internal.rule;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

import com.bstek.dorado.vidorsupport.internal.output.OutputContext;

public class ReferenceEditorMeta extends EnumEditorMeta {

	private String rule;
	private String property;
	
	public ReferenceEditorMeta(String rule, String property, String[] enumValues) {
		super(rule + ":" + property, enumValues);
		this.type = Treaty.Editor.REFERENCE;
		this.rule = rule;
		this.property = property;
	}

	public String getRule() {
		return rule;
	}

	public String getProperty() {
		return property;
	}

	@Override
	public void output(OutputContext context) {
		JsonGenerator jsonGenerator = context.getJsonGenerator();
		try {
			jsonGenerator.writeStartObject();
			jsonGenerator.writeStringField("type", getType());
			jsonGenerator.writeStringField("rule", rule);
			jsonGenerator.writeStringField("property", property);
			
			String[] enumValues = this.getEnumValues();
			if (enumValues != null && enumValues.length > 0) {
				jsonGenerator.writeFieldName("enumValues");
				jsonGenerator.writeStartArray();
				for (String value: enumValues) {
					jsonGenerator.writeString(value);
				}
				jsonGenerator.writeEndArray();
			}
			jsonGenerator.writeEndObject();
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
