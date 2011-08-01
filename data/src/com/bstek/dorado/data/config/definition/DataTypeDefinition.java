package com.bstek.dorado.data.config.definition;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.common.Namable;
import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.DefaultDefinitionReference;
import com.bstek.dorado.config.definition.Definition;
import com.bstek.dorado.config.definition.DefinitionUtils;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.data.Constants;
import com.bstek.dorado.data.config.DataTypeName;
import com.bstek.dorado.data.config.xml.DataXmlConstants;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.RudeDataType;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.util.CloneUtils;

/**
 * DataType的配置声明对象。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 8, 2008
 * @see com.bstek.dorado.data.type.DataType
 */
public class DataTypeDefinition extends ListenableObjectDefinition implements
		Namable {
	private static final String SELF_AGGREGATION_DATA_TYPE_SCTION = '[' + PropertyDef.SELF_DATA_TYPE_NAME + ']';

	private String name;
	private String id;
	private Class<?> matchType;
	private Class<?> creationType;
	private boolean global = true;
	private Map<String, ObjectDefinition> propertyDefs;
	private boolean isAggregationType;

	public DataTypeDefinition() {
		setCacheCreatedObject(true);
	}

	/**
	 * 返回DataType的名称。
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置DataType的名称。
	 */
	public void setName(String name) {
		this.name = name;
		if (StringUtils.isNotEmpty(name)) {
			DataTypeName dataTypeName = new DataTypeName(name);
			isAggregationType = (dataTypeName.getSubDataTypes().length == 1);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		if (StringUtils.isNotEmpty(id)) {
			setBeanId(Constants.SCOPE_DATA_TYPE_PREFIX + id);
		}
	}

	public boolean isAggregationType() {
		return isAggregationType;
	}

	/**
	 * 返回DataType的相匹配类型。
	 */
	public Class<?> getMatchType() {
		return matchType;
	}

	/**
	 * 设置DataType的相匹配类型。
	 */
	public void setMatchType(Class<?> matchType) {
		this.matchType = matchType;
	}

	/**
	 * 返回DataType的相匹配的可实例化类型。
	 */
	public Class<?> getCreationType() {
		return creationType;
	}

	/**
	 * 返回DataType的相匹配的可实例化类型。
	 */
	public void setCreationType(Class<?> creationType) {
		this.creationType = creationType;
	}

	/**
	 * 返回该DataType是否是一个全局对象。
	 */
	public boolean isGlobal() {
		return global;
	}

	/**
	 * 设置该DataType是否是一个全局对象。
	 */
	public void setGlobal(boolean global) {
		this.global = global;
	}

	/**
	 * 向DataType的声明中添加一个属性声明对象。
	 * 
	 * @param name
	 *            属性声明的名称
	 * @param propertyDef
	 *            要添加的属性声明对象
	 */
	public void addPropertyDef(String name, ObjectDefinition propertyDef) {
		if (propertyDefs == null) {
			propertyDefs = new LinkedHashMap<String, ObjectDefinition>();
		}
		propertyDefs.put(name, propertyDef);
	}

	/**
	 * 根据名称返回一个属性声明对象。
	 * 
	 * @param name
	 *            属性声明的名称
	 * @return 相应的属性声明对象
	 */
	public ObjectDefinition getPropertyDef(String name) {
		if (propertyDefs != null) {
			return propertyDefs.get(name);
		} else {
			return null;
		}
	}

	/**
	 * 返回DataType声明中所有的属性声明对象的Map集合。<br>
	 * 其中Map集合的键为属性声明的名称，值为相应的属性声明对象。
	 */
	public Map<String, ObjectDefinition> getPropertyDefs() {
		return propertyDefs;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doInitObject(Object object, CreationInfo creationInfo,
			CreationContext context) throws Exception {
		DataType dataType = (DataType) object;

		RudeDataType rudeDataType = (RudeDataType) dataType;
		rudeDataType.setName(name);
		if (StringUtils.isNotEmpty(id)) {
			rudeDataType.setId(id);
		}

		Class<?> matchType = (Class<?>) creationInfo.getUserData("matchType");
		if (matchType != null)
			rudeDataType.setMatchType(matchType);

		Class<?> creationType = (Class<?>) creationInfo
				.getUserData("creationType");
		if (creationType != null)
			rudeDataType.setCreationType(creationType);

		Map<String, ObjectDefinition> propertyDefs = (Map<String, ObjectDefinition>) creationInfo
				.getUserData("propertyDefs");
		if (propertyDefs != null && !propertyDefs.isEmpty()) {
			if (dataType instanceof EntityDataType) {
				EntityDataType entityDataType = (EntityDataType) dataType;
				for (ObjectDefinition definition : propertyDefs.values()) {
					Map<String, Object> properties = definition.getProperties();

					Object tempDataType = properties
							.get(DataXmlConstants.ATTRIBUTE_DATA_TYPE);
					if (tempDataType != null
							&& tempDataType instanceof DefaultDefinitionReference<?>) {
						DefaultDefinitionReference<?> dataTypeRef = (DefaultDefinitionReference<?>) tempDataType;
						String nameRef = dataTypeRef.getName();
						if (nameRef.equals(PropertyDef.SELF_DATA_TYPE_NAME)) {
							dataTypeRef.setName(name);
						} else if (nameRef
								.indexOf(SELF_AGGREGATION_DATA_TYPE_SCTION) >= 0) {
							nameRef = nameRef.replace(
									SELF_AGGREGATION_DATA_TYPE_SCTION,
									'[' + name + ']');
							dataTypeRef.setName(nameRef);
						}
					}

					PropertyDef propertyDef = (PropertyDef) DefinitionUtils
							.getRealValue(definition, context);
					entityDataType.addPropertyDef(propertyDef);
				}
			} else {
				throw new IllegalArgumentException(
						"Can not add PropertyDef for DataType [" + name + "].");
			}
		}

		super.doInitObject(dataType, creationInfo, context);

		if (dataType instanceof EntityDataType) {
			EntityDataType entityDataType = ((EntityDataType) dataType);
			if (entityDataType.isAutoCreatePropertyDefs()) {
				entityDataType.createPropertyDefinitons();
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initCreationInfo(CreationInfo creationInfo,
			ObjectDefinition definition, boolean processConstrInfos)
			throws Exception {
		super.initCreationInfo(creationInfo, definition, processConstrInfos);
		DataTypeDefinition dataTypeDefinition = (DataTypeDefinition) definition;

		if (processConstrInfos) {
			// process MatchType and CreationType
			Class<?> matchType = dataTypeDefinition.getMatchType();
			if (matchType != null) {
				creationInfo.setUserData("matchType", matchType);
			}
			Class<?> creationType = dataTypeDefinition.getCreationType();
			if (creationType != null) {
				creationInfo.setUserData("creationType", creationType);
			}
		}

		// process PropertyDefs
		if (isAggregationType) {
			return;
		}

		Map<String, ObjectDefinition> allPropertyDefs = (Map<String, ObjectDefinition>) creationInfo
				.getUserData("propertyDefs");
		if (allPropertyDefs == null) {
			allPropertyDefs = new LinkedHashMap<String, ObjectDefinition>();
			creationInfo.setUserData("propertyDefs", allPropertyDefs);
		}

		Map<String, ObjectDefinition> propertyDefs = dataTypeDefinition
				.getPropertyDefs();
		if (propertyDefs != null) {
			for (Map.Entry<String, ObjectDefinition> entry : propertyDefs
					.entrySet()) {
				String name = entry.getKey();
				ObjectDefinition propertyDef = entry.getValue();

				ObjectDefinition parentProperty = allPropertyDefs.get(name);
				if (parentProperty != null) {
					propertyDef = (ObjectDefinition) CloneUtils
							.clone(propertyDef);
					Definition[] originParents = propertyDef.getParents();
					Definition[] parents;
					if (originParents == null) {
						parents = new Definition[] { parentProperty };
					} else {
						parents = new Definition[originParents.length + 1];
						parents[0] = parentProperty;
						System.arraycopy(originParents, 0, parents, 1,
								originParents.length);
					}
					propertyDef.setParents(parents);
				}
				allPropertyDefs.put(name, propertyDef);
			}
		}
	}

	@Override
	protected void setObjectProperty(Object object, String property,
			Object value, CreationContext context) throws Exception {
		try {
			super.setObjectProperty(object, property, value, context);
		} catch (NoSuchMethodException e) {
			if (DataXmlConstants.ATTRIBUTE_ELEMENT_DATA_TYPE.equals(property)
					&& !(object instanceof EntityDataType)) {
				// do nothing
			} else {
				throw e;
			}
		}
	}

}
