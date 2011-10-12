package com.bstek.dorado.jdbc.config.xml;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;

public class DbElementParser extends TagedObjectParser {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		JdbcParseContext jdbcContext = (JdbcParseContext) context;
		
		return super.doParse(node, jdbcContext);
	}
}
