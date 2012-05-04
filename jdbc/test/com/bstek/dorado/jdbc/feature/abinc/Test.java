package com.bstek.dorado.jdbc.feature.abinc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcDataResolverItem;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;

/**
 * Table对象插入功能，支持自增主键
 * @author mark.li@bstek.com
 *
 */
public class Test extends AbstractJdbcTestCase {

	static class T1 {
		static void create(JdbcEnviroment env) {
			String createScript = "CREATE TABLE IF NOT EXISTS T1 (" +
					"ID INT IDENTITY(1, 1), C1 VARCHAR(20)" +
					")";
			env.getSpringNamedDao().getJdbcTemplate().update(createScript);
		}
		
		static void drop(JdbcEnviroment env) {
			String dropScript = "DROP TABLE IF EXISTS T1";
			env.getSpringNamedDao().getJdbcTemplate().update(dropScript);
		}
		
		static Record get(JdbcEnviroment env, Integer id) {
			NamedParameterJdbcTemplate tpl = env.getSpringNamedDao().getNamedParameterJdbcTemplate();
			
			String sql = "SELECT * FROM T1 WHERE ID = :id";
			return tpl.queryForObject(sql, Collections.singletonMap("id", id), new RowMapper<Record>(){

				public Record mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					Record record = new Record();
					record.set("ID", rs.getInt("ID"));
					record.set("C1", rs.getString("C1"));
					return record;
				}
			});
		}
	}
	
	public void test1() throws Exception {
		JdbcEnviroment env = this.getJdbcEnviroment();
		T1.create(env);
		{
			DataItems dataItems = new DataItems();
			Record record = JdbcUtils.getRecordWithState(new Record(), EntityState.NEW) ;
			record.put("C1", "Xx");
			dataItems.put("t1", record);
			
			JdbcDataResolverItem item = new JdbcDataResolverItem();
			item.setName("t1");
			item.setTableName("T1");
			JdbcDataResolver resolver = newResolver(item);
			
			resolver.resolve(dataItems);
			assertEquals(Integer.valueOf(1), record.get("ID"));
			
			Record record2 = T1.get(env, Integer.valueOf(1));
			assertEquals(Integer.valueOf(1), record2.get("ID"));
			assertEquals("Xx", record2.get("C1"));
		}
		{
			DataItems dataItems = new DataItems();
			Record record = JdbcUtils.getRecordWithState(new Record(), EntityState.NEW) ;
			record.put("C1", "Xx");
			dataItems.put("t1", record);
			
			JdbcDataResolverItem item = new JdbcDataResolverItem();
			item.setName("t1");
			item.setTableName("T1");
			JdbcDataResolver resolver = newResolver(item);
			
			resolver.resolve(dataItems);
			assertEquals(Integer.valueOf(2), record.get("ID"));
			
			Record record2 = T1.get(env, Integer.valueOf(2));
			assertEquals(Integer.valueOf(2), record2.get("ID"));
			assertEquals("Xx", record2.get("C1"));
		}
	}
}
