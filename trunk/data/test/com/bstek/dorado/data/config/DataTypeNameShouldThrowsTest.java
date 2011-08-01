package com.bstek.dorado.data.config;

import junit.framework.TestCase;

public class DataTypeNameShouldThrowsTest extends TestCase {

	public void testDataTypeName1() {
		try {
			new DataTypeName("String[");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}

	public void testDataTypeName2() {
		try {
			new DataTypeName("String[]");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}

	public void testDataTypeName3() {
		try {
			new DataTypeName(" String");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}

	public void testDataTypeName4() {
		try {
			new DataTypeName("[]");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}

	public void testDataTypeName6() {
		try {
			new DataTypeName("List[String] ");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}

	public void testDataTypeName7() {
		try {
			new DataTypeName("List[String,]");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}

	public void testDataTypeName8() {
		try {
			new DataTypeName("List[String,,Bean]");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}

	public void testDataTypeName9() {
		try {
			new DataTypeName("Map[String,List[String]");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}

	public void testDataTypeName10() {
		try {
			new DataTypeName("Map[String,List[String]]]");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}
}
