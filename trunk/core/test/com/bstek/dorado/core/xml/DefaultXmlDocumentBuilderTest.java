/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.core.xml;

import org.w3c.dom.Document;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.ContextTestCase;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.xml.DefaultXmlDocumentBuilder;

public class DefaultXmlDocumentBuilderTest extends ContextTestCase {
	private static final String MOCK_XML = "com/bstek/dorado/core/xml/Test.xml";
	private DefaultXmlDocumentBuilder xmlDocumentBuilder = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		xmlDocumentBuilder = new DefaultXmlDocumentBuilder();
	}

	@Override
	protected void tearDown() throws Exception {
		xmlDocumentBuilder = null;
		super.tearDown();
	}

	public void testLoadDocument() throws Exception {
		Resource resource = Context.getCurrent().getResource(MOCK_XML);
		Document document = xmlDocumentBuilder.loadDocument(resource);
		assertNotNull(document);
	}

}
