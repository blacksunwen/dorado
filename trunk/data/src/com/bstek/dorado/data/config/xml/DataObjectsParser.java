package com.bstek.dorado.data.config.xml;

import java.util.Map;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.XmlParser;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-27
 */
public class DataObjectsParser {
	private XmlParser globalDataTypeParser;
	private XmlParser globalDataProviderParser;
	private XmlParser globalDataResolverParser;

	/**
	 * 设置全局DataType的解析器。
	 */
	public void setGlobalDataTypeParser(XmlParser globalDataTypeParser) {
		this.globalDataTypeParser = globalDataTypeParser;
	}

	/**
	 * 设置全局DataProvider的解析器。
	 */
	public void setGlobalDataProviderParser(XmlParser globalDataProviderParser) {
		this.globalDataProviderParser = globalDataProviderParser;
	}

	/**
	 * 设置全局DataResolver的解析器。
	 */
	public void setGlobalDataResolverParser(XmlParser globalDataResolverParser) {
		this.globalDataResolverParser = globalDataResolverParser;
	}

	public Object parse(ParseContext context) throws Exception {
		DataParseContext parseContext = (DataParseContext) context;

		Map<String, NodeWrapper> configuredDataTypes = parseContext
				.getConfiguredDataTypes();
		for (NodeWrapper wrapper : configuredDataTypes.values()) {
			parseContext.setResource(wrapper.getResource());
			globalDataTypeParser.parse(wrapper.getNode(), parseContext);
		}

		Map<String, NodeWrapper> configuredDataProviders = parseContext
				.getConfiguredDataProviders();
		for (NodeWrapper wrapper : configuredDataProviders.values()) {
			parseContext.setResource(wrapper.getResource());
			globalDataProviderParser.parse(wrapper.getNode(), parseContext);
		}

		Map<String, NodeWrapper> configuredDataResolvers = parseContext
				.getConfiguredDataResolvers();
		for (NodeWrapper wrapper : configuredDataResolvers.values()) {
			parseContext.setResource(wrapper.getResource());
			globalDataResolverParser.parse(wrapper.getNode(), parseContext);
		}
		return null;
	}

}
