package com.bstek.dorado.jdbc.feature.aqlik;

import java.util.HashMap;
import java.util.Map;

import com.bstek.dorado.jdbc.sql.JdbcParameterSource;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.sql.VarExpr;
import com.bstek.dorado.jdbc.sql.VarSql;
import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;

/**
 * 在Velocity中定义的SQL在处理like时，需要使用#set为参数添加%；为了避免#set，而允许用户编写like :%var%的语句
 * 
 * @author mark.li@bstek.com
 *
 */
public class AqlikTest extends AbstractJdbcTestCase {

	public void test1() throws Exception {
		String vClause = "where 1=1#if($name) and name like :%name%#end";
		
		Map<Object, Object> parameter = new HashMap<Object, Object>();
		parameter.put("name", "abc");
		
		VarSql sql = SqlUtils.build(vClause, parameter);
		assertEquals("where 1=1 and name like :_1", sql.getClause());
		assertEquals(1, sql.getVarExprs().size());
		
		VarExpr varExpr = sql.getVarExprs().get(0);
		assertEquals("%name%", varExpr.getExpr());
		assertEquals("_1", varExpr.getVarName());
		assertEquals(null, varExpr.getDataType());
		assertEquals(true, varExpr.isPercentStart());
		assertEquals(true, varExpr.isPercentEnd());
		
		JdbcParameterSource ps = sql.getParameterSource();
		assertEquals("%abc%", ps.getValue("_1"));
	}
	
	public void test2() throws Exception {
		String vClause = "where 1=1#if($name) and name like :name%#end";
		
		Map<Object, Object> parameter = new HashMap<Object, Object>();
		parameter.put("name", "abc");
		
		VarSql sql = SqlUtils.build(vClause, parameter);
		assertEquals("where 1=1 and name like :_1", sql.getClause());
		assertEquals(1, sql.getVarExprs().size());
		
		VarExpr varExpr = sql.getVarExprs().get(0);
		assertEquals("name%", varExpr.getExpr());
		assertEquals("_1", varExpr.getVarName());
		assertEquals(null, varExpr.getDataType());
		assertEquals(false, varExpr.isPercentStart());
		assertEquals(true, varExpr.isPercentEnd());
		
		JdbcParameterSource ps = sql.getParameterSource();
		assertEquals("abc%", ps.getValue("_1"));
	}
	
	public void test3() throws Exception {
		String vClause = "where 1=1#if($name) and name like :%name#end";
		
		Map<Object, Object> parameter = new HashMap<Object, Object>();
		parameter.put("name", "abc");
		
		VarSql sql = SqlUtils.build(vClause, parameter);
		assertEquals("where 1=1 and name like :_1", sql.getClause());
		assertEquals(1, sql.getVarExprs().size());
		
		VarExpr varExpr = sql.getVarExprs().get(0);
		assertEquals("%name", varExpr.getExpr());
		assertEquals("_1", varExpr.getVarName());
		assertEquals(null, varExpr.getDataType());
		assertEquals(true, varExpr.isPercentStart());
		assertEquals(false, varExpr.isPercentEnd());
		
		JdbcParameterSource ps = sql.getParameterSource();
		assertEquals("%abc", ps.getValue("_1"));
	}
}
