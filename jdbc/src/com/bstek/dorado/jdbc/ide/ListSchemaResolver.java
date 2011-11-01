package com.bstek.dorado.jdbc.ide;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.bstek.dorado.jdbc.JdbcConstants;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGenerator;

public class ListSchemaResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String catalog = request.getParameter(PARAM_CATA);
		String envName = request.getParameter(PARAM_ENV);
		
		return toContent(catalog, envName);
	}

	List<Map<String,String>> schemaList(String catalog, String envName) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		ModelGenerator generator = jdbcEnv.getModelGenerator();
		
		List<Map<String,String>> schemaList = generator.listSchemas(jdbcEnv, catalog);
		
		return schemaList;
	}
	
	public String toContent(String catalog, String envName) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		final List<Map<String,String>> schemaList = jdbcEnv.getModelGenerator().listSchemas(jdbcEnv, catalog);
		
		return toXml("Schemas", new XML() {

			@Override
			public void build(XMLWriter xmlWriter, Element rootElement) throws Exception {
				for (Map<String,String> schema: schemaList) {
					Element schemaElement = DocumentHelper.createElement("Schema");
					schemaElement.addAttribute("catalog", schema.get(JdbcConstants.TABLE_CATALOG));
					schemaElement.addAttribute("schema", schema.get(JdbcConstants.TABLE_SCHEM));
					xmlWriter.write(schemaElement);
				}
			}
			
		});
	}
	
}
