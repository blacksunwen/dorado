package com.bstek.dorado.vidorsupport.vidor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.dom4j.Attribute;
import org.dom4j.Element;

import com.bstek.dorado.vidorsupport.output.IOutputable;
import com.bstek.dorado.vidorsupport.output.OutputContext;
import com.bstek.dorado.vidorsupport.rule.Rule;

public class XmlNode implements IOutputable<OutputContext> {

	static final String NAME  = "name";
	static final String NODES = "nodes";
	static final String ATTRS = "attrs";
	static final String UV_ATTRS = "uv_attrs";
	static final String EVTS  = "events";
	static final String RULE  = "rule";
	static final String LAYOUT = "layout";
	static final String LAYOUT_CONSTRAINT = "layoutConstraint";
	static final String META = MetaNode.NAME_VALUE;
	
	private String ruleId;
	private String text;
	
	private Map<String, String> attributes = new LinkedHashMap<String, String>();
	private Map<String, XmlNode> attributeNodes = new LinkedHashMap<String, XmlNode>();
	private List<EventNode> eventNodes = new ArrayList<EventNode>();
	private List<XmlNode> nodes = new ArrayList<XmlNode>();
	
	private LayoutAttribute layoutAttribute;
	private PositionAttribute positionAttribute;
	
	private Map<String, String> uv_attributes = new LinkedHashMap<String, String>(2);
	private MetaNode metaNode;
	
	public XmlNode(String ruleId) {
		this.ruleId = ruleId;
	}
	
