package com.bstek.dorado.vidorsupport.vidor;

import org.dom4j.Document;
import org.junit.Test;

import com.bstek.dorado.vidorsupport.AbstractCloudoTestCase;
import com.bstek.dorado.vidorsupport.CloudoTestHelper;

/**
 * 将json格式的view读取成xml格式
 * 
 * @author TD
 *
 */
public class JsonReaderTest extends AbstractCloudoTestCase {

	@Test
	public void test_01() throws Exception {
		String resourceName = "01.Empty.view";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
	@Test
	public void test_02() throws Exception {
		String resourceName = "02.Property1.view";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
	@Test
	public void test_03() throws Exception {
		String resourceName = "03.Property2.view";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
	@Test
	public void test_04() throws Exception {
		String resourceName = "04.Property3.view";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
	@Test
	public void test_05() throws Exception {
		String resourceName = "05.Property4.view";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
	@Test
	public void test_06() throws Exception {
		String resourceName = "06.Property5.view";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
	@Test
	public void test_07() throws Exception {
		String resourceName = "07.Mapping.view";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		
		System.out.println("---------- Actual ----------");
		System.out.println(CloudoTestHelper.toString(actual));
		
		System.out.println("---------- Expected ----------");
		System.out.println(CloudoTestHelper.toString(expected));
		
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
	@Test
	public void test_08() throws Exception {
		String resourceName = "08.Wrapper.view";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
	@Test
	public void test_09() throws Exception {
		String resourceName = "09.Event.view";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
	@Test
	public void test_10() throws Exception {
		String resourceName = "10.DockLayout.view";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
	@Test
	public void test_11() throws Exception {
		String resourceName = "11.AnchorLayout.view";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
	@Test
	public void test_12() throws Exception {
		String resourceName = "12.HBoxLayout.view";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
	@Test
	public void test_13() throws Exception {
		String resourceName = "13.VBoxLayout.view";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
	@Test
	public void test_14() throws Exception {
		String resourceName = "14.FormLayout.view";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
	@Test
	public void test_15() throws Exception {
		String resourceName = "15.NativeLayout.view";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
	@Test
	public void test_16() throws Exception {
		String resourceName = "16.Layout.view";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
	@Test
	public void test_17() throws Exception {
		String resourceName = "17.Parameter.view";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
	@Test
	public void test_18() throws Exception {
		String resourceName = "18.Validator.view";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
	@Test
	public void test_19() throws Exception {
		String resourceName = "19.Meta";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		Document actual = CloudoTestHelper.json2document(json);
		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
		CloudoTestHelper.assertEquals(expected, actual);
	}
	
//	@Test
//	public void test_20() throws Exception {
//		String resourceName = "20_AnchorLayout";
//		
//		String json = CloudoTestHelper.json(resourceName, this.getClass());
//		Document actual = CloudoTestHelper.json2document(json);
//		Document expected = CloudoTestHelper.document(resourceName, this.getClass());
//		CloudoTestHelper.assertEquals(expected, actual);
//	}
}
