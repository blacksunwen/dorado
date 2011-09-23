package com.bstek.dorado.idesupport.model;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.map.UnmodifiableMap;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.util.Assert;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-18
 */
public class Rule {
	private static transient Comparator<String> propertyComparator;
	static {
		propertyComparator = new Comparator<String>() {
			public int compare(String name1, String name2) {
				return name1.compareTo(name2);
			}
		};
	}

	private Rule[] parents;
	private Set<Rule> subRules;
	private boolean _abstract;

	private String name;
	private String nodeName;
	private String type;
	private boolean supportsMetaProperty;
	private String category;
	private String[] robots;
	private int sortFactor = Integer.MAX_VALUE;
	private String reserve;
	private Object userData;

	private Map<String, Property> primitiveProperties = new TreeMap<String, Property>(
			propertyComparator);
	private Map<String, Property> properties = new TreeMap<String, Property>(
			propertyComparator);
	private Map<String, ClientEvent> clientEvents = new LinkedHashMap<String, ClientEvent>();
	private Map<String, Child> children = new TreeMap<String, Child>(
			new Comparator<String>() {
				public int compare(String name1, String name2) {
					Child child1 = childMap.get(name1);
					Child child2 = childMap.get(name2);
					if (child1 == null || child2 == null) {
						return 0;
					} else {
						Rule rule1 = child1.getRule();
						Rule rule2 = child2.getRule();
						int result = rule1.getSortFactor()
								- rule2.getSortFactor();
						if (result == 0) {
							result = rule1.getName().compareTo(rule2.getName());
						}
						return result;
					}
				}

			});
	private Map<String, Child> childMap = new HashMap<String, Child>();

	public Rule(String name) {
		Assert.notEmpty(name);
		this.name = name;
	}

	public Rule[] getParents() {
		return parents;
	}

	public void setParents(Rule[] parents) {
		if (this.parents != null) {
			for (Rule parent : this.parents) {
				if (parent.subRules != null)
					parent.subRules.remove(this);
			}
		}
		this.parents = parents;
		if (parents != null) {
			for (Rule parent : parents) {
				if (parent.subRules == null) {
					parent.subRules = new LinkedHashSet<Rule>();
				}
				parent.subRules.add(this);
			}
		}
	}

	public Rule[] getSubRules() {
		Rule[] rules = new Rule[0];
		if (subRules != null) {
			rules = subRules.toArray(rules);
		}
		return rules;
	}

	public boolean isSubRuleOf(Rule parent) {
		Rule rule = this;
		if (rule.parents != null) {
			if (ArrayUtils.indexOf(rule.parents, parent) >= 0)
				return true;
			for (Rule p : rule.parents) {
				if (p.isSubRuleOf(parent))
					return true;
			}
		}
		return false;
	}

	public boolean isAbstract() {
		return _abstract;
	}

	public void setAbstract(boolean _abstract) {
		this._abstract = _abstract;
	}

	public String getName() {
		return name;
	}

	public String getNodeName() {
		return StringUtils.isEmpty(nodeName) ? name : nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isSupportsMetaProperty() {
		return supportsMetaProperty;
	}

	public void setSupportsMetaProperty(boolean supportsMetaProperty) {
		this.supportsMetaProperty = supportsMetaProperty;
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

	public Map<String, Property> getPrimitiveProperties() {
		return primitiveProperties;
	}

	public void addPrimitiveProperties(Collection<Property> primitiveProperties) {
		for (Property property : primitiveProperties) {
			addPrimitiveProperty(property);
		}
	}

	public void addPrimitiveProperty(Property property) {
		primitiveProperties.put(property.getName(), property);
	}

	public Property getPrimitiveProperty(String name) {
		return primitiveProperties.get(name);
	}

	public Map<String, Property> getProperties() {
		return properties;
	}

	public Property getProperty(String name) {
		return properties.get(name);
	}

	public void addProperties(Collection<Property> properties) {
		for (Property property : properties) {
			addProperty(property);
		}
	}

	public void addProperty(Property property) {
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

	@SuppressWarnings("unchecked")
	public Map<String, Child> getChildren() {
		return UnmodifiableMap.decorate(children);
	}

	public Child getChild(String name) {
		return children.get(name);
	}

	public void addChild(Child child) {
		childMap.put(child.getName(), child);
		children.put(child.getName(), child);
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

	public Object getUserData() {
		return userData;
	}

	public void setUserData(Object userData) {
		this.userData = userData;
	}
}