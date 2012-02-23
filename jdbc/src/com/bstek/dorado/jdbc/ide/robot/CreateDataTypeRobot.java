package com.bstek.dorado.jdbc.ide.robot;

import java.util.Properties;

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

	@Override
	public Node execute(Node node, Properties properties) throws Exception {
		Element element = (Element)node;
		String tableName = element.getAttribute(Constants.PARAM_TBNM);
		Element dataTypeElement = DomUtils.getChildByTagName(element, "DataType");
		
		ModelGeneratorSuit generator = JdbcUtils.getModelGeneratorSuit();
		if (dataTypeElement == null) {
			Document document = generator.getDataTypeMetaDataGenerator().create(tableName);
			return document.getDocumentElement();
		} else {
			Document oldDocument = DomHelper.newDocument();
			oldDocument.appendChild(DomHelper.adoptElement(dataTypeElement));
			Document document = generator.getDataTypeMetaDataGenerator().merge(tableName, oldDocument);
			return document.getDocumentElement();
		}
	}

}
