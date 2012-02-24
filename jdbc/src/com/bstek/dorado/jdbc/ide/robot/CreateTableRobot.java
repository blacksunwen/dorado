package com.bstek.dorado.jdbc.ide.robot;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.idesupport.robot.Robot;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.config.DomHelper;
import com.bstek.dorado.jdbc.ide.Constants;

/**
 * 用于创建{@link com.bstek.dorado.jdbc.model.table.Table}
 * 
 * @author mark.li@bstek.com
 *
 */
public class CreateTableRobot implements Robot {

	private static Log logger = LogFactory.getLog(CreateTableRobot.class);
	
	@Override
	public Node execute(Node node, Properties properties) throws Exception {
		Element tableElement = (Element)node;
		
		if (logger.isInfoEnabled()) {
			logger.info("CreateTable::---------------------------");
			logger.info(DomHelper.toString(tableElement));
			logger.info(properties);
		}
		
		String envName = tableElement.getAttribute(Constants.PARAM_ENV);
		String namespace = tableElement.getAttribute(Constants.PARAM_SPACE);
		String table   = tableElement.getAttribute(Constants.PARAM_TBNM);
		
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		ModelGeneratorSuit generator = JdbcUtils.getModelGeneratorSuit();
		
		Document documentOld = DomHelper.newDocument();
		Element tableElementOld = (Element)tableElement.cloneNode(true);
		tableElementOld = (Element)documentOld.adoptNode(tableElementOld);
		documentOld.appendChild(tableElementOld);
		
		Document document = generator.getTableMetaDataGenerator().mergeDocument(namespace, table, jdbcEnv, documentOld);
		if (logger.isInfoEnabled()) {
			logger.info(DomHelper.toString(document));
		}
		
		return document.getDocumentElement();
	}

}
