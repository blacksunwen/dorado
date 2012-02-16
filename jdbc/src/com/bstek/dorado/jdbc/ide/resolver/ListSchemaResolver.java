package com.bstek.dorado.jdbc.ide.resolver;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.config.DomHelper;
import com.bstek.dorado.jdbc.ide.Constants;
import com.bstek.dorado.jdbc.support.JdbcConstants;

public class ListSchemaResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String catalog = request.getParameter(Constants.PARAM_CATA);
		String envName = request.getParameter(Constants.PARAM_ENV);
		
		return toContent(envName, catalog);
	}

	List<Map<String,String>> schemaList(String catalog, String envName) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		ModelGeneratorSuit generator = JdbcUtils.getModelGeneratorSuit();
		
		List<Map<String,String>> schemaList = generator.getJdbcEnviromentMetaDataGenerator().listSchemas(jdbcEnv, catalog);
		
		return schemaList;
	}
	
	public String toContent(String envName, String catalog) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		List<Map<String,String>> schemaList = JdbcUtils.getModelGeneratorSuit().getJdbcEnviromentMetaDataGenerator().listSchemas(jdbcEnv, catalog);
		
		Document document = DomHelper.newDocument();
		Element schemas = DomHelper.addElement(document, "Schemas");
		for (Map<String,String> schemaObj: schemaList) {
			Element schema = DomHelper.addElement(schemas, "Schema");
			schema.setAttribute("catalog", schemaObj.get(JdbcConstants.TABLE_CATALOG));
			schema.setAttribute("schema", schemaObj.get(JdbcConstants.TABLE_SCHEM));
		}
		
		return DomHelper.toString(document);
	}
	
}
