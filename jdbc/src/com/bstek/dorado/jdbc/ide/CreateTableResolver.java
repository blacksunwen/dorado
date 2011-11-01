package com.bstek.dorado.jdbc.ide;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;

import com.bstek.dorado.data.variant.VariantUtils;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGenerator;
import com.bstek.dorado.jdbc.support.TableGeneratorOption;

public class CreateTableResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(PARAM_ENV);
		String catalog = request.getParameter(PARAM_CATA);
		String schema  = request.getParameter(PARAM_SCHE);
		String table   = request.getParameter(PARAM_TBNM);
		String generateCatalog = request.getParameter(PARAM_GENERATE_CATALOG);
		String generateSchema  = request.getParameter(PARAM_GENERATE_SCHEMA);
		
		TableGeneratorOption option = new TableGeneratorOption();
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		option.setJdbcEnviroment(jdbcEnv);
		option.setGenerateCatalog(StringUtils.isNotEmpty(generateCatalog)? VariantUtils.toBoolean(generateCatalog): false);
		option.setGenerateSchema(StringUtils.isNotEmpty(generateSchema)? VariantUtils.toBoolean(generateSchema): false);
		
		return toContent(catalog, schema, table, option);
	}

	public String toContent(String catalog, String schema, String table, TableGeneratorOption option) {
		final JdbcEnviroment jdbcEnv = option.getJdbcEnviroment();
		final ModelGenerator generator = jdbcEnv.getModelGenerator();
		
		Document document = generator.createTableDocument(catalog, schema, table, option);
		return document.asXML();
	}
}
