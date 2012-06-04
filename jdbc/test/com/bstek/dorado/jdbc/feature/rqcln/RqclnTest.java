package com.bstek.dorado.jdbc.feature.rqcln;

import java.util.HashMap;
import java.util.Map;

import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.sql.VarSql;
import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;

/**
 * 因为使用Velocity命令时，例如#if、#end常常会编写换行，但是这些换行对于最终的SQL来说是多余的，所以自动去掉这些换行
 * @author mark.li@bstek.com
 *
 */
public class RqclnTest extends AbstractJdbcTestCase {

	public void test1() throws Exception {
		String vClause = "where 1=1\n" +
				"#if($id)\n" +
				" and ID=:id\n" +
				"#end";
		Map<Object, Object> parameter = new HashMap<Object, Object>();
		
		VarSql sql = SqlUtils.build(vClause, parameter);
		
		assertEquals("where 1=1 ", sql.getClause());
	}
	
	public void test2() throws Exception {
		String vClause = "where 1=1\n" +
				"#if($id)\n" +
				"and ID=:id\n" +
				"#end";
		Map<Object, Object> parameter = new HashMap<Object, Object>();
		parameter.put("id", "123");
		
		VarSql sql = SqlUtils.build(vClause, parameter);
		
		assertEquals("where 1=1 and ID=:id ", sql.getClause());
	}
}
