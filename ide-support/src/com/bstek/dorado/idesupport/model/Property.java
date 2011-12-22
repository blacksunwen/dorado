package com.bstek.dorado.idesupport.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-18
 */
public class Property {
	private String name;
	private String type;
	private Object defaultValue;
	private int highlight;
	private boolean fixed;
	private String[] enumValues;
	private String editor;
	private Reference reference;
	private boolean visible = true;
	private CompositeType compositeType = CompositeType.Unsupport;
	private Map<String, Property> properties;
	private Property parentProperty;
	private String reserve;
	private Object userData;

	public Property() {
	}

	public Property(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getEnumValues() {
		return enumValues;
	}

	public void setEnumValues(String[] enumValues) {
		this.enumValues = enumValues;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int getHighlight() {
		return highlight;
	}

	public void setHighlight(int highlight) {
		this.highlight = highlight;
	}

	public boolean isFixed() {
		return fixed;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public CompositeType getCompositeType() {
		return compositeType;
	}

	public void setCompositeType(CompositeType compositeType) {
		this.compositeType = compositeType;
	}

	public Map<String, Property> getProperties() {
		if (properties == null) {
			properties = new LinkedHashMap<String, Property>();
		}
		return properties;
	}

	public Property getProperty(String name) {
		return getProperties().get(name);
	}

	public void addProperties(Collection<Property> properties) {
		for (Property property : properties) {
			addProperty(property);
			property.setParentProperty(this);
		}
	}

	public void addProperty(Property property) {
		getProperties().put(property.getName(), property);
	}

	public Property getParentProperty() {
		return parentProperty;
	}

	public void setParentProperty(Property parentProperty) {
		this.parentProperty = parentProperty;
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
