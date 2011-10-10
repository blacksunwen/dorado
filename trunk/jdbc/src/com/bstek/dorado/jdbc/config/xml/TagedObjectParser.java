package com.bstek.dorado.jdbc.config.xml;

import com.bstek.dorado.config.xml.ObjectParser;

public class TagedObjectParser extends ObjectParser {

	private String tagName;

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
}
