package com.bstek.dorado.jdbc.ide.robot;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.idesupport.robot.Robot;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.config.DomHelper;
import com.bstek.dorado.jdbc.ide.Constants;
import com.bstek.dorado.util.xml.DomUtils;

public class CreateSqlTableRobot implements Robot {

	@Override
	public Node execute(Node node, Properties properties) throws Exception {
		Element element = (Element)node;
		String envName = element.getAttribute(Constants.PARAM_ENV);
		String sql = element.getAttribute(Constants.PARAM_SQL);
		Element sqlTableElement = DomUtils.getChildByTagName(element, "SqlTable");
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		ModelGeneratorSuit generator = JdbcUtils.getModelGeneratorSuit();
		
		if (sqlTableElement == null) {
			Document document = generator.getSqlTableMetaDataGenerator().createDocument(jdbcEnv, sql);
			return document.getDocumentElement();
		} else {
			Document oldDocument = DomHelper.newDocument();
			oldDocument.appendChild(DomHelper.adoptElement(sqlTableElement));
			
			Document document = generator.getSqlTableMetaDataGenerator().mergeDocument(jdbcEnv, sql, oldDocument);
			return document.getDocumentElement();
		}
	}

}
