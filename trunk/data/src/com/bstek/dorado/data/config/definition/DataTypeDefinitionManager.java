package com.bstek.dorado.data.config.definition;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.config.definition.DefaultDefinitionManager;
import com.bstek.dorado.config.definition.Definition;
import com.bstek.dorado.config.definition.DefinitionManager;
import com.bstek.dorado.data.config.DataTypeName;
import com.bstek.dorado.util.Assert;

/**
 * DataType配置声明管理器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 9, 2008
 */
public class DataTypeDefinitionManager extends
		DefaultDefinitionManager<DataTypeDefinition> {
	private static Log logger = LogFactory
			.getLog(DataTypeDefinitionManager.class);
	/**
	 * 以DataType的相关Java类型作为键值的DataType集合，
	 * 即以DataType的getMatchType()的返回值作为键值的DataType集合。
	 */
	private Map<Type, DataTypeDefinition> typeMap = new Hashtable<Type, DataTypeDefinition>();
	private Map<String, Type> nameTypeMap = new HashMap<String, Type>();

	public DataTypeDefinitionManager() {
		super();
	}

	public DataTypeDefinitionManager(
			DefinitionManager<DataTypeDefinition> parent) {
		super(parent);
	}

	private boolean isConflicting(DataTypeDefinition dataType,
			DataTypeDefinition conflictingDataType) {
		boolean conflicting = true;
		Definition[] parents = dataType.getParents();
		if (parents != null) {
			for (Definition parent : parents) {
				if (parent == conflictingDataType) {
					conflicting = false;
				} else {
					conflicting = isConflicting((DataTypeDefinition) parent,
							conflictingDataType);
				}
				if (!conflicting)
					break;
			}
		}
		return conflicting;
	}

	@Override
	public void registerDefinition(String name, DataTypeDefinition definition) {
		Assert.notEmpty(name);

		DataTypeDefinition registered = super.getDefinition(name);
		if (registered != null && registered.getId().equals(definition.getId())) {
			throw new IllegalStateException("The DataType [" + name
					+ "] is already registered!");
		}

		super.registerDefinition(name, definition);

		DataTypeDefinition dataType = definition;
		if (dataType.isGlobal()) {
			try {
				registerMatchType(dataType);
			} catch (Exception e) {
				logger.warn(e, e);
			}
		}
	}

	@Override
	public DataTypeDefinition unregisterDefinition(String name) {
		DataTypeDefinition definition = super.unregisterDefinition(name);

		if (definition != null) {
			Type type = nameTypeMap.get(name);
			if (type != null) {
				typeMap.remove(type);
			}
		}
		return definition;
	}

	/**
	 * 根据给定的DataType名称返回相应的DataType的配置声明对象。<br>
	 * 此方法具有具有处理复合类型DataType名称的能力。<br>
	 * 例如：我们已经在管理器中分别注册了List和Map这两中DataType，当我们尝试从管理器中获得一个名为List[Map]的DataType时，
	 * 管理器将会自动创建一个新的DataType与之匹配，并且将这个新的DataType注册到管理器。这样，当我们下次在尝试获得List[Map]时，
	 * 管理器将直接返回该DataType。
	 * 
	 * @param name
	 *            DataType的名称
	 * @return DataType的配置声明对象
	 */
	@Override
	public DataTypeDefinition getDefinition(String name) {
		DataTypeDefinition definition = getDefinitions().get(name);
		DataTypeDefinition dataType = definition;
		if (dataType == null) {
			DataTypeName dataTypeName = new DataTypeName(name);
			if (dataTypeName.hasSubDataType()) {
				// 确保复合DataType名称中所有依赖的DataType都已被解析并有效。
				boolean allSubDataTypeValidated = true;
				Definition parentDataType = getDefinition(dataTypeName
						.getDataType());
				if (parentDataType != null) {
					String[] subDataTypes = dataTypeName.getSubDataTypes();
					for (int i = 0; i < subDataTypes.length; i++) {
						if (getDefinition(subDataTypes[i]) == null) {
							allSubDataTypeValidated = false;
							break;
						}
					}
				} else {
					allSubDataTypeValidated = false;
				}

				if (allSubDataTypeValidated) {
					definition = createAggregationDataType(dataTypeName);
					registerDefinition(name, definition);
				}
			}

			if (definition == null) {
				DefinitionManager<DataTypeDefinition> parent = getParent();
				if (parent != null) {
					definition = parent.getDefinition(name);
				}
			}
		}
		return definition;
	}

	protected DataTypeDefinition createAggregationDataType(
			DataTypeName dataTypeName) {
		DataTypeDefinition definition = DataTypeDefinitionUtils.create(this,
				dataTypeName);

		StringBuffer id = new StringBuffer();
		id.append(dataTypeName.getOriginDataType());
		id.append(DataTypeName.BRACKET_LEFT);
		int i = 0;
		for (String subDataType : dataTypeName.getSubDataTypes()) {
			if (i > 0) {
				id.append(DataTypeName.SEPARATOR);
			}
			id.append(getDefinition(subDataType).getId());
			i++;
		}
		id.append(DataTypeName.BRACKET_RIGHT);
		definition.setId(id.toString());

		return definition;
	}

	private void registerMatchType(DataTypeDefinition dataTypeDefinition)
			throws Exception {
		Type matchType = dataTypeDefinition.getMatchType();
		if (matchType != null) {
			DataTypeDefinition conflictingDataType = typeMap.get(matchType);
			if (conflictingDataType != null) {
				boolean conflicting = isConflicting(dataTypeDefinition,
						conflictingDataType);
				if (conflicting) {
					throw new IllegalArgumentException("DataType \""
							+ dataTypeDefinition.getName()
							+ "\"'s relavantType is conflict with DataType \""
							+ conflictingDataType.getName() + "\"!");
				}
			}
		}

		if (matchType != null) {
			typeMap.put(matchType, dataTypeDefinition);
			nameTypeMap.put(dataTypeDefinition.getName(), matchType);
		}
	}

	/**
	 * 根据给定的Class类型返回相应的DataType的配置声明对象。
	 * 
	 * @param type
	 *            Class类型
	 * @return DataType的配置声明对象
	 */
	public DataTypeDefinition getDefinition(Type type) {
		return typeMap.get(type);
	}
}
