/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.idesupport.template;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bstek.dorado.idesupport.model.CompositeType;

public class PropertyTemplate {
	private String name;
	private String type;
	private Object defaultValue;
	private Boolean fixed;
	private String[] enumValues;
	private String editor;
	private Integer highlight;
	private ReferenceTemplate reference;
	private Boolean ignored;
	private Boolean visible;
	private CompositeType compositeType = CompositeType.Unsupport;
	private Map<String, PropertyTemplate> properties;
	private String reserve;

	public PropertyTemplate() {
	}

	public PropertyTemplate(String name) {
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

	public Integer getHighlight() {
		return highlight;
	}

	public void setHighlight(Integer highlight) {
		this.highlight = highlight;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Boolean getFixed() {
		return fixed;
	}

	public void setFixed(Boolean fixed) {
		this.fixed = fixed;
	}

	public ReferenceTemplate getReference() {
		return reference;
	}

	public void setReference(ReferenceTemplate reference) {
		this.reference = reference;
	}

	public Boolean getIgnored() {
		return ignored;
	}

	public void setIgnored(Boolean ignored) {
		this.ignored = ignored;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public CompositeType getCompositeType() {
		return compositeType;
	}

	public void setCompositeType(CompositeType compositeType) {
		this.compositeType = compositeType;
	}

	public Map<String, PropertyTemplate> getProperties() {
		if (properties == null) {
			properties = new LinkedHashMap<String, PropertyTemplate>();
		}
		return properties;
	}

	public PropertyTemplate getProperty(String name) {
		return getProperties().get(name);
	}

	public void addProperties(Collection<PropertyTemplate> properties) {
		for (PropertyTemplate property : properties) {
			addProperty(property);
		}
	}

	public void addProperty(PropertyTemplate property) {
		getProperties().put(property.getName(), property);
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

}
