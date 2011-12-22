package com.bstek.dorado.data.config.xml;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.config.definition.DirectDefinitionReference;
import com.bstek.dorado.config.xml.XmlParseException;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.config.xml.XmlParserUtils;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.data.config.DataTypeName;
import com.bstek.dorado.data.config.definition.DataProviderDefinition;
import com.bstek.dorado.data.config.definition.DataResolverDefinition;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.util.xml.DomUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-30
 */
public class DataObjectParseHelper {
	private XmlParser dataTypeParser;
	private XmlParser dataProviderParser;
	private XmlParser dataResolverParser;

	/**
	 * 设置全局DataType的解析器。
	 */
	public void setDataTypeParser(XmlParser dataTypeParser) {
		this.dataTypeParser = dataTypeParser;
	}

	/**
	 * 设置私有DataProvider的解析器。
	 */
	public void setDataProviderParser(XmlParser dataProviderParser) {
		this.dataProviderParser = dataProviderParser;
	}

	/**
	 * 设置私有DataResolver的解析器。
	 */
	public void setDataResolverParser(XmlParser dataResolverParser) {
		this.dataResolverParser = dataResolverParser;
	}

	/**
	 * 根据DataType的名称返回一个指向某DataType配置声明对象的引用。 <br>
	 * 此方法与{@link #getDataTypeReference(String)}
	 * 的区别在于，此方法会首先确认被引用的DataType配置声明是真实有效的。
	 * 如果被引用的DataType确有定义但尚未被解析，那么此方法会立即尝试对该DataType的配置信息进行解析。
	 * 
	 * @param name
	 *            DataType的名称
	 * @param context
	 *            解析上下文
	 * @return 配置声明对象的引用
	 * @throws Exception
	 */
	public DefinitionReference<DataTypeDefinition> getDataTypeByName(
			String name, DataParseContext context, boolean subDataTypeAllowed)
			throws Exception {
		DefinitionReference<DataTypeDefinition> definitionReference = null;
		DataTypeDefinition dataType = context.getDataTypeDefinitionManager()
				.getDefinition(name);
		if (dataType == null) {
			dataType = context.getParsedDataTypes().get(name);
		}

		if (definitionReference == null && dataType == null) {
			if (PropertyDef.SELF_DATA_TYPE_NAME.equals(name)) {
				definitionReference = context.getDataTypeReference(name);
			} else {
				DataTypeName dataTypeName = new DataTypeName(name);
				if (dataTypeName.hasSubDataType()) {
					if (!subDataTypeAllowed) {
						throw new ComplexDataTypeNameUnsupportedException(null);
					}
					definitionReference = context.getDataTypeReference(name);
				}
			}
		}

		if (definitionReference == null && dataType == null) {
			Map<String, NodeWrapper> dataTypeMap = context
					.getConfiguredDataTypes();
			NodeWrapper nodeWrapper = dataTypeMap.get(name);
			if (nodeWrapper != null) {
				Element refDataTypeElement = (Element) nodeWrapper.getNode();
				if (context.getParsingNodes().contains(refDataTypeElement)) {
					definitionReference = context.getDataTypeReference(name);
				} else {
					Resource refDataTypeResource = nodeWrapper.getResource();

					Resource oldRecource = context.getResource();
					context.setResource(refDataTypeResource);

					dataType = (DataTypeDefinition) dataTypeParser.parse(
							refDataTypeElement, context);

					context.setResource(oldRecource);
				}
			}
		}

		if (definitionReference == null) {
			if (dataType == null) {
				throw new XmlParseException("Invalid DataType reference \""
						+ name + "\".", context);
			}
			definitionReference = context.getDataTypeReference(name);
		}
		return definitionReference;
	}

	/**
	 * 尝试获得一个XML节点引用到某个DataType，并返回指向该DataType配置声明的引用。<br>
	 * 注意，此处所说的DataType可能是XML节点内部定义的私有DataType。
	 * 
	 * @param propertyName
	 *            可能定义DataType引用信息的属性名。
	 * @param childTagName
	 *            可能定义DataType引用信息的子节点名。可为null，表忽略。
	 *            注意，如果propertyName和childTagName参数所代表的定义方式中都能够找到关于DataType的描述
	 *            ，将以childTagName中的定义为准。
	 * @param element
	 *            XML节点。
	 * @param context
	 *            解析上下文。
	 * @return 指向DataType配置声明的引用。
	 * @throws Exception
	 */
	public DefinitionReference<DataTypeDefinition> getReferencedDataType(
			String propertyName, String childTagName, Element element,
			DataParseContext context) throws Exception {
		DefinitionReference<DataTypeDefinition> definitionReference = null;
		String name = null;
		Node node = XmlParserUtils.getPropertyNode(element, propertyName);
		if (node != null && XmlParserUtils.isSimpleValueProperty(node)) {
			name = XmlParserUtils.getSimpleValue(node);
		}
		if (StringUtils.isNotEmpty(name)) {
			definitionReference = getDataTypeByName(name, context, true);
		} else {
			Element dataTypeElement = null;
			if (node != null) {
				dataTypeElement = (Element) node;
			}
			if (StringUtils.isNotEmpty(childTagName)) {
				dataTypeElement = DomUtils.getChildByTagName(element,
						childTagName);
			}
			if (dataTypeElement != null) {
				DataTypeDefinition dataType = (DataTypeDefinition) dataTypeParser
						.parse(dataTypeElement, context);
				if (dataType == null) {
					throw new XmlParseException(
							"Parse inner DataType element failed.",
							dataTypeElement, context);
				}
				definitionReference = new DirectDefinitionReference<DataTypeDefinition>(
						dataType);
			}
		}
		return definitionReference;
	}

