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
	
	public static XmlDocumentBuilder getXmlDocumentBuilder(){
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
	
	/**
	 * 将字符串解析成XML文档
	 * 
	 * @param text
	 * @return
	 */
	public static Document parseText(String text) {
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
	
	public static Document newDocument() {
		try {
			return getXmlDocumentBuilder().newDocument();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 为节点新增子节点
	 * 
	 * @param e
	 * @param elementName
	 * @return
	 */
	public static Element addElement(Element e, String elementName) {
		Element element = e.getOwnerDocument().createElement(elementName);
		e.appendChild(element);
		return element;
	}
	
	/**
	 * 为文档新增子节点
	 * 
	 * @param document
	 * @param elementName
	 * @return
	 */
	public static Element addElement(Document document, String elementName) {
		Element element = document.createElement(elementName);
		document.appendChild(element);
		return element;
	}
	
	/**
	 * 将文档转化成字符串
	 * 
	 * @param document
	 * @return
	 */
	public static String toString(Document document) {
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
	
	/**
	 * 将节点转化成字符串
	 * 
	 * @param element
	 * @return
	 */
	public static String toString(Element element) {
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
