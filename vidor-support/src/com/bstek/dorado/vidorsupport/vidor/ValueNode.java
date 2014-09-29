package com.bstek.dorado.vidorsupport.vidor;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.dom4j.Element;

import com.bstek.dorado.vidorsupport.output.OutputContext;
import com.bstek.dorado.vidorsupport.rule.Rule;


public class ValueNode extends XmlNode {

	static final String NODE_NAME = "Value";
	
	public ValueNode() {
		super(NODE_NAME);
	}

	@Override
	public void acceptAttributes(Element element, Rule rule) {
		super.acceptAttributes(element, rule);
		this.setText(element);
	}
	
	@Override
	public void output(OutputContext context) {
		JsonGenerator jsonGenerator = context.getJsonGenerator();
		String value = this.getText();
		try {
			jsonGenerator.writeString(value);
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
