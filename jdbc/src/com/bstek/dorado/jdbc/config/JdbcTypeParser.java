package com.bstek.dorado.jdbc.config;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.PropertyParser;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.type.JdbcType;

public class JdbcTypeParser extends PropertyParser {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		String name = (String)super.doParse(node, context);
		if (StringUtils.isNotEmpty(name)) {
			JdbcParseContext jdbcContext = (JdbcParseContext) context; 
			JdbcEnviroment env = jdbcContext.getJdbcEnviroment();
			JdbcType jdbcType = env.getDialect().getJdbcType(name);
			return jdbcType;
		} else {
			return null;	
		}
	}
}
