package com.bstek.dorado.vidorsupport;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

import junit.framework.Assert;

import org.codehaus.jackson.JsonGenerator.Feature;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.vidorsupport.iapi.IRuleSetFactory;
import com.bstek.dorado.vidorsupport.internal.output.IOutputable;
import com.bstek.dorado.vidorsupport.internal.output.OutputContext;
import com.bstek.dorado.vidorsupport.internal.rule.RuleSet;
import com.bstek.dorado.vidorsupport.internal.vidor.JsonReader;
import com.bstek.dorado.vidorsupport.internal.vidor.XmlNode;
import com.bstek.dorado.vidorsupport.internal.vidor.XmlReader;

public final class CloudoTestHelper {
	
	public static String toString(IOutputable<OutputContext> def) {
		OutputContext context = new OutputContext();
		context.setUsePrettyJson(true);
		context.getJsonGenerator().configure(Feature.ESCAPE_NON_ASCII, false);
		def.output(context);
		return toString(context);
	}
	
	private static String toString(OutputContext context) {
		StringWriter writer = (StringWriter)context.getWriter();
		return writer.toString();
	}
	
	public static Document json2document(String json) throws Exception {
		com.bstek.dorado.vidorsupport.internal.rule.RuleSet ruleSet = CloudoTestHelper.getRuleSet();
		JsonReader reader = new JsonReader();
		Document document = reader.parse(json, ruleSet);
		return document;
	}
	
	public static String json2xml(String json) throws Exception {
		Document document = json2document(json);
		return toString(document);
	}
	
	public static Document document(String name, Class<?> clazz) throws Exception {
		if (!name.endsWith(".xml")) {
			if (!name.endsWith(".view")) {
				name += ".view.xml"; 
			} else {
				name += ".xml"; 
			}
		}
		
		URL url = clazz.getResource(name);
		SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        return document;
	}
	
	public static String toString(Document document) throws Exception {
		OutputFormat format = OutputFormat.createPrettyPrint();
		StringWriter writer = new StringWriter();
		XMLWriter xmlWriter = new XMLWriter(writer, format);
		xmlWriter.write(document);
		
		return writer.toString();
	}
	
	private static String toStringCompactFormat(Document document) throws Exception {
		OutputFormat format = OutputFormat.createCompactFormat();
		StringWriter writer = new StringWriter();
		XMLWriter xmlWriter = new XMLWriter(writer, format);
		xmlWriter.write(document);
		
		return writer.toString();
	}
	
	public static void assertEquals(Document expected, Document actual) throws Exception {
		String expString = toStringCompactFormat(expected);
		String actString = toStringCompactFormat(actual);
		
		Assert.assertEquals(expString, actString);
	}
	
	public static String view2Json(String name, Class<?> clazz) throws Exception {
		if (!name.endsWith(".xml")) {
			if (!name.endsWith(".view")) {
				name += ".view.xml"; 
			} else {
				name += ".xml"; 
			}
		}
		URL url = clazz.getResource(name);
		XmlNode xmlNode = parseXmlNode(url);
		String actual = toString(xmlNode);
		return actual;
	}
	
	public static URL jsonUrl(String name, Class<?> clazz) {
		if (!name.endsWith(".json")) {
			name += ".json";
		}
		URL url = clazz.getResource(name);
		return url;
	}
	
	private static String toString(URL url) throws Exception {
		InputStream input = url.openStream();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[1024];
	        int n = 0;
	        while (-1 != (n = input.read(buffer))) {
	            output.write(buffer, 0, n);
	        }
		} finally {
			input.close();
		}
		
		return new String(output.toByteArray(), "UTF-8");
	}
	
	public static String toString(Resource res) throws Exception {
		if (res.exists()) {
			URL url = res.getURL();
			return toString(url);
		}
		
		return null;
	}
	
	public static String json(String name, Class<?> clazz) throws Exception {
		URL url = jsonUrl(name, clazz);
		if (url == null) {
			url = jsonUrl(name + ".view", clazz);
		}
		return toString(url);
	}
	
	public static com.bstek.dorado.vidorsupport.internal.rule.RuleSet getRuleSet() throws Exception {
		Context context = Context.getCurrent();
		IRuleSetFactory factory = (IRuleSetFactory)context.getServiceBean("&dorado.vidor.rulesetFactory");
		return factory.get();
	}
	
	public static XmlNode parseXmlNode(URL url) throws Exception {
		RuleSet ruleSet = CloudoTestHelper.getRuleSet();
		XmlReader xmlReader = new XmlReader();
		
		XmlReader.ParseParameter parseParameter = new XmlReader.ParseParameter();
		parseParameter.setRuleSet(ruleSet);
		
		XmlNode actual = xmlReader.parse(url, parseParameter);
		return actual;
	}
}
