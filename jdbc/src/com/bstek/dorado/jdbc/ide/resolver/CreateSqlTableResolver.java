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

public class CreateSqlTableResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(Constants.PARAM_ENV);
		String sql = request.getParameter(Constants.PARAM_SQL);
		String xml = request.getParameter(Constants.PARAM_XML);
		Document document = null;
		if (StringUtils.isNotEmpty(xml)) {
			document = DomHelper.parseText(xml);
		}
		
		return toContent(envName, sql, document);
	}


	public String toContent(String envName, String sql, Document document) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		ModelGeneratorSuit generator = JdbcUtils.getModelGeneratorSuit();
		if (document == null) {
			document = generator.getSqlTableMetaDataGenerator().createDocument(jdbcEnv, sql);
		} else {
			document = generator.getSqlTableMetaDataGenerator().mergeDocument(jdbcEnv, sql, document);
		}
		return DomHelper.toString(document);
	}
}
