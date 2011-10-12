package com.bstek.dorado.jdbc.config;

import org.w3c.dom.Element;

import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.core.io.Resource;

class XmlElementWrapper {

	private Element element;
	
	private Resource resource;
	
	private ObjectParser parser;
	
	XmlElementWrapper(Element element, Resource resource, ObjectParser parser){
		this.element = element;
		this.resource = resource;
		this.parser = parser;
	}

	public Element getElement() {
		return element;
	}

	public Resource getResource() {
		return resource;
	}

	public ObjectParser getParser() {
		return parser;
	}
	
}
