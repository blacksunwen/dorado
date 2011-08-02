package com.bstek.dorado.idesupport.template;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.idesupport.model.ClientEvent;
import com.bstek.dorado.util.Assert;
import com.bstek.dorado.view.output.Outputter;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-18
 */
public class RuleTemplate {
	private RuleTemplate[] parents;
	private Set<RuleTemplate> subRuleTemplates;

	private String type;
	private XmlParser parser;
	private Outputter outputter;

	private String name;
	private String nodeName;
	private boolean _abstract;
	private Boolean supportsCustomProperty;

	private String category;
	private String[] robots;
	private int sortFactor;
	private String scope = "protected";
	private String reserve;

	private boolean global;
	private boolean initialized;
	private boolean inheritanceProcessed;

	private Map<String, PropertyTemplate> primitiveProperties = new LinkedHashMap<String, PropertyTemplate>();
	private Map<String, PropertyTemplate> properties = new LinkedHashMap<String, PropertyTemplate>();
	private Map<String, ClientEvent> clientEvents = new LinkedHashMap<String, ClientEvent>();
	private Map<String, ChildTemplate> children = new LinkedHashMap<String, ChildTemplate>();

	public RuleTemplate(String name) {
		Assert.notEmpty(name);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public RuleTemplate[] getParents() {
		return parents;
	}

	public void setParents(RuleTemplate[] parents) {
		if (this.parents != null) {
			for (RuleTemplate parent : this.parents) {
				if (parent.subRuleTemplates != null)
					parent.subRuleTemplates.remove(this);
			}
		}
		this.parents = parents;
		if (parents != null) {
			for (RuleTemplate parent : parents) {
				if (parent.subRuleTemplates == null) {
					parent.subRuleTemplates = new LinkedHashSet<RuleTemplate>();
				}
				parent.subRuleTemplates.add(this);
			}
		}
	}

	public RuleTemplate[] getSubRuleTemplates() {
		RuleTemplate[] ruleTemplates = new RuleTemplate[0];
		if (subRuleTemplates != null) {
			ruleTemplates = subRuleTemplates.toArray(ruleTemplates);
		}
		return ruleTemplates;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public boolean isAbstract() {
		return _abstract;
	}

	public void setAbstract(boolean _abstract) {
		this._abstract = _abstract;
	}

	public Boolean getSupportsCustomProperty() {
		return supportsCustomProperty;
	}

	public void setSupportsCustomProperty(Boolean supportsCustomProperty) {
		this.supportsCustomProperty = supportsCustomProperty;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public XmlParser getParser() {
		return parser;
	}

	public void setParser(XmlParser parser) {
		this.parser = parser;
	}

	public Outputter getOutputter() {
		return outputter;
	}

	public void setOutputter(Outputter outputter) {
		this.outputter = outputter;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String[] getRobots() {
		return robots;
	}

	public void setRobots(String[] robots) {
		this.robots = robots;
	}

	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public Map<String, PropertyTemplate> getPrimitiveProperties() {
		return primitiveProperties;
	}

	public void addPrimitiveProperties(
			Collection<PropertyTemplate> primitiveProperties) {
		for (PropertyTemplate property : primitiveProperties) {
			addPrimitiveProperty(property);
		}
	}

	public void addPrimitiveProperty(PropertyTemplate property) {
		primitiveProperties.put(property.getName(), property);
	}

	public PropertyTemplate getPrimitiveProperty(String name) {
		return primitiveProperties.get(name);
	}

	public Map<String, PropertyTemplate> getProperties() {
		return properties;
	}

	public PropertyTemplate getProperty(String name) {
		return properties.get(name);
	}

	public void addProperties(Collection<PropertyTemplate> properties) {
		for (PropertyTemplate property : properties) {
			addProperty(property);
		}
	}

	public void addProperty(PropertyTemplate property) {
		properties.put(property.getName(), property);
	}

	public Map<String, ClientEvent> getClientEvents() {
		return clientEvents;
	}

	public ClientEvent getClientEvent(String name) {
		return clientEvents.get(name);
	}

	public void addClientEvents(Collection<ClientEvent> clientEvents) {
		for (ClientEvent clientEvent : clientEvents) {
			addClientEvent(clientEvent);
		}
	}

	public void addClientEvent(ClientEvent clientEvent) {
		clientEvents.put(clientEvent.getName(), clientEvent);
	}

	public Map<String, ChildTemplate> getChildren() {
		return children;
	}

	public ChildTemplate getChild(String childName) {
		return children.get(childName);
	}

	public void addChildren(Collection<ChildTemplate> children) {
		for (ChildTemplate childTemplate : children) {
			addChild(childTemplate);
		}
	}

	public void addChild(ChildTemplate childTemplate) {
		children.put(childTemplate.getName(), childTemplate);
	}

	public Map<String, PropertyTemplate> getFinalPrimitiveProperties() {
		if (parents != null && parents.length > 0) {
			Map<String, PropertyTemplate> finalPrimitiveProperties = new LinkedHashMap<String, PropertyTemplate>();
			for (RuleTemplate parent : parents) {
				finalPrimitiveProperties.putAll(parent
						.getFinalPrimitiveProperties());
			}
			finalPrimitiveProperties.putAll(primitiveProperties);
			return finalPrimitiveProperties;
		} else {
			return primitiveProperties;
		}
	}

	public Map<String, PropertyTemplate> getFinalProperties() {
		if (parents != null && parents.length > 0) {
			Map<String, PropertyTemplate> finalProperties = new LinkedHashMap<String, PropertyTemplate>();
			for (RuleTemplate parent : parents) {
				finalProperties.putAll(parent.getFinalProperties());
			}
			finalProperties.putAll(properties);
			return finalProperties;
		} else {
			return properties;
		}
	}

	public Map<String, ClientEvent> getFinalClientEvents() {
		if (parents != null && parents.length > 0) {
			Map<String, ClientEvent> finalClientEvents = new LinkedHashMap<String, ClientEvent>();
			for (RuleTemplate parent : parents) {
				finalClientEvents.putAll(parent.getFinalClientEvents());
			}
			finalClientEvents.putAll(clientEvents);
			return finalClientEvents;
		} else {
			return clientEvents;
		}
	}

	public Map<String, ChildTemplate> getFinalChildren() {
		if (parents != null && parents.length > 0) {
			Map<String, ChildTemplate> finalChildren = new LinkedHashMap<String, ChildTemplate>();
			for (RuleTemplate parent : parents) {
				finalChildren.putAll(parent.getFinalChildren());
			}
			finalChildren.putAll(children);
			return finalChildren;
		} else {
			return children;
		}
	}

	public void processInheritance() throws Exception {
		if (inheritanceProcessed)
			return;
		inheritanceProcessed = true;
		if (parents != null && parents.length > 0) {
			Map<String, Object> props = new HashMap<String, Object>();
			applyProperties(this, props, "nodeName,supportsCustomProperty");
			for (RuleTemplate parent : parents) {
				parent.processInheritance();
				applyProperties(parent, this, "nodeName,supportsCustomProperty");
			}
			applyProperties(props, this, "nodeName,supportsCustomProperty");
		}
	}

	private void applyProperties(Object source, Object target,
			String propertyNames) throws Exception {
		if (StringUtils.isNotEmpty(propertyNames)) {
			for (String propertyName : StringUtils.split(propertyNames, ',')) {
				applyProperty(source, target, propertyName);
			}
		}
	}

	private void applyProperty(Object source, Object target, String propertyName)
			throws Exception {
		Object value = PropertyUtils.getProperty(source, propertyName);
		if (value != null)
			PropertyUtils.setProperty(target, propertyName, value);
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public int getSortFactor() {
		return sortFactor;
	}

	public void setSortFactor(int sortFactor) {
		this.sortFactor = sortFactor;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
}
