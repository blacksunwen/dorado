package com.bstek.dorado.jdbc.ide.robot;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.idesupport.robot.Robot;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.config.DomHelper;
import com.bstek.dorado.jdbc.ide.Constants;
import com.bstek.dorado.util.xml.DomUtils;

/**
 * 用于创建{@link com.bstek.dorado.data.type.DataType}
 * 
 * @author mark.li@bstek.com
 *
 */
public class CreateDataTypeRobot implements Robot{

	private static Log logger = LogFactory.getLog(CreateDataTypeRobot.class);
	
	@Override
	public Node execute(Node node, Properties properties) throws Exception {
		Element tableElement = (Element)node;
		
		if (logger.isInfoEnabled()) {
			logger.info("CreateDataType::---------------------------");
			logger.info(DomHelper.toString(tableElement));
			logger.info(properties);
		}
		
		String tableName = tableElement.getAttribute(Constants.PARAM_TBNM);
		Element dataTypeElement = DomUtils.getChildByTagName(tableElement, "DataType");
		
		ModelGeneratorSuit generator = JdbcUtils.getModelGeneratorSuit();
		
		Document oldDocument = DomHelper.newDocument();
		oldDocument.appendChild(oldDocument.adoptNode(dataTypeElement));
		Document document = generator.getDataTypeMetaDataGenerator().merge(tableName, oldDocument);
		if (logger.isInfoEnabled()) {
			logger.info(DomHelper.toString(document));
		}
		
		return document.getDocumentElement();
	}

}
