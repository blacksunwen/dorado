package com.bstek.dorado.jdbc.ide;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.bstek.dorado.core.Constants;
import com.bstek.dorado.web.resolver.AbstractTextualResolver;

public abstract class Resolver extends AbstractTextualResolver {

	public static final String PARAM_ENV  = "ENV";  //env
	public static final String PARAM_CATA = "CATA"; //catalog
	public static final String PARAM_SCHE = "SCHE"; //schema
	public static final String PARAM_TBTY = "TBTY"; //tableType
	public static final String PARAM_TBNM = "TBNM"; //tableName
	public static final String PARAM_GENERATE_CATALOG = "generateCatalog";
	public static final String PARAM_GENERATE_SCHEMA  = "generateSchema";
	
	public abstract String getContent(HttpServletRequest request,
			HttpServletResponse response);
	
	protected void output(String result, PrintWriter writer) {
		if (StringUtils.isNotEmpty(result)) {
			try {
				writer.write(result);
			} finally {
				writer.flush();
				writer.close();
			}
		}
	}
	
	@Override
	protected void execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String content = getContent(request, response);
		if (StringUtils.isNotEmpty(content)) {
			PrintWriter writer = getWriter(request, response);
			output(content, writer);
		}
	}

	protected String toXml(String rootElementName, XML xml) {
		StringWriter writer = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(Constants.DEFAULT_CHARSET);

		XMLWriter xmlWriter = new XMLWriter(writer, format);
		try {
			xmlWriter.startDocument();
			
			Element rootElement = DocumentHelper.createElement(rootElementName);
			
			xmlWriter.writeOpen(rootElement);
			xml.build(xmlWriter, rootElement);
			xmlWriter.writeClose(rootElement);
			
			xmlWriter.endDocument();
			xmlWriter.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return writer.toString();
	}
	
	interface XML {
		void build(XMLWriter xmlWriter, Element rootElement) throws Exception;
	}
}
