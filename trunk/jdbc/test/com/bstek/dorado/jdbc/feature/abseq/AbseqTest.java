package com.bstek.dorado.jdbc.feature.abseq;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcDataResolverItem;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.test.TestSequence;
import com.bstek.dorado.jdbc.test.TestTable;

public class AbseqTest extends AbstractJdbcTestCase {
	private TestTable t1 = new TestTable("T1");
	private TestSequence seq1 = new TestSequence("SEQ_1");
	
	public AbseqTest() {
		super();
		
		this.register(
			t1.addColumn("ID", "INT", "PRIMARY KEY")
			  .addColumn("C1", "VARCHAR(20)")
		);
		
		this.register(
			seq1.setStart(10)
			    .setIncrement(2)
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
			assertEquals(Integer.valueOf(10), record.get("ID"));
			
			Record record2 = t1.get("ID", Integer.valueOf(10));
			assertEquals(Integer.valueOf(10), record2.get("ID"));
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
			item.setSupportBatchSql(false);
			JdbcDataResolver resolver = newResolver(item);
			
			resolver.resolve(dataItems);
			assertEquals(Integer.valueOf(12), record.get("ID"));
			
			Record record2 = t1.get("ID", Integer.valueOf(12));
			assertEquals(Integer.valueOf(12), record2.get("ID"));
			assertEquals("Xx", record2.get("C1"));
		}
	}
}
