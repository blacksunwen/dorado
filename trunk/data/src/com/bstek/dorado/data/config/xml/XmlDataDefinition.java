package com.bstek.dorado.data.config.xml;

import org.w3c.dom.Node;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.Definition;
import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.data.config.definition.DataCreationContext;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;

/**
 * XML数据的配置声明对象。<br>
 * 该对象主要用于完成对XML数据的懒解析。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 14, 2008
 * @see com.bstek.dorado.data.config.xml.PreloadDataElementParser
 */
public class XmlDataDefinition extends Definition {
	private Node node;
	private DefinitionReference<DataTypeDefinition> dataTypeDefinition;
	private XmlParser parser;

	/**
	 * @param node
	 *            包含数据信息的XML节点
	 * @param dataTypeDefinition
	 *            目标DataType的配置声明引用，最终生成的数据将被转换为与该DataType相匹配的类型。
	 * @param parser
	 *            真正的XML数据解析器
	 */
	public XmlDataDefinition(Node node,
			DefinitionReference<DataTypeDefinition> dataTypeDefinition,
			XmlParser parser) {
		// 此处保留DOM对象的引用可能导致内存的额外占用。
		this.node = node;
		this.dataTypeDefinition = dataTypeDefinition;
		this.parser = parser;
	}

	@Override
	protected synchronized Object doCreate(CreationContext context)
			throws Exception {
		Object data = null;
		if (node != null) {
			DataCreationContext createContext = (DataCreationContext) context;
			DataParseContext parseContext = new DataParseContext();
			DefinitionReference<DataTypeDefinition> dataTypeDefinition = createContext
					.getDataTypeDefinition();
			if (dataTypeDefinition == null) {
				dataTypeDefinition = this.dataTypeDefinition;
			}
			parseContext.setCurrentDataType(dataTypeDefinition);
			data = parser.parse(node, parseContext);
			parseContext.restoreCurrentDataType();
		}
		return data;
	}

}
