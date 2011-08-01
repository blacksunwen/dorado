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
