package com.bstek.dorado.vidorsupport.vidor;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.dom4j.Element;

import com.bstek.dorado.vidorsupport.output.OutputContext;
import com.bstek.dorado.vidorsupport.rule.Rule;

public class EventNode extends XmlNode {

	static final String NODE_NAME = "ClientEvent";
	static final String TEXT = "text";
	
	public EventNode() {
		super(NODE_NAME);
	}

	@Override
	public void acceptAttributes(Element element, Rule rule) {
		super.acceptAttributes(element, rule);
		this.setText(element);
	}
	
	public void appendTo(XmlNode parent, Element element) {
		parent.getEventNodes().add(this);
	}
	
	@Override
	public void output(OutputContext context) {
		JsonGenerator jsonGenerator = context.getJsonGenerator();
		try {
			jsonGenerator.writeStartObject();
			Map<String, String> attributes = this.getOutputAttributes();
			if (!attributes.isEmpty()) {
				for (String key: attributes.keySet()) {
					String value = attributes.get(key);
					jsonGenerator.writeStringField(key, value);
				}
			}
			
			String text = this.getText();
			if (text != null && text.length() > 0) {
				jsonGenerator.writeStringField(TEXT, text);
			}
			
			jsonGenerator.writeEndObject();
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
