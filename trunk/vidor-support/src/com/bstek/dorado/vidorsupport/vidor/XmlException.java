package com.bstek.dorado.vidorsupport.vidor;

import org.dom4j.Element;

public class XmlException extends IllegalArgumentException {

	private static final long serialVersionUID = -2527121760392056053L;

	private static String messageTemplate1 = "xml fragment not correct [XML]";
	private static String messageTemplate2 = "xml fragment not correct [XML], with reason [REASON]";
	
	public XmlException(Element element) {
		super(messageTemplate1.replace("XML", element.asXML()));
	}
	
	public XmlException(Element element, String message) {
		super(messageTemplate2.replace("XML", element.asXML()).replace("REASON", message));
	}
	
	public XmlException(String message) {
		super(message);
	}
}
