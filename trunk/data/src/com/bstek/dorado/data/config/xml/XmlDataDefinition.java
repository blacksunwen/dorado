/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.data.config.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.Definition;
import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;
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
	 * @throws Exception
	 */
	public XmlDataDefinition(Node node,
			DefinitionReference<DataTypeDefinition> dataTypeDefinition,
			XmlParser parser) throws Exception {
		Document newDocument = getXmlDocumentBuilder().newDocument();
		Node clonedNode = newDocument.importNode(node, true);

		this.node = clonedNode;
		this.dataTypeDefinition = dataTypeDefinition;
		this.parser = parser;

		if (clonedNode != null && clonedNode instanceof Element) {
			// http://bsdn.org/projects/dorado7/issue/dorado7-1258
			gothroughElement((Element) clonedNode);
		}
	}

	private static XmlDocumentBuilder getXmlDocumentBuilder() throws Exception {
		return (XmlDocumentBuilder) Context.getCurrent().getServiceBean(
				"xmlDocumentBuilder");
	}

	@Override
	protected Object doCreate(CreationContext context,
			Object[] constructorArgs) throws Exception {
		Object data = null;
		if (node != null) {
			DataCreationContext createContext = (DataCreationContext) context;
			DataParseContext parseContext = new DataParseContext();
			DefinitionReference<DataTypeDefinition> dataTypeDefinition = createContext
					.getCurrentDataTypeDefinition();
			if (dataTypeDefinition == null) {
				dataTypeDefinition = this.dataTypeDefinition;
			}
			parseContext.setCurrentDataType(dataTypeDefinition);
			data = parser.parse(node, parseContext);
			parseContext.restoreCurrentDataType();
		}
		return data;
	}

	private void gothroughElement(Element element) {
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child != null && child instanceof Element) {
				gothroughElement((Element) child);
			} else {
				// do nothing;
			}
		}
	}

}
