package com.bstek.dorado.data.config.xml;

import java.util.Map;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.DispatchableXmlParser;
import com.bstek.dorado.config.xml.XmlParser;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-11-22
 */
public class DataObjectParserDispatcher implements XmlParser {
	private DispatchableXmlParser dataTypeParser;
	private DispatchableXmlParser dataProviderParser;
	private DispatchableXmlParser dataResolverParser;

	public void setDataTypeParser(DispatchableXmlParser dataTypeParser) {
		this.dataTypeParser = dataTypeParser;
	}

	public void setDataProviderParser(DispatchableXmlParser dataProviderParser) {
		this.dataProviderParser = dataProviderParser;
	}

	public void setDataResolverParser(DispatchableXmlParser dataResolverParser) {
		this.dataResolverParser = dataResolverParser;
	}

	public Object parse(Node node, ParseContext context) throws Exception {
		DataParseContext dataParseContext = (DataParseContext) context;

		Map<String, NodeWrapper> configuredDataTypes = dataParseContext
				.getConfiguredDataTypes();
		for (NodeWrapper wrapper : configuredDataTypes.values()) {
			dataParseContext.setResource(wrapper.getResource());
			dataTypeParser.parse(wrapper.getNode(), dataParseContext);
		}

		Map<String, NodeWrapper> configuredDataProviders = dataParseContext
				.getConfiguredDataProviders();
		for (NodeWrapper wrapper : configuredDataProviders.values()) {
			dataParseContext.setResource(wrapper.getResource());
			dataProviderParser.parse(wrapper.getNode(), dataParseContext);
		}

		Map<String, NodeWrapper> configuredDataResolvers = dataParseContext
				.getConfiguredDataResolvers();
		for (NodeWrapper wrapper : configuredDataResolvers.values()) {
			dataParseContext.setResource(wrapper.getResource());
			dataResolverParser.parse(wrapper.getNode(), dataParseContext);
		}
		return null;
	}
}
