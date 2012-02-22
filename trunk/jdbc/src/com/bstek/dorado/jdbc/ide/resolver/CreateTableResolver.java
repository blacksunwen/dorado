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
		String namespace = request.getParameter(Constants.PARAM_SPACE);
		String table   = request.getParameter(Constants.PARAM_TBNM);
		String xml = request.getParameter(Constants.PARAM_XML);
		
		Document document = null;
		if (StringUtils.isNotEmpty(xml)) {
			document = DomHelper.parseText(xml);
		}
		
		return toContent(envName, namespace, table, document);
	}

	public String toContent(String envName, String namespace, String table, Document document) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		ModelGeneratorSuit generator = JdbcUtils.getModelGeneratorSuit();
		if (document == null) {
			document = generator.getTableMetaDataGenerator().createDocument(namespace, table, jdbcEnv);
		} else {
			document = generator.getTableMetaDataGenerator().mergeDocument(namespace, table, jdbcEnv, document);
		}
		 
		return DomHelper.toString(document);
	}
	
}
