package com.bstek.dorado.data.config.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.XmlParseException;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.data.provider.manager.DataProviderTypeRegisterInfo;
import com.bstek.dorado.data.provider.manager.DataProviderTypeRegistry;

/**
 * DataProvider解析任务的分派器。
 * 该分派器会根据某DataProvider节点的类型将具体的解析任务分派给相应的DataProvider解析器进行解析。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 13, 2008
 */
public abstract class DataProviderParserDispatcher extends GenericParser {
	private DataProviderTypeRegistry dataProviderTypeRegistry;

	/**
	 * 设置DataProvider的类型注册管理器。
	 */
	public void setDataProviderTypeRegistry(
			DataProviderTypeRegistry dataProviderTypeRegistry) {
		this.dataProviderTypeRegistry = dataProviderTypeRegistry;
	}

	protected boolean shouldProcessImport() {
		return false;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		XmlParser parser = null;

		Element element = (Element) node;
		String type = element
				.getAttribute(DataXmlConstants.ATTRIBUTE_PROVIDER_TYPE);
		DataProviderTypeRegisterInfo registryInfo = dataProviderTypeRegistry
				.getTypeRegistryInfo(type);
		if (registryInfo != null) {
			parser = (XmlParser) registryInfo.getParser();
		} else {
			throw new XmlParseException("Unrecognized DataProvider type["
					+ type + "].", element, context);
		}

		if (parser == null) {
			throw new XmlParseException(
					"Can not get Parser for DataProvider of [" + type
							+ "] type.", element, context);
		}

		return parser.parse(node, context);
	}

}
