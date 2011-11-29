package com.bstek.dorado.jdbc.oracle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.model.storedprogram.ProgramParameter;
import com.bstek.dorado.jdbc.model.storedprogram.ProgramParameter.Type;
import com.bstek.dorado.jdbc.model.storedprogram.StoredProgram;
import com.bstek.dorado.jdbc.type.JdbcType;

public class StoredProgramTest extends OracleJdbcTestCase {

	static class SP {
		static StoredProgram noop1() {
			StoredProgram sp = new StoredProgram();
			sp.setCatalog("DORADO");
			sp.setProgramName("NOOP1");
			sp.setName("noop1");
			return sp;
		}
		
		static StoredProgram noop2() {
			StoredProgram sp = new StoredProgram();
			sp.setCatalog("DORADO");
			sp.setProgramName("NOOP2");
			sp.setName("noop2");
			
			JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(JDBC_ENV_NAME);
			JdbcType vsType = jdbcEnv.getDialect().getJdbcType("VARCHAR-String");
			{
				ProgramParameter pp = new ProgramParameter();
				pp.setName("A");
				pp.setType(Type.IN);
				pp.setJdbcType(vsType);
				pp.setValue(":a");
				sp.addParameter(pp);
			}{
				ProgramParameter pp = new ProgramParameter();
				pp.setName("B");
				pp.setType(Type.IN);
				pp.setJdbcType(vsType);
				pp.setValue(":b");
				sp.addParameter(pp);
			}
			
			return sp;
		}
		static StoredProgram p1() {
			StoredProgram sp = new StoredProgram();
			sp.setCatalog("DORADO");
			sp.setProgramName("P1");
			sp.setName("p1");
			
			JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(JDBC_ENV_NAME);
			JdbcType vsType = jdbcEnv.getDialect().getJdbcType("VARCHAR-String");
			{
				ProgramParameter pp = new ProgramParameter();
				pp.setName("A");
				pp.setType(Type.IN);
				pp.setJdbcType(vsType);
				pp.setValue(":a");
				sp.addParameter(pp);
			}{
				ProgramParameter pp = new ProgramParameter();
				pp.setName("B");
				pp.setType(Type.IN);
				pp.setJdbcType(vsType);
				pp.setValue(":b");
				sp.addParameter(pp);
			}{
				ProgramParameter pp = new ProgramParameter();
				pp.setName("C1");
				pp.setType(Type.OUT);
				pp.setJdbcType(vsType);
				sp.addParameter(pp);
			}
			
			return sp;
		}
		static StoredProgram p2() {
			StoredProgram sp = new StoredProgram();
			sp.setCatalog("DORADO");
			sp.setProgramName("P2");
			sp.setName("p2");
			
			JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(JDBC_ENV_NAME);
			JdbcType vsType = jdbcEnv.getDialect().getJdbcType("VARCHAR-String");
			{
				ProgramParameter pp = new ProgramParameter();
				pp.setName("A");
				pp.setType(Type.IN);
				pp.setJdbcType(vsType);
				pp.setValue(":a");
				sp.addParameter(pp);
			}{
				ProgramParameter pp = new ProgramParameter();
				pp.setName("B");
				pp.setType(Type.IN);
				pp.setJdbcType(vsType);
				pp.setValue(":b");
				sp.addParameter(pp);
			}{
				ProgramParameter pp = new ProgramParameter();
				pp.setName("C1");
				pp.setType(Type.OUT);
				pp.setJdbcType(vsType);
				sp.addParameter(pp);
			}{
				ProgramParameter pp = new ProgramParameter();
				pp.setName("C2");
				pp.setType(Type.OUT);
				pp.setJdbcType(vsType);
				sp.addParameter(pp);
			}
			
			return sp;
		}
		static StoredProgram fn1() {
			JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(JDBC_ENV_NAME);
			JdbcType vsType = jdbcEnv.getDialect().getJdbcType("VARCHAR-String");
			
			StoredProgram sp = new StoredProgram();
			sp.setCatalog("DORADO");
			sp.setProgramName("FN1");
			sp.setName("fn1");
			sp.setReturnValueType(vsType);
			
			{
				ProgramParameter pp = new ProgramParameter();
				pp.setName("A");
				pp.setType(Type.IN);
				pp.setJdbcType(vsType);
				pp.setValue(":a");
				sp.addParameter(pp);
			}{
				ProgramParameter pp = new ProgramParameter();
				pp.setName("B");
				pp.setType(Type.IN);
				pp.setJdbcType(vsType);
				pp.setValue(":b");
				sp.addParameter(pp);
			}
			return sp;
		}
		static StoredProgram fn2() {
			JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(JDBC_ENV_NAME);
			JdbcType vsType = jdbcEnv.getDialect().getJdbcType("VARCHAR-String");
			
			StoredProgram sp = new StoredProgram();
			sp.setCatalog("DORADO");
			sp.setProgramName("FN2");
			sp.setName("fn2");
			sp.setReturnValueType(vsType);
			
			{
				ProgramParameter pp = new ProgramParameter();
				pp.setName("A");
				pp.setType(Type.IN);
				pp.setJdbcType(vsType);
				pp.setValue(":a");
				sp.addParameter(pp);
			}{
				ProgramParameter pp = new ProgramParameter();
				pp.setName("B");
				pp.setType(Type.IN);
				pp.setJdbcType(vsType);
				pp.setValue(":b");
				sp.addParameter(pp);
			}{
				ProgramParameter pp = new ProgramParameter();
				pp.setName("C1");
				pp.setType(Type.OUT);
				pp.setJdbcType(vsType);
				sp.addParameter(pp);
			}
			return sp;
		}
		static StoredProgram fn3() {
			JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(JDBC_ENV_NAME);
			JdbcType vsType = jdbcEnv.getDialect().getJdbcType("VARCHAR-String");
			
			StoredProgram sp = new StoredProgram();
			sp.setCatalog("DORADO");
			sp.setProgramName("FN3");
			sp.setName("fn3");
			sp.setReturnValueType(vsType);
			
			{
				ProgramParameter pp = new ProgramParameter();
				pp.setName("A");
				pp.setType(Type.IN);
				pp.setJdbcType(vsType);
				pp.setValue(":a");
				sp.addParameter(pp);
			}{
				ProgramParameter pp = new ProgramParameter();
				pp.setName("B");
				pp.setType(Type.IN);
				pp.setJdbcType(vsType);
				pp.setValue(":b");
				sp.addParameter(pp);
			}{
				ProgramParameter pp = new ProgramParameter();
				pp.setName("C1");
				pp.setType(Type.OUT);
				pp.setJdbcType(vsType);
				sp.addParameter(pp);
			}{
				ProgramParameter pp = new ProgramParameter();
				pp.setName("C2");
				pp.setType(Type.OUT);
				pp.setJdbcType(vsType);
				sp.addParameter(pp);
			}
			return sp;
		}
	}
	
