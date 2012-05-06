package com.bstek.dorado.jdbc.feature.abkup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.data.entity.EntityEnhancer;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.AbstractTestTable;
import com.bstek.dorado.jdbc.AbstractTestTable.Action;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcDataResolverItem;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;

/**
 * Table对象update功能，支持主键值update
 * @author mark
 *
 */
public class Test extends AbstractJdbcTestCase {

	static class T1 extends AbstractTestTable {

		public T1(JdbcEnviroment jdbcEnviroment) {
			super(jdbcEnviroment);
		}

		@Override
		public String getCreateScript() {
			String script = "CREATE TABLE T1 (" +
					"ID INT PRIMARY KEY, C1 VARCHAR(20)" +
					")";
			return script;
		}

		@Override
		public String getDropScript() {
			return "DROP TABLE T1";
		}

		@Override
		public Record get(Object key) throws Exception {
			NamedParameterJdbcTemplate tpl = this.getJdbcEnviroment().getSpringNamedDao().getNamedParameterJdbcTemplate();
			
			String sql = "SELECT * FROM T1 WHERE ID = :id";
			return tpl.queryForObject(sql, Collections.singletonMap("id", key), new RowMapper<Record>(){

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
		final T1 t1 = new T1(this.getJdbcEnviroment());
		t1.doTest(new Action(){

			public void doAction(JdbcEnviroment jdbcEnviroment)
					throws Exception {
				Integer oldId = Integer.valueOf(10);
				{
					DataItems dataItems = new DataItems();
					Record record = JdbcUtils.getRecordWithState(new Record(), EntityState.NEW);
					record.put("ID", oldId);
					record.put("C1", "Xx");
					dataItems.put("t1", record);
					
					JdbcDataResolverItem item = new JdbcDataResolverItem();
					item.setName("t1");
					item.setTableName("T1");
					
					JdbcDataResolver resolver = newResolver(item);
					resolver.resolve(dataItems);
					
					Record record2 = t1.get(oldId);
					assertEquals(oldId, record2.get("ID"));
					assertEquals("Xx", record2.get("C1"));
				}
				
				Integer newId = Integer.valueOf(20);
				{
					Record record = JdbcUtils.getRecordWithState(new Record(), EntityState.MODIFIED);
					
					EntityEnhancer entity = record.getEntityEnhancer();
					entity.getOldValues(true).put("ID", oldId);
					entity.getOldValues(true).put("C1", "Xx");
					record.put("ID", newId);
					record.put("C1", "Xxx");

					DataItems dataItems = new DataItems();
					dataItems.put("t1", record);
					
					JdbcDataResolverItem item = new JdbcDataResolverItem();
					item.setName("t1");
					item.setTableName("T1");
					
					JdbcDataResolver resolver = newResolver(item);
					resolver.resolve(dataItems);
					
					Record record2 = t1.get(newId);
					assertEquals(newId, record2.get("ID"));
					assertEquals("Xxx", record2.get("C1"));
				}
			}
			
		});
	}
}
