package com.bstek.dorado.vidorsupport.internal.vidor;

import org.codehaus.jackson.node.ObjectNode;

public class PositionAttribute extends AbstractExpressionAttribute {

	static final String POSITION = "position";
	
	public PositionAttribute() {
		super();
	}
	
	public PositionAttribute(String expression) {
		super(expression);
	}

	public PositionAttribute(ObjectNode lauoutNode) {
		super(lauoutNode);
	}

	@Override
	public String getType() {
		String type = super.getType();
		if (type == null || type.length() == 0) {
			type = "dock";
			this.setType(type);
		}
		return super.getType();
	}

	@Override
	public String getJsonAttrubuteName() {
		return POSITION;
	}
}
