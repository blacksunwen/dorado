package com.bstek.dorado.jdbc.config;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;

public class DomHelper {
	static private XmlDocumentBuilder xmlDocumentBuilder;
	
	static public XmlDocumentBuilder getXmlDocumentBuilder(){
		if (xmlDocumentBuilder == null) {
			Context context = Context.getCurrent();
			try {
				xmlDocumentBuilder = (XmlDocumentBuilder) context.getServiceBean("xmlDocumentBuilder");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return xmlDocumentBuilder;
	}
	
	static public Document parseText(String text) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			return builder.parse(new InputSource(new StringReader(text)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	static public Document newDocument() {
		try {
			return getXmlDocumentBuilder().newDocument();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	static public Element adoptElement(Element element) {
		Document document = element.getOwnerDocument();
		if (document != null) {
			return (Element) document.adoptNode(element);
		} else {
			return element;
		}
	}
	
	static public Element addElement(Element e, String elementName) {
		Element element = e.getOwnerDocument().createElement(elementName);
		e.appendChild(element);
		return element;
	}
	
	static public Element addElement(Document document, String elementName) {
		Element element = document.createElement(elementName);
		document.appendChild(element);
		return element;
	}
	
	static public String toString(Document document) {
		StringWriter writer = new StringWriter();
		TransformerFactory tFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return writer.toString();
	}
	
	
	static public String toString(Element element) {
		StringWriter writer = new StringWriter();
		TransformerFactory tFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(element);
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return writer.toString();
	}
}