	/**
	 * 尝试获得一个XML节点引用到某个DataProvider，并返回指向该DataProvider配置声明的引用。<br>
	 * 注意，此处所说的DataProvider可能是XML节点内部定义的私有DataProvider。
	 * 
	 * @param propertyName
	 *            可能定义DataProvider引用信息的属性名。
	 * @param childTagName
	 *            可能定义DataProvider引用信息的子节点名。可为null，表忽略。
	 *            注意，如果propertyName和childTagName参数所代表的定义方式中都能够找到关于DataProvider的描述
	 *            ，将以childTagName中的定义为准。
	 * @param element
	 *            XML节点。
	 * @param context
	 *            解析上下。
	 * @return 指向DataProvider配置声明的引用。
	 * @throws Exception
	 */
	public DefinitionReference<DataProviderDefinition> getReferencedDataProvider(
			String propertyName, String childTagName, Element element,
			DataParseContext context) throws Exception {
		DefinitionReference<DataProviderDefinition> definitionReference = null;
		String name = null;
		Node node = XmlParserUtils.getPropertyNode(element, propertyName);
		if (node != null && XmlParserUtils.isSimpleValueProperty(node)) {
			name = XmlParserUtils.getSimpleValue(node);
		}
		if (StringUtils.isNotEmpty(name)) {
			definitionReference = context.getDataProviderReference(name);
		} else {
			Element dataProviderElement = null;
			if (node != null) {
				dataProviderElement = (Element) node;
			}
			if (StringUtils.isNotEmpty(childTagName)) {
				dataProviderElement = DomUtils.getChildByTagName(element,
						childTagName);
			}
			if (dataProviderElement != null) {
				DataProviderDefinition dataProvider = (DataProviderDefinition) dataProviderParser
						.parse(dataProviderElement, context);
				if (dataProvider == null) {
					throw new XmlParseException(
							"Parse inner DataProvider element failed.",
							dataProviderElement, context);
				}
				definitionReference = new DirectDefinitionReference<DataProviderDefinition>(
						dataProvider);
			}
		}
		return definitionReference;
	}

	/**
	 * 尝试获得一个XML节点引用到某个DataResolver，并返回指向该DataResolver配置声明的引用。<br>
	 * 注意，此处所说的DataResolver可能是XML节点内部定义的私有DataResolver。
	 * 
	 * @param propertyName
	 *            可能定义DataResolver引用信息的属性名。
	 * @param childTagName
	 *            可能定义DataResolver引用信息的子节点名。可为null，表忽略。
	 *            注意，如果propertyName和childTagName参数所代表的定义方式中都能够找到关于DataResolver的描述
	 *            ，将以childTagName中的定义为准。
	 * @param element
	 *            XML节点。
	 * @param context
	 *            解析上下。
	 * @return 指向DataResolver配置声明的引用。
	 * @throws Exception
	 */
	public DefinitionReference<DataResolverDefinition> getReferencedDataResolver(
			String propertyName, String childTagName, Element element,
			DataParseContext context) throws Exception {
		DefinitionReference<DataResolverDefinition> definitionReference = null;
		String name = null;
		Node node = XmlParserUtils.getPropertyNode(element, propertyName);
		if (node != null && XmlParserUtils.isSimpleValueProperty(node)) {
			name = XmlParserUtils.getSimpleValue(node);
		}
		if (StringUtils.isNotEmpty(name)) {
			definitionReference = context.getDataResolverReference(name);
		} else {
			Element dataResolverElement = null;
			if (node != null) {
				dataResolverElement = (Element) node;
			}
			if (StringUtils.isNotEmpty(childTagName)) {
				dataResolverElement = DomUtils.getChildByTagName(element,
						childTagName);
			}
			if (dataResolverElement != null) {
				DataResolverDefinition dataResolver = (DataResolverDefinition) dataResolverParser
						.parse(dataResolverElement, context);
				if (dataResolver == null) {
					throw new XmlParseException(
							"Parse inner DataProvider element failed.",
							dataResolverElement, context);
				}
				definitionReference = new DirectDefinitionReference<DataResolverDefinition>(
						dataResolver);
			}
		}
		return definitionReference;
	}

}
