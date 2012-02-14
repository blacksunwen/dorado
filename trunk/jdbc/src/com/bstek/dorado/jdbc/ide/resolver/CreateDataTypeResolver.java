package com.bstek.dorado.jdbc.ide.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.config.xml.DomHelper;
import com.bstek.dorado.jdbc.ide.Constants;

public class CreateDataTypeResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(Constants.PARAM_ENV);
		String tableName = request.getParameter(Constants.PARAM_TBNM);
		String xml = request.getParameter(Constants.PARAM_XML);
		
		Document document = null;
		if (StringUtils.isNotEmpty(xml)) {
			document = DomHelper.parseText(xml);
		}
		
		return toContent(envName, tableName, document);
	}

	public String toContent(String envName, String tableName, Document document) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		ModelGeneratorSuit generator = JdbcUtils.getModelGeneratorSuit();
		if (document == null) {
			document = generator.getDataTypeMetaGenerator().createDocument(jdbcEnv, tableName);
		} else {
			document = generator.getDataTypeMetaGenerator().mergeDocument(jdbcEnv, tableName, document);
		}
		
		return DomHelper.toString(document);
	}
}
