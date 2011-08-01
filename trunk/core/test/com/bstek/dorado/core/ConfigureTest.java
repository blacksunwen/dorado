package com.bstek.dorado.core;

import com.bstek.dorado.core.Configure;

import junit.framework.TestCase;

public class ConfigureTest extends TestCase {

	public void testGetString() {
		String value = Configure.getString("core.resourceLoader");
		assertEquals("com.bstek.dorado.core.io.BaseResourceLoader", value);
	}

	public void testGetDefaultString() {
		String value = Configure.getString("#invalidKey", "defaultValue");
		assertEquals("defaultValue", value);
	}

	public void testGetBoolean() {
		boolean value = Configure.getBoolean("data.config.autoReloadEnabled");
		assertEquals(false, value);
	}

	public void testGetDefaultBoolean() {
		boolean value = Configure.getBoolean("#invalidKey", true);
		assertEquals(true, value);
	}

	public void testGetLong() {
		long value = Configure
				.getLong("data.config.validateThreadIntervalSeconds");
		assertEquals(5, value);
	}

	public void testGetDefaultLong() {
		long value = Configure.getLong("#invalidKey", 100);
		assertEquals(100, value);
	}

}
