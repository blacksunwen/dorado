/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.idesupport.robot;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.core.xml.XercesXmlDocumentBuilder;
import com.bstek.dorado.idesupport.IdeSupportContextTestCase;
import com.bstek.dorado.util.xml.DomUtils;

public class EntityDataTypeReflectionRobotTest extends
		IdeSupportContextTestCase {
	public void test() throws Exception {
		Document document = new XercesXmlDocumentBuilder().newDocument();
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
