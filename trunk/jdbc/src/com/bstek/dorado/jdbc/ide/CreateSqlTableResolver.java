package com.bstek.dorado.jdbc.ide;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;

public class CreateSqlTableResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(PARAM_ENV);
		String sql = request.getParameter(PARAM_SQL);
		String xml = request.getParameter(PARAM_XML);
		Document document = null;
		if (StringUtils.isNotEmpty(xml)) {
			try {
				document = DocumentHelper.parseText(xml);
			} catch (DocumentException e) {
				throw new RuntimeException(e);
			}
		}
		
		return toContent(envName, sql, document);
	}


	public String toContent(String envName, String sql, Document document) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		ModelGeneratorSuit generator =jdbcEnv.getModelGeneratorSuit();
		if (document == null) {
			document = generator.getSqlTableMetaDataGenerator().createDocument(jdbcEnv, sql);
		} else {
			document = generator.getSqlTableMetaDataGenerator().mergeDocument(jdbcEnv, sql, document);
		}
		return toString(document);
	}
}
