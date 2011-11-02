package com.bstek.dorado.jdbc.ide;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGenerator;

public class ListCatalogResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(PARAM_ENV);
		return toContent(envName);
	}

	public String toContent(String envName) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		ModelGenerator generator = jdbcEnv.getModelGenerator();
		final String[] catalogs = generator.listCatalogs(jdbcEnv);
		return toXml("Catalogs", new XML(){

			@Override
			public void build(XMLWriter xmlWriter, Element rootElement)
					throws Exception {
				for (String catalog: catalogs) {
					Element element = DocumentHelper.createElement("Catalog");
					element.addAttribute("name", catalog);
					xmlWriter.write(element);
				}
			}
			
		});
	}
}
