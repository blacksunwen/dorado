package com.bstek.dorado.jdbc.ide.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.ide.Constants;

public class ListCatalogResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(Constants.PARAM_ENV);
		return toContent(envName);
	}

	public String toContent(String envName) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		ModelGeneratorSuit generator = jdbcEnv.getModelGeneratorSuit();
		final String[] catalogs = generator.getJdbcEnviromentMetaDataGenerator().listCatalogs(jdbcEnv);
		return StringUtils.join(catalogs, ',');
	}
}
