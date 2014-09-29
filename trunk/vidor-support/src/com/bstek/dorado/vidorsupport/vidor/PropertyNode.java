package com.bstek.dorado.vidorsupport.vidor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.dom4j.Element;

import com.bstek.dorado.vidorsupport.exception.XmlException;
import com.bstek.dorado.vidorsupport.output.OutputContext;
import com.bstek.dorado.vidorsupport.rule.Rule;

public class PropertyNode extends XmlNode {

	static final String NODE_NAME = "Property";
	
	public PropertyNode() {
		super(NODE_NAME);
	}
	
	protected PropertyNode(String ruleId) {
		super(ruleId);
	}
	
	public PropertyNode(String name, String value) {
		this();
		this.getAttributes().put(NAME, name);
		this.setText(value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void acceptAttributes(Element element, Rule rule) {
		super.acceptAttributes(element, rule);
		
		List<Element> elements = element.elements();
		if (elements.size() == 0) {
			this.setText(element);
		}
	}

	@Override
	public void output(OutputContext context) {
		// *Property/Property
		if (!this.getAttributeNodes().isEmpty()) {
			JsonGenerator jsonGenerator = context.getJsonGenerator();
			try {
				jsonGenerator.writeStartObject();
				Map<String, XmlNode> attributeNodes = this.getAttributeNodes();
				for (String key: attributeNodes.keySet()) {
					XmlNode node = attributeNodes.get(key);
					if (node.getClass().equals(this.getClass())) {
						PropertyNode propertyNode = (PropertyNode)node;
						String name = propertyNode.getOutputAttributes().get(NAME);
						String value = propertyNode.getText();
						jsonGenerator.writeStringField(name, value);
					} else 
					if (node.getClass().equals(CollectionlNode.class)) {
						jsonGenerator.writeFieldName(key);
						node.output(context);
					}
				}
				jsonGenerator.writeEndObject();
				return;
			} catch (JsonGenerationException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		throw new XmlException(this.toString());
	}
	
	@Override
	public void appendTo(XmlNode parent, Element element) {
		String name = this.getAttributes().get(NAME);
		
		// Property/*Property/Text
		if (parent.getClass().equals(PropertyNode.class)) {
			if (this.getNodes().size() == 0) {
				parent.getAttributeNodes().put(name, this);
				return;
			}
		}
		
		// *Property/Property
		if (this.getAttributeNodes().size() > 0) {
			parent.getAttributeNodes().put(name, this);
			return;
		}
		
		// *Property/Text
		if (this.getAttributeNodes().size() == 0 && this.getNodes().size() == 0) {
			String value = this.getText();
			if (value != null && value.length() > 0) {
				parent.getAttributes().put(name, value);
			}
			
			return;
		} 
		
		// *Property/Entity 或者  *Property/Collection
		if (this.getNodes().size() == 1) {
			XmlNode innerPropertyNode = this.getNodes().remove(0);
			parent.getAttributeNodes().put(name, innerPropertyNode);
			
			return;
		} 
				
		throw new XmlException(element);
	}

}
