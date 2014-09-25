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
package com.bstek.dorado.vidorsupport.bugs.bug_41;

import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.bstek.dorado.vidorsupport.AbstractCloudoTestCase;
import com.bstek.dorado.vidorsupport.CloudoTestHelper;
import com.bstek.dorado.vidorsupport.internal.rule.Rule;
import com.bstek.dorado.vidorsupport.internal.rule.RuleSet;
import com.bstek.dorado.vidorsupport.internal.vidor.XmlNode;

public class Bug41Test extends AbstractCloudoTestCase {
	
	private String resourceName = "HelloWorld.view.xml";
	
	private XmlNode parse(String resourceName) throws Exception {
		URL url = this.getClass().getResource(resourceName);
		XmlNode actual = CloudoTestHelper.parseXmlNode(url);
		return actual;
	}
	
	private String getRuleType(XmlNode node) throws Exception {
		String ruleId = node.getRuleId();
		RuleSet ruleSet = CloudoTestHelper.getRuleSet();
		Rule rule = ruleSet.getRule(ruleId);
		com.bstek.dorado.idesupport.model.Rule originalRule = rule.getOriginalObject();
		return originalRule.getType();
	}
	
	public void test_XmlNode() throws Exception {
		XmlNode rootNode = this.parse(resourceName);
		{
			String ruleType = this.getRuleType(rootNode);
			Assert.assertEquals(ruleType, com.bstek.dorado.view.widget.base.toolbar.ToolBar.class.getName());
		}
		Assert.assertEquals("ToolBar", rootNode.getRuleId());
		List<XmlNode> childNodes = rootNode.getNodes();
		Assert.assertEquals(2, childNodes.size());;
		{
			XmlNode node = childNodes.get(0);
			String ruleType = this.getRuleType(node);
			Assert.assertEquals(ruleType, com.bstek.dorado.view.widget.base.Button.class.getName());
		}
		{
			XmlNode node = childNodes.get(1);
			String ruleType = this.getRuleType(node);
			Assert.assertEquals(ruleType, com.bstek.dorado.view.widget.base.toolbar.Separator.class.getName());
		}
	}
	
	@Test
	public void test_output() throws Exception {
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		System.out.println(actual);
		
//		String expected = CloudoTestHelper.json(resourceName, this.getClass());
//		this.assertJsonEquals(actual, expected);
	}
	
}
