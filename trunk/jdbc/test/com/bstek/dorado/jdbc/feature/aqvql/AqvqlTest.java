package com.bstek.dorado.jdbc.feature.aqvql;

import java.util.HashMap;
import java.util.Map;

import com.bstek.dorado.data.util.DataUtils;
import com.bstek.dorado.jdbc.sql.JdbcParameterSource;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.sql.VarExpr;
import com.bstek.dorado.jdbc.sql.VarSql;
import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;

/**
 * 
 * 编写Velocity语法的SQL，根据参数计算实际运行时的SQL
 * 
 * @author mark.li@bstek.com
 *
 */
public class AqvqlTest extends AbstractJdbcTestCase {

	public void test01() throws Exception {
		String vClause = "where 1=1";
		Map<Object, Object> parameter = null;
		
		VarSql sql = SqlUtils.build(vClause, parameter);
		
		assertEquals(vClause, sql.getClause());
		assertEquals(0, sql.getVarExprs().size());
	}
	
	public void test02() throws Exception {
		String vClause = "where 1=1#if($id) and ID=:id#end";
		Map<Object, Object> parameter = new HashMap<Object, Object>();
		
		VarSql sql = SqlUtils.build(vClause, parameter);
		
		assertEquals("where 1=1", sql.getClause());
	}
	
	public void test03() throws Exception {
		String vClause = "where 1=1#if($id) and ID=:id#end";
		Map<Object, Object> parameter = new HashMap<Object, Object>();
		parameter.put("id", "123");
		
		VarSql sql = SqlUtils.build(vClause, parameter);
		
		assertEquals("where 1=1 and ID=:id", sql.getClause());
		assertEquals(1, sql.getVarExprs().size());
		
		VarExpr varExpr = sql.getVarExprs().get(0);
		assertEquals("id", varExpr.getVarName());
	}
	
	public void test04() throws Exception {
		String vClause = "where 1=1#if($id) and ID=:id#end";
		Map<Object, Object> parameter = new HashMap<Object, Object>();
		parameter.put("id", null);
		
		VarSql sql = SqlUtils.build(vClause, parameter);
		
		assertEquals("where 1=1", sql.getClause());
		assertEquals(0, sql.getVarExprs().size());
	} 
	
	public void test05() throws Exception {
		String vClause = "where 1=1#if($id) and ID=:id#end";
		Map<Object, Object> parameter = new HashMap<Object, Object>();
		parameter.put("id", "");
		
		VarSql sql = SqlUtils.build(vClause, parameter);
		
		assertEquals("where 1=1 and ID=:id", sql.getClause());
		assertEquals(1, sql.getVarExprs().size());
		VarExpr varExpr = sql.getVarExprs().get(0);
		assertEquals("id", varExpr.getVarName());
	} 
	
	public void test06() throws Exception {
		String vClause = "where 1=1#if($id && $id.length>0) and ID=:id#end";
		Map<Object, Object> parameter = new HashMap<Object, Object>();
		parameter.put("id", "");
		
		VarSql sql = SqlUtils.build(vClause, parameter);
		
		assertEquals("where 1=1", sql.getClause());
		assertEquals(0, sql.getVarExprs().size());
	} 
	
	public void test07() throws Exception {
		String vClause = "where 1=1#if($id) and ID=:(Integer)id#end";
		Map<Object, Object> parameter = new HashMap<Object, Object>();
		parameter.put("id", "123");
		
		VarSql sql = SqlUtils.build(vClause, parameter);
		
		assertEquals("where 1=1 and ID=:id", sql.getClause());
		assertEquals(1, sql.getVarExprs().size());
		
		VarExpr varExpr = sql.getVarExprs().get(0);
		assertEquals("(Integer)id", varExpr.getExpr());
		assertEquals("id", varExpr.getVarName());
		assertEquals(DataUtils.getDataType("Integer"), varExpr.getDataType());
		assertEquals(false, varExpr.isPercentStart());
		assertEquals(false, varExpr.isPercentEnd());
		
		JdbcParameterSource ps = sql.getParameterSource();
		assertEquals(Integer.valueOf(123), ps.getValue("id"));
	} 
}
