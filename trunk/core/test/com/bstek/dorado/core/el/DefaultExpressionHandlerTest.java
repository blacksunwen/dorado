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
package com.bstek.dorado.core.el;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.bstek.dorado.core.ContextTestCase;
import com.bstek.dorado.core.el.CombinedExpression;
import com.bstek.dorado.core.el.ContextVarsInitializer;
import com.bstek.dorado.core.el.DefaultExpressionHandler;
import com.bstek.dorado.core.el.SingleExpression;

public class DefaultExpressionHandlerTest extends ContextTestCase {
	private static class MockVarsInitializer implements ContextVarsInitializer {
		public void initializeContext(Map<String, Object> vars) {
			vars.put("Runtime", Runtime.getRuntime());
		}
	};

	private DefaultExpressionHandler defaultExpressionHandler = new DefaultExpressionHandler();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		List<ContextVarsInitializer> initializers = new ArrayList<ContextVarsInitializer>();
		initializers.add(new MockVarsInitializer());
		defaultExpressionHandler.setContextInitializers(initializers);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCompile() {
		String text = "\\${2*3}";
		assertNull(defaultExpressionHandler.compile(text));

		text = "${2*3";
		assertNull(defaultExpressionHandler.compile(text));

		text = "${2*3}";
		assertEquals(defaultExpressionHandler.compile(text).getClass(),
				SingleExpression.class);

		text = "ABC${2*3}";
		assertEquals(defaultExpressionHandler.compile(text).getClass(),
				CombinedExpression.class);

		text = "${2*3}ABC";
		assertEquals(defaultExpressionHandler.compile(text).getClass(),
				CombinedExpression.class);
	}

	public void testGetJexlContext() throws Exception {
		String text;
		Object value;

		text = "${2*3}";
		value = defaultExpressionHandler.compile(text).evaluate();
		assertEquals(value, 6);

		text = "ABC${2*3}";
		value = defaultExpressionHandler.compile(text).evaluate();
		assertEquals(value, "ABC6");

		text = "${2*3}ABC";
		value = defaultExpressionHandler.compile(text).evaluate();
		assertEquals(value, "6ABC");

		text = "${2*3}ABC${2*3}";
		value = defaultExpressionHandler.compile(text).evaluate();
		assertEquals(value, "6ABC6");

		text = "${2*3}ABC\\${2*3}";
		value = defaultExpressionHandler.compile(text).evaluate();
		assertEquals(value, "6ABC${2*3}");

		text = "\\${2*3}ABC${2*3}";
		value = defaultExpressionHandler.compile(text).evaluate();
		assertEquals(value, "${2*3}ABC6");
	}

	public void testSetContextInitializers() throws Exception {
		String text;
		Object value;

		text = "${Runtime}";
		value = defaultExpressionHandler.compile(text).evaluate();
		assertEquals(value, Runtime.getRuntime());

		text = "${Runtime.availableProcessors()}";
		value = defaultExpressionHandler.compile(text).evaluate();
		assertTrue(value != new Long(0));
	}

}
