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
package com.bstek.dorado.vidorsupport.vidor.auxiliary.miport;

import org.dom4j.Document;
import org.junit.Test;

import com.bstek.dorado.vidorsupport.AbstractCloudoTestCase;
import com.bstek.dorado.vidorsupport.CloudoTestHelper;

public class ImportTest extends AbstractCloudoTestCase {
	private String resourceName = "01";
	
	@Test
	public void test_view2json() throws Exception {
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());

		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_json2view() throws Exception {
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		CloudoTestHelper.assertEquals(expected, actual);
	}
}
