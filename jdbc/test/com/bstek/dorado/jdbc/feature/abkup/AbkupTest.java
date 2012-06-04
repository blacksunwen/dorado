package com.bstek.dorado.jdbc.feature.abkup;

import com.bstek.dorado.data.entity.EntityEnhancer;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcDataResolverItem;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.test.TestTable;

/**
 * Table对象update功能，支持主键值update
 * @author mark
 *
 */
public class AbkupTest extends AbstractJdbcTestCase {

	private TestTable t1 = new TestTable("T1");
	
	public AbkupTest() {
		super();
		
		this.register(
			t1.addColumn("ID", "INT", "PRIMARY KEY")
			  .addColumn("C1", "VARCHAR(20)")
		);
	}
	
	public void test1() throws Exception {
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
			item.setSupportBatchSql(false);
			
			JdbcDataResolver resolver = newResolver(item);
			resolver.resolve(dataItems);
			
			Record record2 = t1.get("ID",oldId);
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
			item.setSupportBatchSql(false);
			
			JdbcDataResolver resolver = newResolver(item);
			resolver.resolve(dataItems);
			
			Record record2 = t1.get("ID",newId);
			assertEquals(newId, record2.get("ID"));
			assertEquals("Xxx", record2.get("C1"));
		}
	}
}
