package com.bstek.dorado.jdbc.ide.resolver;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.JdbcTypeManager;
import com.bstek.dorado.jdbc.type.JdbcType;

/**
 * {@link JdbcType}列表
 * 
 * @author mark.li@bstek.com
 *
 */
public class ListJdbcTypeResolver extends AbstractResolver {

	private JdbcTypeManager jdbcTypeManager;
	
	public JdbcTypeManager getJdbcTypeManager() {
		return jdbcTypeManager;
	}

	public void setJdbcTypeManager(JdbcTypeManager jdbcTypeManager) {
		this.jdbcTypeManager = jdbcTypeManager;
	}
	
	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		return toContent();
	}

	public String toContent() {
		JdbcType[] types = jdbcTypeManager.list();
		List<String> names = new ArrayList<String>(types.length);
		for (JdbcType type: types) {
			names.add(type.getName());
		}
		return StringUtils.join(names, ',');
	}
}
