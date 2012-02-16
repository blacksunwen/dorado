package com.bstek.dorado.jdbc.config;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.PropertyParser;
import com.bstek.dorado.jdbc.JdbcUtils;

public class JdbcEnviromentParser extends PropertyParser {
	
	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		String name = (String)super.doParse(node, context);
		if (StringUtils.isNotEmpty(name)) {
			if ("default".equals(name)) {
				return JdbcUtils.getEnviromentManager().getDefault();
			} else {
				return JdbcUtils.getEnviromentManager().getEnviroment(name);
			}
		} else {
			return null;
		}
	}
}
