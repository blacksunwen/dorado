package com.bstek.dorado.vidorsupport.internal.vidor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import com.bstek.dorado.vidorsupport.internal.output.IOutputable;
import com.bstek.dorado.vidorsupport.internal.output.OutputContext;
import com.bstek.dorado.config.text.ConfigurableDispatchableTextParser;
import com.bstek.dorado.config.text.DispatchableTextParser;
import com.bstek.dorado.config.text.TextParseContext;
import com.bstek.dorado.config.text.TextParser;

public abstract class AbstractExpressionAttribute implements IOutputable<OutputContext> {

	private Map<String, Object> attributeMap = new LinkedHashMap<String, Object>();
	private String type;
	
	public AbstractExpressionAttribute() {
		super();
	}
	
	public AbstractExpressionAttribute(String expression) {
		this();
		this.parse(expression);
	}
	
	public AbstractExpressionAttribute(ObjectNode lauoutNode) {
		this();
		this.parse(lauoutNode);
	}
	
	protected void parse(ObjectNode lauoutNode) {
		Iterator<String> fieldNameItr = lauoutNode.getFieldNames();
		while (fieldNameItr.hasNext()) {
			String fieldName = fieldNameItr.next();
			JsonNode filedValue = lauoutNode.get(fieldName);
			if (!filedValue.isNull() && filedValue.isTextual()) {
				String text = filedValue.asText();
				if (text != null && (text = text.trim()).length() > 0) {
					if ("type".equals(fieldName)) {
						type = text;
					} else {
						attributeMap.put(fieldName, text);
					}
				}
			}
		}
	}
	
	private TextParser newTextParser() {
		ConfigurableDispatchableTextParser dispatchableTextParser = new ConfigurableDispatchableTextParser();
		dispatchableTextParser.setHasHeader(true);
		TextParser attributeParser = new StringTextParser();
		dispatchableTextParser.registerAttributeParser(
				DispatchableTextParser.WILDCARD, attributeParser);
		return dispatchableTextParser;
	}
	
	@SuppressWarnings("unchecked")
	private void parse(String expression) {
		TextParser parser = this.newTextParser();
		
		String type = null;
		Map<String, String> attributes = null;
		try {
			attributes = (Map<String, String>) parser.parse(
					expression.toCharArray(), new TextParseContext());
			type = attributes.remove(DispatchableTextParser.HEADER_ATTRIBUTE);
		} catch (Exception e) {
			throw new IllegalArgumentException(expression);
		}
		
		this.type = type;
		this.attributeMap.putAll(attributes);
	}
	
	public String toExpression() {
		List<String> tokens = new ArrayList<String>(attributeMap.size());
		for (String key: attributeMap.keySet()) {
			Object value = attributeMap.get(key);
			tokens.add(key + ":" + value);
		}
		
		if (type != null && type.length() > 0) {
			if (tokens.isEmpty()) {
				return type;
			} else {
				return type + " " + StringUtils.join(tokens, ';');
			}
		} else {
			if (tokens.isEmpty()) {
				return null;
			} else {
				return StringUtils.join(tokens, ';');
			}
		}
	}
	
	public Map<String, Object> getAttributeMap() {
		return attributeMap;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public abstract String getJsonAttrubuteName();

	@Override
	public void output(OutputContext context) {
		String attrName = this.getJsonAttrubuteName();
		JsonGenerator jsonGenerator = context.getJsonGenerator();
		try {
			jsonGenerator.writeObjectFieldStart(attrName);
			
			if (type != null && type.length() > 0) {
				jsonGenerator.writeStringField("type", type);
			}
			
			for (String key: attributeMap.keySet()) {
				Object value = attributeMap.get(key);
				jsonGenerator.writeStringField(key, (String)value);
			}
			
			jsonGenerator.writeEndObject();
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributeMap == null) ? 0 : attributeMap.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractExpressionAttribute other = (AbstractExpressionAttribute) obj;
		if (attributeMap == null) {
			if (other.attributeMap != null)
				return false;
		} else if (!attributeMap.equals(other.attributeMap))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format(
				"AbstractExpressionAttribute [attributeMap=%s, type=%s]",
				attributeMap, type);
	}
	
}
