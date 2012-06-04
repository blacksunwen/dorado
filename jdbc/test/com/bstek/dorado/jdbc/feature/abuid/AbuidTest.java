package com.bstek.dorado.jdbc.feature.abuid;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcDataResolverItem;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.test.TestTable;

public class AbuidTest extends AbstractJdbcTestCase {
	private TestTable t1 = new TestTable("T1");
	
	public AbuidTest() {
		super();
		
		this.register(
			t1.addColumn("ID", "VARCHAR(50)", "PRIMARY KEY")
			  .addColumn("C1", "VARCHAR(20)")
		);
	}

	public void test1() throws Exception {
		{
			DataItems dataItems = new DataItems();
			Record record = JdbcUtils.getRecordWithState(new Record(), EntityState.NEW) ;
			record.put("C1", "Xx");
			dataItems.put("t1", record);
			
			JdbcDataResolverItem item = new JdbcDataResolverItem();
			item.setName("t1");
			item.setTableName("T1");
			item.setSupportBatchSql(false);
			JdbcDataResolver resolver = newResolver(item);
			
			resolver.resolve(dataItems);
			String id = record.getString("ID");
			assertNotNull(id);
			
			Record record2 = t1.get("ID", id);
			assertEquals(id, record2.get("ID"));
			assertEquals("Xx", record2.get("C1"));
		}
		
		{
			DataItems dataItems = new DataItems();
			Record record = JdbcUtils.getRecordWithState(new Record(), EntityState.NEW) ;
			record.put("C1", "Xxx");
			dataItems.put("t1", record);
			
			JdbcDataResolverItem item = new JdbcDataResolverItem();
			item.setName("t1");
			item.setTableName("T1");
			item.setSupportBatchSql(false);
			JdbcDataResolver resolver = newResolver(item);
			
			resolver.resolve(dataItems);
			String id = record.getString("ID");
			assertNotNull(id);
			
			Record record2 = t1.get("ID", id);
			assertEquals(id, record2.get("ID"));
			assertEquals("Xxx", record2.get("C1"));
		}
	}
}
