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

public class CreateTableResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(PARAM_ENV);
		String catalog = request.getParameter(PARAM_CATA);
		String schema  = request.getParameter(PARAM_SCHE);
		String table   = request.getParameter(PARAM_TBNM);
		String xml = request.getParameter(PARAM_XML);
		
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		
		Document document = null;
		if (StringUtils.isNotEmpty(xml)) {
			try {
				document = DocumentHelper.parseText(xml);
			} catch (DocumentException e) {
				throw new RuntimeException(e);
			}
		}
		
		return toContent(catalog, schema, table, jdbcEnv, document);
	}

	public String toContent(String catalog, String schema, String table, 
			final JdbcEnviroment jdbcEnv, Document document) {
		final ModelGeneratorSuit generator = jdbcEnv.getModelGeneratorSuit();
		
		if (document == null) {
			document = generator.getTableMetaDataGenerator().createDocument(catalog, schema, table, jdbcEnv);
		} else {
			document = generator.getTableMetaDataGenerator().mergeDocument(catalog, schema, table, jdbcEnv, document);
		}
		 
		return toString(document);
	}
	
}
