package com.bstek.dorado.vidorsupport.vidor;

import org.junit.Test;

import com.bstek.dorado.vidorsupport.AbstractCloudoTestCase;
import com.bstek.dorado.vidorsupport.CloudoTestHelper;

public class OutputTest extends AbstractCloudoTestCase {
	
	@Test
	public void test_01() throws Exception {
		String resourceName = "01.Empty";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());

		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_02() throws Exception {
		String resourceName = "02.Property1";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_03() throws Exception {
		String resourceName = "03.Property2";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_04() throws Exception {
		String resourceName = "04.Property3";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_05() throws Exception {
		String resourceName = "05.Property4";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
	
		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_06() throws Exception {
		String resourceName = "06.Property5";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
		
		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_07() throws Exception {
		String resourceName = "07.Mapping";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_08() throws Exception {
		String resourceName = "08.Wrapper";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_09() throws Exception {
		String resourceName = "09.Event";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_10() throws Exception {
		String resourceName = "10.DockLayout";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_11() throws Exception {
		String resourceName = "11.AnchorLayout";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_12() throws Exception {
		String resourceName = "12.HBoxLayout";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_13() throws Exception {
		String resourceName = "13.VBoxLayout";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_14() throws Exception {
		String resourceName = "14.FormLayout";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_15() throws Exception {
		String resourceName = "15.NativeLayout";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_16() throws Exception {
		String resourceName = "16.Layout";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_17() throws Exception {
		String resourceName = "17.Parameter";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_18() throws Exception {
		String resourceName = "18.Validator";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_19() throws Exception {
		String resourceName = "19.Meta";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
		this.assertJsonEquals(actual, expected);
	}
	
	@Test
	public void test_20() throws Exception {
		String resourceName = "20_AnchorLayout";
		
		String actual = CloudoTestHelper.view2Json(resourceName, this.getClass());
		String expected = CloudoTestHelper.json(resourceName, this.getClass());
		
		System.out.println("-- actual --");
		System.out.println(actual);
		System.out.println("-- expected --");
		System.out.println(expected);
		
		this.assertJsonEquals(actual, expected);
	}
}
