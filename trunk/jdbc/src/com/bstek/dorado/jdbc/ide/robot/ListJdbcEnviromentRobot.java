package com.bstek.dorado.jdbc.ide.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.idesupport.robot.Robot;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.config.xml.DomHelper;

public class ListJdbcEnviromentRobot implements Robot {

	@Override
	public Node execute(Node node, Properties properties) throws Exception {
		JdbcEnviroment[] envs = JdbcUtils.getEnviromentManager().listAll();
		List<String> names = new ArrayList<String>(envs.length);
		for (JdbcEnviroment env: envs) {
			names.add(env.getName());
		}
		String text = StringUtils.join(names, ',');
		
		Element textElement = DomHelper.newDocument().createElement("Text");
		textElement.setTextContent(text);
		return textElement;
	}

}
