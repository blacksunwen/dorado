package com.bstek.dorado.jdbc.ide.resolver;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ide.Constants;
import com.bstek.dorado.jdbc.type.JdbcType;

public class ListJdbcTypeResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(Constants.PARAM_ENV);
		
		return toContent(envName);
	}

	public String toContent(String envName) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		
		List<JdbcType> types = jdbcEnv.getDialect().getJdbcTypes();
		List<String> names = new ArrayList<String>(types.size());
		for (JdbcType type: types) {
			names.add(type.getName());
		}
		return StringUtils.join(names, ',');
	}
}
