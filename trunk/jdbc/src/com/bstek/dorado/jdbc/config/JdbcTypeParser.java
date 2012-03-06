package com.bstek.dorado.jdbc.config;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.PropertyParser;
import com.bstek.dorado.jdbc.JdbcTypeManager;
import com.bstek.dorado.jdbc.type.JdbcType;

/**
 * {@link JdbcType}的解析器
 * 
 * @author mark.li@bstek.com
 *
 */
public class JdbcTypeParser extends PropertyParser {

	private JdbcTypeManager jdbcTypeManager;
	
	public JdbcTypeManager getJdbcTypeManager() {
		return jdbcTypeManager;
	}

	public void setJdbcTypeManager(JdbcTypeManager jdbcTypeManager) {
		this.jdbcTypeManager = jdbcTypeManager;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		String name = (String)super.doParse(node, context);
		if (StringUtils.isNotEmpty(name)) {
			JdbcType jdbcType = jdbcTypeManager.get(name);
			return jdbcType;
		} else {
			return null;
		}
	}
}
