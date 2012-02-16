package com.bstek.dorado.jdbc.ide.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.config.DomHelper;
import com.bstek.dorado.jdbc.ide.Constants;

public class CreateTableResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(Constants.PARAM_ENV);
		String catalog = request.getParameter(Constants.PARAM_CATA);
		String schema  = request.getParameter(Constants.PARAM_SCHE);
		String table   = request.getParameter(Constants.PARAM_TBNM);
		String xml = request.getParameter(Constants.PARAM_XML);
		
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		
		Document document = null;
		if (StringUtils.isNotEmpty(xml)) {
			document = DomHelper.parseText(xml);
		}
		
		return toContent(catalog, schema, table, jdbcEnv, document);
	}

	public String toContent(String catalog, String schema, String table, 
			final JdbcEnviroment jdbcEnv, Document document) {
		final ModelGeneratorSuit generator = JdbcUtils.getModelGeneratorSuit();
		
		if (document == null) {
			document = generator.getTableMetaDataGenerator().createDocument(catalog, schema, table, jdbcEnv);
		} else {
			document = generator.getTableMetaDataGenerator().mergeDocument(catalog, schema, table, jdbcEnv, document);
		}
		 
		return DomHelper.toString(document);
	}
	
}
