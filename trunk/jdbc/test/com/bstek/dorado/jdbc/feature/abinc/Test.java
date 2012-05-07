package com.bstek.dorado.jdbc.feature.abinc;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcDataResolverItem;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.TestTable;

/**
 * Table对象插入功能，支持自增主键
 * @author mark.li@bstek.com
 *
 */
public class Test extends AbstractJdbcTestCase {

	private TestTable t1 = new TestTable("T1");
	
	public Test() {
		super();
		
		t1.addColumn("ID", "INT", "IDENTITY(1, 1)");
		t1.addColumn("C1", "VARCHAR(20)");
		
		this.register(t1);
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
			JdbcDataResolver resolver = newResolver(item);
			
			resolver.resolve(dataItems);
			assertEquals(Integer.valueOf(1), record.get("ID"));
			
			Record record2 = t1.get("ID",Integer.valueOf(1));
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
			
			Record record2 = t1.get("ID",Integer.valueOf(2));
			assertEquals(Integer.valueOf(2), record2.get("ID"));
			assertEquals("Xx", record2.get("C1"));
		}
	}
	
}