	public void testNoop1() {
		StoredProgram sp = SP.noop1();
		Object rt = JdbcUtils.call(sp, null);
		
		Assert.assertNull(rt);
	}
	
	public void testNoop2() {
		StoredProgram sp = SP.noop2();
		Map<String,Object> param = new HashMap<String,Object>(2);
		String a = RandomStringUtils.randomAlphabetic(3);
		String b = RandomStringUtils.randomAlphabetic(4);
		param.put("a", a);
		param.put("b", b);
		Object rt = JdbcUtils.call(sp, param);
		
		Assert.assertNull(rt);
	}
	
	public void testP1() {
		StoredProgram sp = SP.p1();
		Map<String,Object> param = new HashMap<String,Object>(2);
		String a = RandomStringUtils.randomAlphabetic(3);
		String b = RandomStringUtils.randomAlphabetic(4);
		param.put("a", a);
		param.put("b", b);
		Object rt = JdbcUtils.call(sp, param);
		
		Assert.assertEquals(a + "-" + b, rt);
	}
	
	public void testP2() {
		StoredProgram sp = SP.p2();
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
		StoredProgram sp = SP.fn1();
		Map<String,Object> param = new HashMap<String,Object>(2);
		String a = RandomStringUtils.randomAlphabetic(3);
		String b = RandomStringUtils.randomAlphabetic(4);
		param.put("a", a);
		param.put("b", b);
		Object rt = JdbcUtils.call(sp, param);
		
		Assert.assertEquals(a + "-" + b, rt);
	}
	public void testFn2() {
		StoredProgram sp = SP.fn2();
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
		StoredProgram sp = SP.fn3();
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
