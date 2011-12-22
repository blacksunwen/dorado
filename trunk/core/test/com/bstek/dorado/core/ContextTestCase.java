package com.bstek.dorado.core;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Configure;

import junit.framework.TestCase;

public abstract class ContextTestCase extends TestCase {
	private String locations = "";

	public ContextTestCase() {
		addExtensionContextConfigLocation("com/bstek/dorado/core/context.xml");
		addExtensionContextConfigLocation("com/bstek/dorado/core/test-context.xml");
		addExtensionContextConfigLocation("com/bstek/dorado/config/context.xml");
	}

	protected void addExtensionContextConfigLocation(String location) {
		if (StringUtils.isNotEmpty(locations)) {
			locations += ';';
		}
		locations += location;
	}

	protected String getLocations() {
		return locations;
	}

	@Override
	protected void setUp() throws Exception {
		Configure.getStore().set("core.contextConfigLocation", getLocations());
		super.setUp();
		MockContext.init();
	}

	@Override
	protected void tearDown() throws Exception {
		CommonContext.dispose();
		super.tearDown();
	}

}
