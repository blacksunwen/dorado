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

public class CreateTableRobot implements Robot {

	@Override
	public Node execute(Node node, Properties properties) throws Exception {
		Element element = (Element)node;
		
		String envName = element.getAttribute(Constants.PARAM_ENV);
		String catalog = element.getAttribute(Constants.PARAM_CATA);
		String schema  = element.getAttribute(Constants.PARAM_SCHE);
		String table   = element.getAttribute(Constants.PARAM_TBNM);
		Element tableElement = DomUtils.getChildByTagName(element, "Table");
		
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		ModelGeneratorSuit generator =jdbcEnv.getModelGeneratorSuit();
		if (tableElement == null) {
			Document document = generator.getTableMetaDataGenerator().createDocument(catalog, schema, table, jdbcEnv);
			return document.getDocumentElement();
		} else {
			Document oldDocument = DomHelper.newDocument();
			oldDocument.appendChild(DomHelper.adoptElement(tableElement));
			
			Document document = generator.getTableMetaDataGenerator().mergeDocument(catalog, schema, table, jdbcEnv, oldDocument);
			return document.getDocumentElement();
		}
	}

}
