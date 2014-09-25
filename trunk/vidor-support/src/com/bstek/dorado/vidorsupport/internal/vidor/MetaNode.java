package com.bstek.dorado.vidorsupport.internal.vidor;

import java.util.Map;

import org.dom4j.Element;

public class MetaNode extends EntityNode {

	public static final String RULE_ID = "!META_NODE";
	
	static final String NAME_VALUE = "meta";
	
	public MetaNode() {
		super(RULE_ID);
		this.getAttributes().put(NAME, NAME_VALUE);
	}
	
	public void appendTo(XmlNode parent, Element element) {
		parent.setMetaNode(this);
	}
	
	@Override
	protected Map<String, String> getOutputAttributes() {
		Map<String, String> attributes = super.getOutputAttributes();
		attributes.remove(NAME);
		return attributes;
	}
}
