package com.bstek.dorado.jdbc.ide;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;

public class ListTableTypeResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(PARAM_ENV);
		
		return toContent(envName);
	}
	
	public String toContent(String envName) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		final String[] types = jdbcEnv.getModelGenerator().listTableTypes(jdbcEnv);
		
		return toXml("Types", new XML(){

			@Override
			public void build(XMLWriter xmlWriter, Element rootElement)
					throws Exception {
				for (String type: types) {
					Element element = DocumentHelper.createElement("Type");
					element.addAttribute("name", type);
					xmlWriter.write(element);
				}
			}
		});
	}
}
