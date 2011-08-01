package com.bstek.dorado.data.config;

import com.bstek.dorado.data.config.DataTypeName;

import junit.framework.TestCase;

public class DataTypeNameTest extends TestCase {

	public void testDataTypeName1() {
		DataTypeName dataTypeName = new DataTypeName("String");
		assertEquals(dataTypeName.getDataType(), "String");
		assertFalse(dataTypeName.hasSubDataType());
	}

	public void testDataTypeName2() {
		DataTypeName dataTypeName = new DataTypeName("List[Bean]");
		assertEquals(dataTypeName.getDataType(), "List");
		assertTrue(dataTypeName.hasSubDataType());
		assertEquals(dataTypeName.getSubDataTypes().length, 1);

		String subDataTypeName = dataTypeName.getSubDataTypes()[0];
		assertEquals("Bean", subDataTypeName);
	}

	public void testDataTypeName3() {
		DataTypeName dataTypeName = new DataTypeName("Map[String,Bean]");
		assertEquals(dataTypeName.getDataType(), "Map");
		assertTrue(dataTypeName.hasSubDataType());
		assertEquals(dataTypeName.getSubDataTypes().length, 2);

		String subDataTypeName1 = dataTypeName.getSubDataTypes()[0];
		String subDataTypeName2 = dataTypeName.getSubDataTypes()[1];

		assertEquals("String", subDataTypeName1);
		assertEquals("Bean", subDataTypeName2);
	}

	public void testDataTypeName4() {
		DataTypeName dataTypeName = new DataTypeName("List[List[Bean]]");
		assertEquals(dataTypeName.getDataType(), "List");
		assertTrue(dataTypeName.hasSubDataType());
		assertEquals(dataTypeName.getSubDataTypes().length, 1);

		String subDataTypeName1 = dataTypeName.getSubDataTypes()[0];
		assertEquals("List[Bean]", subDataTypeName1);
	}

	public void testDataTypeName5() {
		DataTypeName dataTypeName = new DataTypeName("[Bean]");
		assertEquals(dataTypeName.getDataType(), "Collection");
		assertTrue(dataTypeName.hasSubDataType());
		assertEquals(dataTypeName.getSubDataTypes().length, 1);

		String subDataTypeName1 = dataTypeName.getSubDataTypes()[0];
		assertEquals("Bean", subDataTypeName1);
	}

}