	public String getRuleId() {
		return ruleId;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public List<EventNode> getEventNodes() {
		return eventNodes;
	}
	public void setEventNodes(List<EventNode> eventNodes) {
		this.eventNodes = eventNodes;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	protected void setText(Element element) {
		String text = element.getText();
		if (text != null && text.length() > 0) {
			this.setText(text);
		}
	}

	public List<XmlNode> getNodes() {
		return nodes;
	}
	public void setNodes(List<XmlNode> nodes) {
		this.nodes = nodes;
	}

	public Map<String, XmlNode> getAttributeNodes() {
		return attributeNodes;
	}
	public void setAttributeNodes(Map<String, XmlNode> attributeNodes) {
		this.attributeNodes = attributeNodes;
	}
	
	public Map<String, String> getUnVisibleAttributes() {
		return uv_attributes;
	}
	public void setUnVisibleAttributes(Map<String, String> uv_attributes) {
		this.uv_attributes = uv_attributes;
	}

	public MetaNode getMetaNode() {
		return metaNode;
	}
	public void setMetaNode(MetaNode metaNode) {
		this.metaNode = metaNode;
	}

	public void acceptAttributes(Element element, Rule rule) {
		for (int i=0; i<element.attributeCount(); i++) {
			Attribute attr = element.attribute(i);
			String name = attr.getName();
			String value = attr.getValue();
			if (value != null && value.length() > 0) {
				if (LAYOUT.equals(name)) {
					this.setLayout(new LayoutAttribute(value));
				} else if (LAYOUT_CONSTRAINT.equals(name)){
					this.setPosition(new PositionAttribute(value));
				} else if(META.equals(name)) {
					uv_attributes.put(name, value);
				} else if (rule == null || !rule.hasFixedProperty(name)) {
					this.getAttributes().put(name, value);
				}
			}
		}
	}
	
	public void setLayout(LayoutAttribute layoutAttribute) {
		this.layoutAttribute = layoutAttribute;
	}
	public LayoutAttribute getLayout() {
		return this.layoutAttribute;
	}
	
	public void setPosition(PositionAttribute positionAttribute) {
		this.positionAttribute = positionAttribute;
	}
	public PositionAttribute getPosition() {
		return this.positionAttribute;
	}
	
	public void appendTo(XmlNode parent, Element element) {
		parent.getNodes().add(this);
	}
	
	protected Map<String, String> getOutputAttributes() {
		return new LinkedHashMap<String, String>(this.attributes);
	}
	
	public void output(OutputContext context) {
		JsonGenerator jsonGenerator = context.getJsonGenerator();
		
		try {
			jsonGenerator.writeStartObject();
			jsonGenerator.writeStringField(RULE, ruleId);
			
			Map<String, String> attributes = this.getOutputAttributes();
			//输出“属性”
			if (!attributes.isEmpty() || !attributeNodes.isEmpty()) {
				jsonGenerator.writeFieldName(ATTRS);
				jsonGenerator.writeStartObject();
				for (String key: attributes.keySet()) {
					String value = attributes.get(key);
					jsonGenerator.writeStringField(key, value);
				}
				Set<String> attributeNodeKeys = attributeNodes.keySet();
				for (String key: attributeNodeKeys) {
					XmlNode node = attributeNodes.get(key);
					jsonGenerator.writeFieldName(key);
					node.output(context);
				}
				jsonGenerator.writeEndObject();
			}
			//输出“不可见属性”
			if (uv_attributes != null && !uv_attributes.isEmpty()) {
				jsonGenerator.writeFieldName(UV_ATTRS);
				jsonGenerator.writeStartObject();
				for (String key: uv_attributes.keySet()) {
					String value = uv_attributes.get(key);
					jsonGenerator.writeStringField(key, value);
				}
				jsonGenerator.writeEndObject();
			}
			//输出“不可见节点”
			if (metaNode != null) {
				jsonGenerator.writeFieldName(META);
				metaNode.output(context);
			}
			
			//输出“布局”
			if (layoutAttribute != null) {
				layoutAttribute.output(context);
			}
			//输出“位置”
			if (positionAttribute != null) {
				positionAttribute.output(context);
			}
			//输出“子节点”
			if (!nodes.isEmpty()) {
				jsonGenerator.writeFieldName(NODES);
				jsonGenerator.writeStartArray();
				for (XmlNode node: nodes) {
					node.output(context);
				}
				jsonGenerator.writeEndArray();
			}
			//输出“事件”
			if (!eventNodes.isEmpty()) {
				jsonGenerator.writeFieldName(EVTS);
				jsonGenerator.writeStartArray();
				for (EventNode node: eventNodes) {
					node.output(context);
				}
				jsonGenerator.writeEndArray();
			}
			jsonGenerator.writeEndObject();
			
			jsonGenerator.flush();
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
				+ ((attributeNodes == null) ? 0 : attributeNodes.hashCode());
		result = prime * result
				+ ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result
				+ ((eventNodes == null) ? 0 : eventNodes.hashCode());
		result = prime * result
				+ ((layoutAttribute == null) ? 0 : layoutAttribute.hashCode());
		result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
		result = prime
				* result
				+ ((positionAttribute == null) ? 0 : positionAttribute
						.hashCode());
		result = prime * result + ((ruleId == null) ? 0 : ruleId.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		XmlNode other = (XmlNode) obj;
		if (attributeNodes == null) {
			if (other.attributeNodes != null)
				return false;
		} else if (!attributeNodes.equals(other.attributeNodes))
			return false;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (eventNodes == null) {
			if (other.eventNodes != null)
				return false;
		} else if (!eventNodes.equals(other.eventNodes))
			return false;
		if (layoutAttribute == null) {
			if (other.layoutAttribute != null)
				return false;
		} else if (!layoutAttribute.equals(other.layoutAttribute))
			return false;
		if (nodes == null) {
			if (other.nodes != null)
				return false;
		} else if (!nodes.equals(other.nodes))
			return false;
		if (positionAttribute == null) {
			if (other.positionAttribute != null)
				return false;
		} else if (!positionAttribute.equals(other.positionAttribute))
			return false;
		if (ruleId == null) {
			if (other.ruleId != null)
				return false;
		} else if (!ruleId.equals(other.ruleId))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String
				.format("XmlNode [ruleId=%s, text=%s, attributes=%s, attributeNodes=%s, eventNodes=%s, nodes=%s, layoutAttribute=%s, positionAttribute=%s]",
						ruleId, text, attributes, attributeNodes, eventNodes,
						nodes, layoutAttribute, positionAttribute);
	}
	
}
