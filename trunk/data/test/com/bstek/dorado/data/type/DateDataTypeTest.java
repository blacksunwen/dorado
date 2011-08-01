package com.bstek.dorado.data.type;

import java.util.Date;

import org.apache.commons.lang.math.NumberUtils;

import com.bstek.dorado.data.DataContextTestCase;
import com.bstek.dorado.data.type.DateDataType;

public class DateDataTypeTest extends DataContextTestCase {
	private DateDataType dateDataType = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		dateDataType = new DateDataType();
	}

	@Override
	protected void tearDown() throws Exception {
		dateDataType = null;
		super.tearDown();
	}

	public void testConverToText() {
		Object value = new Date();
		String text = dateDataType.toText(value);
		assertTrue(NumberUtils.isNumber(text));
	}

	public void testConvertFromText() {
		String text;
		Date date;

		text = null;
		date = (Date) dateDataType.fromText(text);
		assertNull(date);

		text = "1976-04-03";
		date = (Date) dateDataType.fromText(text);
		assertTrue(date.toString().indexOf("1976") >= 0);

		text = "1949-02-10";
		date = (Date) dateDataType.fromText(text);
		assertTrue(date.toString().indexOf("1949") >= 0);

		text = "13:30:58";
		date = (Date) dateDataType.fromText(text);
		assertTrue(date.toString().indexOf("13") >= 0);

		text = "1976-04-03 13:30:58";
		date = (Date) dateDataType.fromText(text);
		assertTrue(date.toString().indexOf("1976") >= 0);
		assertTrue(date.toString().indexOf("13") >= 0);

		text = String.valueOf(System.currentTimeMillis());
		date = (Date) dateDataType.fromText(text);
		assertNotNull(date);
	}

	public void testConvertFromObject() {
		Object value;
		Date date;

		value = null;
		date = (Date) dateDataType.fromObject(value);
		assertNull(date);

		value = new Long(System.currentTimeMillis());
		date = (Date) dateDataType.fromObject(value);
		assertNotNull(date);
	}

}
