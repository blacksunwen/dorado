package com.bstek.dorado.jdbc.ide;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.type.JdbcType;

public class ListJdbcTypeResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(PARAM_ENV);
		
		return toContent(envName);
	}

	public String toContent(String envName) {
		final JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		
		final List<JdbcType> types = jdbcEnv.getDialect().getJdbcTypes();
		
		return toXml("JdbcTypes",  new XML() {

			@Override
			public void build(XMLWriter xmlWriter, Element rootElement)
					throws Exception {
				for (JdbcType jdbcType: types) {
					Element element = DocumentHelper.createElement("JdbcType");
					element.addAttribute("name", jdbcType.getName());
					xmlWriter.write(element);
				}
			}
			
		}); 
	}
}
