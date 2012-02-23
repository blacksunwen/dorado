package com.bstek.dorado.jdbc.oracle.v11;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;

import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.model.storedprogram.StoredProgram;

public class StoredProgramTest extends AbstractOracle11JdbcTestCase {
	
	public void testNoop1() {
		StoredProgram sp = JdbcUtils.getStoredProgram("noop1");
		Object rt = JdbcUtils.call(sp, null);
		
		Assert.assertNull(rt);
	}
	
	public void testNoop2() {
		StoredProgram sp = JdbcUtils.getStoredProgram("noop2");
		Map<String,Object> param = new HashMap<String,Object>(2);
		String a = RandomStringUtils.randomAlphabetic(3);
		String b = RandomStringUtils.randomAlphabetic(4);
		param.put("a", a);
		param.put("b", b);
		Object rt = JdbcUtils.call(sp, param);
		
		Assert.assertNull(rt);
	}
	
	public void testP1() {
		StoredProgram sp = JdbcUtils.getStoredProgram("p1");
		Map<String,Object> param = new HashMap<String,Object>(2);
		String a = RandomStringUtils.randomAlphabetic(3);
		String b = RandomStringUtils.randomAlphabetic(4);
		param.put("a", a);
		param.put("b", b);
		Object rt = JdbcUtils.call(sp, param);
		
		Assert.assertEquals(a + "-" + b, rt);
	}
	
	public void testP2() {
		StoredProgram sp = JdbcUtils.getStoredProgram("p2");
		Map<String,Object> param = new HashMap<String,Object>(2);
		String a = RandomStringUtils.randomAlphabetic(3);
		String b = RandomStringUtils.randomAlphabetic(4);
		param.put("a", a);
		param.put("b", b);
		Object rt = JdbcUtils.call(sp, param);
		
		Assert.assertNotNull(rt);
		@SuppressWarnings("unchecked")
		Map<String,Object> map = (Map<String,Object>)rt;
		
		Assert.assertEquals(a + "-" + b, map.get("C1"));
		Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), map.get("C2"));
	}
	
	public void testFn1() {
		StoredProgram sp = JdbcUtils.getStoredProgram("fn1");
		Map<String,Object> param = new HashMap<String,Object>(2);
		String a = RandomStringUtils.randomAlphabetic(3);
		String b = RandomStringUtils.randomAlphabetic(4);
		param.put("a", a);
		param.put("b", b);
		Object rt = JdbcUtils.call(sp, param);
		
		Assert.assertEquals(a + "-" + b, rt);
	}
	public void testFn2() {
		StoredProgram sp = JdbcUtils.getStoredProgram("fn2");
		Map<String,Object> param = new HashMap<String,Object>(2);
		String a = RandomStringUtils.randomAlphabetic(3);
		String b = RandomStringUtils.randomAlphabetic(4);
		param.put("a", a);
		param.put("b", b);
		Object rt = JdbcUtils.call(sp, param);
		
		Assert.assertNotNull(rt);
		@SuppressWarnings("unchecked")
		Map<String,Object> map = (Map<String,Object>)rt;
		
		Assert.assertEquals(a + "-" + b, map.get("C1"));
		Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), map.get(sp.getReturnValueName()));
	}
	public void testFn3() {
		StoredProgram sp = JdbcUtils.getStoredProgram("fn3");
		Map<String,Object> param = new HashMap<String,Object>(2);
		String a = RandomStringUtils.randomAlphabetic(3);
		String b = RandomStringUtils.randomAlphabetic(4);
		param.put("a", a);
		param.put("b", b);
		Object rt = JdbcUtils.call(sp, param);
		
		Assert.assertNotNull(rt);
		@SuppressWarnings("unchecked")
		Map<String,Object> map = (Map<String,Object>)rt;
		
		Assert.assertEquals(a + "-" + b, map.get("C1"));
		Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), map.get("C2"));
		Assert.assertEquals(map.get("C1") + "," + map.get("C2"), map.get(sp.getReturnValueName()));
	}
}
