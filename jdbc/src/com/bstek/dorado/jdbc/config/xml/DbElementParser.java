package com.bstek.dorado.jdbc.config.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;

public class DbElementParser extends TagedObjectParser {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		JdbcParseContext jdbcContext = (JdbcParseContext) context;
		
		if (node instanceof Element) {
			Element element = (Element)node;
			return jdbcContext.parse(element, new ParseCallback() {

				public Object doParse(Node node, ParseContext context) throws Exception {
					return DbElementParser.this.parserDbElement(node, context);
				}
				
			});
		} else {
			return parserDbElement(node, context);
		}
		
	}

	private Object parserDbElement(Node node, ParseContext context) throws Exception {
		return super.doParse(node, context);
	}
}
