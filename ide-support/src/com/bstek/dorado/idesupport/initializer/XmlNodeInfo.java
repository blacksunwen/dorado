package com.bstek.dorado.idesupport.initializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-12-8
 */
public class XmlNodeInfo {
	private List<Class<?>> sourceTypes;

	private String nodeName;
	private String definitionType;
	private Set<String> implTypes = new HashSet<String>();
	private boolean scopable;
	private boolean inheritable;
	private Map<String, String> fixedProperties = new HashMap<String, String>();
	private Map<String, XmlProperty> properties = new HashMap<String, XmlProperty>();
	private Set<XmlSubNode> subNodes = new HashSet<XmlSubNode>();

	public void addSourceType(Class<?> sourceType) {
		if (sourceTypes == null) {
			sourceTypes = new ArrayList<Class<?>>();
		}
		sourceTypes.add(sourceType);
	}

	public List<Class<?>> getSourceTypes() {
		return sourceTypes;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getDefinitionType() {
		return definitionType;
	}

	public void setDefinitionType(String definitionType) {
		this.definitionType = definitionType;
	}

	public Set<String> getImplTypes() {
		return implTypes;
	}

	public boolean isScopable() {
		return scopable;
	}

	public void setScopable(boolean scopable) {
		this.scopable = scopable;
	}

	public boolean isInheritable() {
		return inheritable;
	}

	public void setInheritable(boolean inheritable) {
		this.inheritable = inheritable;
	}

	public Map<String, String> getFixedProperties() {
		return fixedProperties;
	}

	public Map<String, XmlProperty> getProperties() {
		return properties;
	}

	public Set<XmlSubNode> getSubNodes() {
		return subNodes;
	}
}
