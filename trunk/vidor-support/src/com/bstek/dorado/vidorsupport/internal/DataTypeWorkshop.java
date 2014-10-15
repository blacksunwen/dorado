package com.bstek.dorado.vidorsupport.internal;

import java.beans.PropertyDescriptor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceUtils;
import com.bstek.dorado.data.JsonUtils;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.SimpleDataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;
import com.bstek.dorado.util.clazz.ClassUtils;

public class DataTypeWorkshop extends AbstractDataTypeWorkshop {
	
	private static final Log log = LogFactory.getLog(DataTypeWorkshop.class);
	
	private DataTypeManager dataTypeManager;
	private Set<String> hiddenDataTypeNames = null;
	private Collection<String> hiddenDataTypeLocations = new ArrayList<String>();
	
	public void setDataTypeManager(DataTypeManager dataTypeManager) {
		this.dataTypeManager = dataTypeManager;
	}
	
	
	public Collection<String> names(Map<String, Object> parameter, boolean containsBaseType) throws Exception {
		Set<String> names = dataTypeManager.getDataTypeNames();
		Collection<String> customNames = Utils.filter(names, parameter);
		if (!containsBaseType) {
			Collection<String> baseNames = this.baseNames();
			customNames.removeAll(baseNames);
			customNames.removeAll(getHiddenDataTypeNames());
		}
		return customNames;
	}
	
	protected Set<String> getHiddenDataTypeNames() {
		if (hiddenDataTypeNames == null) {
			synchronized (this.getClass()) {
				if (hiddenDataTypeNames == null) {
					hiddenDataTypeNames = new LinkedHashSet<String>();
					for (String location: hiddenDataTypeLocations) {
						try {
							Resource resource = ResourceUtils.getResource(location);
							List<String> names = this.loadDataTypes(resource);
							hiddenDataTypeNames.addAll(names);
						} catch (Exception e) {
							log.error("error when load dataTypes from [" + location + "]", e);
						}
					}
				}
			}
		}
		
		return hiddenDataTypeNames;
	}
	
	public Collection<String> getHiddenDataTypeLocations() {
		return hiddenDataTypeLocations;
	}
	public void setHiddenDataTypeLocations(
			Collection<String> hiddenDataTypeLocations) {
		this.hiddenDataTypeLocations = hiddenDataTypeLocations;
	}

	public String reflection(String json) throws Exception {
		ObjectNode objectNode = this.doReflection(json);
		return objectNode.toString();
	}
	
	public ObjectNode doReflection(String json) throws Exception {
		ObjectNode objectNode = (ObjectNode) JsonUtils.getObjectMapper().readTree(json);
		this.doReflection(objectNode, true);
		return objectNode;
	}
	
	@SuppressWarnings("rawtypes")
	protected void doReflection(ObjectNode objectNode, boolean force) throws Exception{
		String reflectType = null;
		String matchType = this.getAttributeValue(objectNode, "matchType");
		String creationType = this.getAttributeValue(objectNode, "creationType");
		if (creationType == null || creationType.length() == 0) {
			reflectType = matchType;
		} else {
			reflectType = creationType;
		}
		
		if (reflectType == null || reflectType.length() == 0)
			return;
		
		if (force ||
				"true".equals(this.getAttributeValue(objectNode, "autoCreatePropertyDefs"))) {
			Class reflectClass = ClassUtils.forName(reflectType);
			this.doReflection(objectNode, reflectClass);
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected ObjectNode reflection(Class reflectClass) throws Exception {
		ObjectNode dataTypeNode = JsonUtils.getObjectMapper().createObjectNode();
		ArrayNode propertyDefNodes = JsonUtils.getObjectMapper().createArrayNode();
		dataTypeNode.put("nodes", propertyDefNodes);
		
		Set<String> propertyDefNames = new HashSet<String>();
		propertyDefNames.add("class");
		PropertyDescriptor[] propertyDescriptors = PropertyUtils
				.getPropertyDescriptors(reflectClass);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			String name = propertyDescriptor.getName();
			if (propertyDefNames.contains(name))
				continue;
			
			String dataTypeName = this.dataTypeOfProperty(propertyDescriptor);
			if (dataTypeName != null && dataTypeName.length() > 0) {
				this.addPropertyNode(propertyDefNodes, name, dataTypeName);
				propertyDefNames.add(name);
			}
		}
		
		return dataTypeNode;
	}
	
	@SuppressWarnings("rawtypes")
	protected void doReflection(ObjectNode dataTypeNode, Class reflectClass) throws Exception {
		ObjectNode from = this.reflection(reflectClass);
		this.doMerge(dataTypeNode, from);
	}
	
	protected String dataTypeOfProperty(PropertyDescriptor propertyDescriptor) throws Exception {
		String dataTypeName = null;
		DataType propertyDataType = dataTypeManager
				.getDataType(propertyDescriptor.getPropertyType());
		if (propertyDataType != null) {
			if (propertyDataType instanceof SimpleDataType) {
				dataTypeName = propertyDataType.getName();
			}
		}
		
		return dataTypeName;
	}
	
	public String json(String name) throws Exception {
		ObjectNode jsonObject = this.getFromGlobal(name, null);
		
		if (jsonObject != null) {
			String json = jsonObject.toString();
			return json;
		}
		
		throw new Exception("No dataType definition be found.[" + name + "]");
	}
	
	
	protected ObjectNode getFromGlobal(String name, Map<String, ObjectNode> jsonObjectMap) throws Exception {
		ObjectNode dataTypeNode = null;
		String n = this.dataTypeName(name);
		if (jsonObjectMap == null) {
			jsonObjectMap = new HashMap<String, ObjectNode>();
		}
		
		DataTypeDefinition dataType = dataTypeManager.getDataTypeDefinitionManager().getDefinition(n);
		Resource resource = dataType.getResource();
		if (resource != null) {
			URL url = resource.getURL();
			String json = this.json(url, name, "DataType");
			dataTypeNode = (ObjectNode)JsonUtils.getObjectMapper().readTree(json);
			this.doMergeFromGlobal(dataTypeNode, jsonObjectMap);
		}
		return dataTypeNode;
	}

	
	protected void doMergeFromGlobal(ObjectNode dataTypeNode, Map<String, ObjectNode> jsonObjectMap) throws Exception {
		super.doMergeFromGlobal(dataTypeNode, jsonObjectMap);
		
		//根据java类自动生成属性列表
		this.doReflection(dataTypeNode, false);
	}
}
