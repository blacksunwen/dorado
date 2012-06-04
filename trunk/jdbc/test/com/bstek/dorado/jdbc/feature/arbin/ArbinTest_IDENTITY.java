package com.bstek.dorado.jdbc.feature.arbin;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.test.TestTable;

public class ArbinTest_IDENTITY extends AbstractJdbcTestCase {

	private TestTable t1 = new TestTable("T1")
		.addColumn("ID", "INT", "IDENTITY(1, 1)")
		.addColumn("C1", "VARCHAR(20)");
	
	public ArbinTest_IDENTITY() {
		super();
		this.register(t1);
	}
	
	public void test1() throws Exception {
		JdbcDataResolver resolver = this.getResolver("dataResolver1");
		
		EntityDataType dataType = this.getDataType("dataType1");
		DataItems dataItems = new DataItems();
		{
			Record record = JdbcUtils.getRecordWithState(new Record(), EntityState.NEW) ;
			record.getEntityEnhancer().setDataType(dataType);
			record.put("ID", null);
			record.put("C1", "Xx");
			dataItems.put("t1", record);
		}
		
		resolver.resolve(dataItems);
		Integer id = Integer.valueOf(1);
		Record record2 = t1.get("ID", id);
		
		assertEquals(id, record2.get("ID"));
		assertEquals("Xx", record2.get("C1"));
	}
}
