package com.bstek.dorado.jdbc.ide;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGenerator;

public class CreateSqlTableResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(PARAM_ENV);
		String sql = request.getParameter(PARAM_SQL);
		
		return toContent(envName, sql);
	}


	public String toContent(String envName, String sql) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		ModelGenerator generator =jdbcEnv.getModelGenerator();
		Document document = generator.createSqlTableDocument(jdbcEnv, sql);
		
		return toString(document);
	}
}
