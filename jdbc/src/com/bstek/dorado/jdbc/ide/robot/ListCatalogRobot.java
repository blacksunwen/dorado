package com.bstek.dorado.jdbc.ide.robot;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.idesupport.robot.Robot;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.config.xml.DomHelper;
import com.bstek.dorado.jdbc.ide.Constants;

public class ListCatalogRobot implements Robot {

	@Override
	public Node execute(Node node, Properties properties) throws Exception {
		Element element = (Element)node;
		String envName = element.getAttribute(Constants.PARAM_ENV);
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		ModelGeneratorSuit generator = jdbcEnv.getModelGeneratorSuit();
		String[] catalogs = generator.getJdbcEnviromentMetaDataGenerator().listCatalogs(jdbcEnv);
		String text = StringUtils.join(catalogs, ',');
		
		Element textElement = DomHelper.newDocument().createElement("Text");
		textElement.setTextContent(text);
		return element;
	}

}
