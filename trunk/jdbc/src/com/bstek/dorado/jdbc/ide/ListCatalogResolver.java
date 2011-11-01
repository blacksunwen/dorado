package com.bstek.dorado.jdbc.ide;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGenerator;

public class ListCatalogResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(PARAM_ENV);
		return toContent(envName);
	}

	public String toContent(String envName) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		ModelGenerator generator = jdbcEnv.getModelGenerator();
		String[] catalogs = generator.listCatalogs(jdbcEnv);;
		return StringUtils.join(catalogs, ',');
	}
}
