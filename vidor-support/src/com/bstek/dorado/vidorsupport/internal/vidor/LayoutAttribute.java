package com.bstek.dorado.vidorsupport.internal.vidor;

import org.codehaus.jackson.node.ObjectNode;

public class LayoutAttribute extends AbstractExpressionAttribute {

	public LayoutAttribute() {
		super();
	}
	
	public LayoutAttribute(String expression) {
		super(expression);
	}

	public LayoutAttribute(ObjectNode lauoutNode) {
		super(lauoutNode);
	}

	@Override
	public String getJsonAttrubuteName() {
		return XmlNode.LAYOUT;
	}
	
}
