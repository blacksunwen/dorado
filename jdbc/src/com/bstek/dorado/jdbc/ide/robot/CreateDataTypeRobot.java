package com.bstek.dorado.jdbc.ide.robot;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.idesupport.robot.Robot;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.config.xml.DomHelper;
import com.bstek.dorado.jdbc.ide.Constants;
import com.bstek.dorado.util.xml.DomUtils;

public class CreateDataTypeRobot implements Robot{

	@Override
	public Node execute(Node node, Properties properties) throws Exception {
		Element element = (Element)node;
		String envName = element.getAttribute(Constants.PARAM_ENV);
		String tableName = element.getAttribute(Constants.PARAM_TBNM);
		Element dataTypeElement = DomUtils.getChildByTagName(element, "DataType");
		
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		ModelGeneratorSuit generator = JdbcUtils.getModelGeneratorSuit();
		if (dataTypeElement == null) {
			Document document = generator.getDataTypeMetaGenerator().createDocument(jdbcEnv, tableName);
			return document.getDocumentElement();
		} else {
			Document oldDocument = DomHelper.newDocument();
			oldDocument.appendChild(DomHelper.adoptElement(dataTypeElement));
			Document document = generator.getDataTypeMetaGenerator().mergeDocument(jdbcEnv, tableName, oldDocument);
			return document.getDocumentElement();
		}
	}

}
