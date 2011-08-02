package com.bstek.dorado.console.parser;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.config.Parser;

public class Node {
	private String name;
	private String type;
	Parser parser;
	private List<KeyValue> properties = new ArrayList<KeyValue>();
	private List<Node> children = new ArrayList<Node>();

	public Node(){}
	public Node(Parser parser, String name) {
		this.parser = parser;
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getParserName() {
		return (parser == null)? null : parser.getClass().getName();
	}
	public void setParserName(String parser) {
		//
	}
	
	public List<KeyValue> getProperties() {
		return properties;
	}
	public void setProperties(List<KeyValue> properties) {
		this.properties = properties;
	}
	public void addProperty(String key, String value) {
		KeyValue kv = new KeyValue(key, value);
		properties.add(kv);
	}

	public List<Node> getChildren() {
		return children;
	}
	public void setChildren(List<Node> children) {
		this.children = children;
	}
	
	public static class KeyValue {
		private String key;
		private String value;
		
		public KeyValue() {}
				
		public KeyValue(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
}
