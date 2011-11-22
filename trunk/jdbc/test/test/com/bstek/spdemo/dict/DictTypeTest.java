package test.com.bstek.spdemo.dict;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import junit.framework.Assert;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;

public class DictTypeTest extends ConfigManagerTestSupport {

	public DictTypeTest() {
		this.addExtensionContextConfigLocation("test/com/bstek/jdbc/runtime/test-context.xml");
	}
	
	public void test1() throws Exception {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment("oracle11");
		
		JdbcTemplate jt = jdbcEnv.getNamedDao().getJdbcTemplate();
		
		final int id = Integer.valueOf(RandomStringUtils.randomNumeric(3));
		
		DictType dictType = jt.execute("{? = call DICT.get(?)}", new CallableStatementCallback<DictType>() {

			@Override
			public DictType doInCallableStatement(CallableStatement cs)
					throws SQLException, DataAccessException {
				String typeName = "BSTEK.DICT_TYPE";
				Connection con = cs.getConnection();
				con.getTypeMap().put(typeName,  DictType.class);
				
				cs.setInt(2, id);
				cs.registerOutParameter(1, Types.STRUCT, typeName);
				
				cs.execute();
				
				DictType dictType = (DictType)cs.getObject(1);
				return dictType;
			}
			
		});
		
		Assert.assertEquals(new BigDecimal(id), dictType.getId());
		Assert.assertEquals("ABC", dictType.getName());
		Assert.assertEquals(new BigDecimal(1), dictType.getParentId());
	}
	
	public void test2() throws Exception {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment("oracle11");
		
		JdbcTemplate jt = jdbcEnv.getNamedDao().getJdbcTemplate();
		
		DictTableType table = jt.execute("{? = call DICT.list()}", new CallableStatementCallback<DictTableType>(){

			@Override
			public DictTableType doInCallableStatement(CallableStatement cs)
					throws SQLException, DataAccessException {
				
				cs.registerOutParameter(1, java.sql.Types.ARRAY, "BSTEK.DICT_TABLE_TYPE");
				cs.execute();
				
				java.sql.Array array = (java.sql.Array)cs.getObject(1);
				Object obj = array.getArray();
				if (obj != null) {
					DictType[] types = objectArray(DictType.class, (Object[])obj);
					DictTableType table = new DictTableType(types);
					return table;
				} else {
					return null;
				}
				
//				oracle.jdbc.OracleCallableStatement oraCs = (oracle.jdbc.OracleCallableStatement)cs;
//				DictTableType table = (DictTableType)oraCs.getORAData(1, DictTableType.getORADataFactory());
//				return table;
			}
			
		});
		
		Assert.assertEquals(2, table.length());
	}
	
	<T> T[] objectArray(Class<T> c, Object[] objs) {
		Object array = java.lang.reflect.Array.newInstance(c, objs.length);
		for (int i=0; i<objs.length; i++) {
			Object obj = objs[i];
			T t = c.cast(obj);
			java.lang.reflect.Array.set(array, i, t);
		} 
		
		@SuppressWarnings("unchecked")
		T[] ts = (T[]) array;
		return ts;
	}
}
