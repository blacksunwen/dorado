package com.bstek.dorado.vidorsupport.rule;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

import com.bstek.dorado.vidorsupport.output.IOutputable;
import com.bstek.dorado.vidorsupport.output.OutputContext;

public abstract class AbstractEditorMeta implements IOutputable<OutputContext> {

	private String name;
	protected String type;
	
	public AbstractEditorMeta(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}
	
	void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}
	
	public void output(OutputContext context) {
		JsonGenerator jsonGenerator = context.getJsonGenerator();
		try {
			jsonGenerator.writeStartObject();
			jsonGenerator.writeStringField("type", getType());
			jsonGenerator.writeEndObject();
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
