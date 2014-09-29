package com.bstek.dorado.vidorsupport.vidor;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

import com.bstek.dorado.vidorsupport.output.OutputContext;

public class EntityNode extends XmlNode {

	static final String NODE_NAME = "Entity";
	
	public EntityNode() {
		super(NODE_NAME);
	}
	
	protected EntityNode(String ruleId) {
		super(ruleId);
	}
	
	@Override
	public void output(OutputContext context) {
		Map<String, String> attributes = this.getOutputAttributes();
		JsonGenerator jsonGenerator = context.getJsonGenerator();
		try {
			jsonGenerator.writeStartObject();
			for (String key: attributes.keySet()) {
				String value = attributes.get(key);
				jsonGenerator.writeStringField(key, value);
			}
			jsonGenerator.writeEndObject();
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
