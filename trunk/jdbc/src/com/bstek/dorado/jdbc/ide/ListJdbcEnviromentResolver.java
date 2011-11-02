package com.bstek.dorado.jdbc.ide;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;

public class ListJdbcEnviromentResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		return toContent();
	}

	public String toContent() {
		final JdbcEnviroment[] envs = JdbcUtils.getEnviromentManager().listAll();
		return toXml("Envs", new XML(){

			@Override
			public void build(XMLWriter xmlWriter, Element rootElement)
					throws Exception {
				for (JdbcEnviroment jdbcEnv: envs) {
					Element element = DocumentHelper.createElement("Env");
					element.addAttribute("name", jdbcEnv.getName());
					xmlWriter.write(element);
				}
			}
			
		});
	}
}
