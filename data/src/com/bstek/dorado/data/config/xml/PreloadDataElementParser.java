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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;

/**
 * 数据节点的预解析器。<br>
 * 由于数据节点可能被包含在DataType的配置信息之中，而数据节点的解析本身又有可能会使用到DataType。
 * 因此为了避免在解析顺序上出现混淆，所有需要使用到其他DataType的数据节点都将以懒解析的方式进行处理。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 16, 2008
 * @see com.bstek.dorado.data.config.xml.XmlDataDefinition
 */
public class PreloadDataElementParser extends GenericParser {
	private XmlParser dataParser;

	/**
	 * 设置具体的数据节点解析器。
	 */
	public void setDataParser(XmlParser dataParser) {
		this.dataParser = dataParser;
	}

	protected boolean shouldProcessImport() {
		return false;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		DataParseContext dataContext = (DataParseContext) context;
		DefinitionReference<DataTypeDefinition> dataTypeRef = null;
		if (node instanceof Element) {
			Element element = (Element) node;
			dataTypeRef = dataObjectParseHelper.getReferencedDataType(
					DataXmlConstants.ATTRIBUTE_DATA_TYPE, null, element,
					dataContext);
		}

		if (dataTypeRef == null) {
			dataTypeRef = dataContext.getCurrentDataType();
		}
		dataContext.setCurrentDataType(dataTypeRef);

		Object value;
		// if (dataTypeRef == null || dataContext.isDuringParsingDataElement())
		// 此判断逻辑在Definition被缓存时可能导致内部EL表达式失效
		if (dataContext.isDuringParsingDataElement()) {
			value = dataParser.parse(node, dataContext);
		} else {
			XmlDataDefinition dataDefinition = new XmlDataDefinition(node,
					dataTypeRef, dataParser);
			value = dataDefinition;
		}

		dataContext.restoreCurrentDataType();
		return value;
	}
}
