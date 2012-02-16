package com.bstek.dorado.idesupport.robot;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.core.xml.DefaultXmlDocumentBuilder;
import com.bstek.dorado.idesupport.IdeSupportContextTestCase;
import com.bstek.dorado.util.xml.DomUtils;

public class EntityDataTypeReflectionRobotTest extends
		IdeSupportContextTestCase {
	public void test() throws Exception {
		Document document = new DefaultXmlDocumentBuilder().newDocument();
		Element dataTypeElement = document.createElement("DataType");
		dataTypeElement.setAttribute("matchType",
				"com.bstek.dorado.view.widget.base.Button");
		document.appendChild(dataTypeElement);

		EntityDataTypeReflectionRobot robot = new EntityDataTypeReflectionRobot();
		dataTypeElement = (Element) robot.execute(dataTypeElement, null);

		for (Element propertyDefElement : DomUtils.getChildrenByTagName(
				dataTypeElement, "PropertyDef")) {
			System.out.println("EntityDataTypeReflectionRobotTest - "
					+ propertyDefElement.getAttribute("name"));
		}
	}
}
