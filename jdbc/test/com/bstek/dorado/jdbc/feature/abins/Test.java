package com.bstek.dorado.jdbc.feature.abins;

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
 * Table对象支持查询插入功能
 * @author mark.li@bstek.com
 *
 */
public class Test extends AbstractJdbcTestCase {

	static class T1 {
		static void create(JdbcEnviroment env) {
			String createScript = "CREATE TABLE IF NOT EXISTS T1 (" +
					"ID INT PRIMARY KEY, C1 VARCHAR(20)" +
					")";
			env.getSpringNamedDao().getJdbcTemplate().update(createScript);
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
		
		DataItems dataItems = new DataItems();
		Record record = JdbcUtils.getRecordWithState(new Record(), EntityState.NEW) ;
		record.put("ID", Integer.valueOf(10));
		record.put("C1", "Xx");
		dataItems.put("t1", record);
		
		JdbcDataResolverItem item = new JdbcDataResolverItem();
		item.setName("t1");
		item.setTableName("T1");
		JdbcDataResolver resolver = newResolver(item);
		
		resolver.resolve(dataItems);
		
		Record record2 = T1.get(env, Integer.valueOf(10));
		
		assertEquals(Integer.valueOf(10), record2.get("ID"));
		assertEquals("Xx", record2.get("C1"));
	}
	
}
