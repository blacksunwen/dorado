package com.bstek.dorado.jdbc.config;

import org.w3c.dom.Element;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.data.config.xml.DataResolverParser;
import com.bstek.dorado.jdbc.JdbcIntercepter;

public class JdbcDataResolverParser extends DataResolverParser {

	private JdbcIntercepter jdbcIntercepter;

	public JdbcIntercepter getJdbcIntercepter() {
		return jdbcIntercepter;
	}

	public void setJdbcIntercepter(JdbcIntercepter jdbcIntercepter) {
		this.jdbcIntercepter = jdbcIntercepter;
	}

	@Override
	protected void initDefinition(ObjectDefinition definition, Element element,
			ParseContext context) throws Exception {
		super.initDefinition(definition, element, context);
		definition.getProperties().put("jdbcIntercepter", jdbcIntercepter);
	}
	
}
