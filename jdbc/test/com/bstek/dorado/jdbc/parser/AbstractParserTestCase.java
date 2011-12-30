package com.bstek.dorado.jdbc.parser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.config.xml.XmlParserHelper;
import com.bstek.dorado.config.xml.XmlParserHelper.XmlParserInfo;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.xml.DefaultXmlDocumentBuilder;
import com.bstek.dorado.jdbc.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.config.DbModel;
import com.bstek.dorado.jdbc.config.xml.JdbcParseContext;

public class AbstractParserTestCase extends AbstractJdbcTestCase {

	ObjectDefinition parse(String xmlPath, Class<?> clazz) throws Exception {
		Context context = Context.getCurrent();
		Resource resource = context.getResource(xmlPath);
		XmlParserHelper xmlParserHelper = (XmlParserHelper) context
				.getServiceBean("xmlParserHelper");

		Document document = new DefaultXmlDocumentBuilder().loadDocument(resource);
		XmlParserInfo xmlParserInfo = xmlParserHelper
				.getXmlParserInfos(clazz).get(0);
		XmlParser parser = xmlParserInfo.getParser();

		ParseContext parseContext = new ParseContext();
		Element documentElement = document.getDocumentElement();
		ObjectDefinition definition = (ObjectDefinition) parser.parse(
				documentElement, parseContext);
		return definition;
	}
	
	DbModel parse(String xmlPath) throws Exception {
		Context context = Context.getCurrent();
		Resource resource = context.getResource(xmlPath);
		XmlParserHelper xmlParserHelper = (XmlParserHelper) context
				.getServiceBean("xmlParserHelper");

		Document document = new DefaultXmlDocumentBuilder().loadDocument(resource);
		XmlParserInfo xmlParserInfo = xmlParserHelper
				.getXmlParserInfos(DbModel.class).get(0);
		XmlParser parser = xmlParserInfo.getParser();

		JdbcParseContext parseContext = new JdbcParseContext();
		parseContext.setResource(resource);
		
		Element documentElement = document.getDocumentElement();
		DbModel definition = (DbModel) parser.parse(
				documentElement, parseContext);
		return definition;
	}

	@Override
	protected List<String> getExtConfigLocations() {
		List<String> locations = new ArrayList<String>();
		locations.add("com/bstek/dorado/jdbc/parser/context.xml");
		return locations;
	}
}
