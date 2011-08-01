package com.bstek.dorado.data.config.definition;

import java.util.Map;

import com.bstek.dorado.config.definition.DefaultDefinitionReference;
import com.bstek.dorado.config.definition.DefinitionManager;
import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.data.config.DataTypeName;
import com.bstek.dorado.data.config.xml.DataXmlConstants;

/**
 * 与DataType的配置声明对象相关的工具类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 14, 2008
 */
public abstract class DataTypeDefinitionUtils {

	/**
	 * 根据复合DataType名称创建新的DataType配置声明对象。
	 * 
	 * @param dataTypeDefinitionManager
	 *            DataType配置声明管理器
	 * @param dataTypeName
	 *            复合的DataType名称
	 * @return 新的DataType配置声明对象
	 */
	public static DataTypeDefinition create(
			DefinitionManager<DataTypeDefinition> dataTypeDefinitionManager,
			DataTypeName dataTypeName) {
		if (dataTypeName.hasSubDataType()) {
			DataTypeDefinition dataType = new DataTypeDefinition();
			dataType.setName(dataTypeName.getFullName());

			DefinitionReference<?> dataTypeRef = new DefaultDefinitionReference<DataTypeDefinition>(
					dataTypeDefinitionManager, dataTypeName.getDataType());
			dataType.setParentReferences(new DefinitionReference<?>[] { dataTypeRef });

			String[] subDataTypeNames = dataTypeName.getSubDataTypes();
			Map<String, Object> properties = dataType.getProperties();
			if (subDataTypeNames.length == 1) {
				DefinitionReference<?> elementDataType = new DefaultDefinitionReference<DataTypeDefinition>(
						dataTypeDefinitionManager, subDataTypeNames[0]);
				properties.put(DataXmlConstants.ATTRIBUTE_ELEMENT_DATA_TYPE,
						elementDataType);
			} else if (subDataTypeNames.length == 2) {
				DefinitionReference<?> keyDataType = new DefaultDefinitionReference<DataTypeDefinition>(
						dataTypeDefinitionManager, subDataTypeNames[0]);
				DefinitionReference<?> valueDataType = new DefaultDefinitionReference<DataTypeDefinition>(
						dataTypeDefinitionManager, subDataTypeNames[1]);

				properties.put(DataXmlConstants.ATTRIBUTE_KEY_DATA_TYPE,
						keyDataType);
				properties.put(DataXmlConstants.ATTRIBUTE_VALUE_DATA_TYPE,
						valueDataType);
			} else {
				throw new IllegalArgumentException("Illegal DataType name ["
						+ dataTypeName.getFullName() + "].");
			}
			return dataType;
		} else {
			throw new IllegalArgumentException("[" + dataTypeName.getFullName()
					+ "] should be a composite name.");
		}
	}

	/**
	 * 根据复合DataType名称创建新的DataType配置声明对象。
	 * 
	 * @param dataTypeDefinitionManager
	 *            DataType配置声明管理器
	 * @param dataTypeName
	 *            复合的DataType名称
	 * @return 新的DataType配置声明对象
	 */
	public static DataTypeDefinition create(
			DataTypeDefinitionManager dataTypeDefinitionManager,
			String dataTypeName) {
		return create(dataTypeDefinitionManager, new DataTypeName(dataTypeName));
	}
}
