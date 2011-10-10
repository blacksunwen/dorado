package com.bstek.dorado.jdbc.model.table;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.PropertyParser;
import com.bstek.dorado.jdbc.config.xml.JdbcParseContext;
import com.bstek.dorado.jdbc.key.KeyGenerator;
import com.bstek.dorado.jdbc.model.JdbcEnviroment;

public class KeyGeneratorParser extends PropertyParser {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		String name = (String)super.doParse(node, context);
		if (StringUtils.isNotEmpty(name)) {
			JdbcParseContext jdbcContext = (JdbcParseContext) context; 
			JdbcEnviroment env = jdbcContext.getJdbcEnviroment();
			KeyGenerator<Object> kg = env.getDialect().getKeyGenerator(name);
			return kg;
		} else {
			return null;
		}
	}
}
