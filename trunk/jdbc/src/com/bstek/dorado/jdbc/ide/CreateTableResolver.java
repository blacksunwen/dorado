package com.bstek.dorado.jdbc.ide;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;

public class CreateTableResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(PARAM_ENV);
		String catalog = request.getParameter(PARAM_CATA);
		String schema  = request.getParameter(PARAM_SCHE);
		String table   = request.getParameter(PARAM_TBNM);
		
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		
		return toContent(catalog, schema, table, jdbcEnv);
	}

	public String toContent(String catalog, String schema, String table, final JdbcEnviroment jdbcEnv) {
		final ModelGeneratorSuit generator = jdbcEnv.getModelGeneratorSuit();
		
		Document document = generator.getTableMetaDataGenerator().createDocument(catalog, schema, table, jdbcEnv);
		return toString(document);
	}
	
}
