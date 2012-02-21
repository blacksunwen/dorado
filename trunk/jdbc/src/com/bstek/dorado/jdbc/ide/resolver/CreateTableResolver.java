package com.bstek.dorado.jdbc.ide.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcSpace;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.config.DomHelper;
import com.bstek.dorado.jdbc.ide.Constants;

public class CreateTableResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(Constants.PARAM_ENV);
		String spaceName = request.getParameter(Constants.PARAM_SPACE);
		String table   = request.getParameter(Constants.PARAM_TBNM);
		String xml = request.getParameter(Constants.PARAM_XML);
		
		Document document = null;
		if (StringUtils.isNotEmpty(xml)) {
			document = DomHelper.parseText(xml);
		}
		
		return toContent(envName, spaceName, table, document);
	}

	public String toContent(String envName, String spaceName, String table, Document document) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		Dialect dialect = jdbcEnv.getDialect();
		String catalog = null;
		String schema = null;
		if (dialect.getTableJdbcSpace() == JdbcSpace.CATALOG) {
			catalog = spaceName;
		} else if (dialect.getTableJdbcSpace() == JdbcSpace.SCHEMA) {
			schema = spaceName;
		}
		
		ModelGeneratorSuit generator = JdbcUtils.getModelGeneratorSuit();
		if (document == null) {
			document = generator.getTableMetaDataGenerator().createDocument(catalog, schema, table, jdbcEnv);
		} else {
			document = generator.getTableMetaDataGenerator().mergeDocument(catalog, schema, table, jdbcEnv, document);
		}
		 
		return DomHelper.toString(document);
	}
	
}
