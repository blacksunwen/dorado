package com.bstek.dorado.jdbc.feature.apudf;

import junit.framework.Assert;

import com.bstek.dorado.data.entity.EntityList;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataProvider;
import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.test.TestTable;

public class ApudfTest extends AbstractJdbcTestCase {
	private TestTable t1 = new TestTable("T1")
				.addColumn("ID", "INT", "PRIMARY KEY")
				.addColumn("C1", "VARCHAR(20)")
				.addColumn("C2", "VARCHAR(20)")
				.addColumn("C3", "VARCHAR(20)")
				.addColumn("C4", "VARCHAR(20)");
	
	public ApudfTest() {
		super();
		this.register(t1);
	}
	
	@SuppressWarnings("rawtypes")
	public void test1() throws Exception {
		for(int i=1; i<=10; i++) {
			Record record = new Record();
			record.put("ID", Integer.valueOf(i) + 10000);
			record.put("C1", "a");
			record.put("C2", "b");
			record.put("C3", "c");
			record.put("C4", "d");
			
			t1.insert(record);
		}
		
		JdbcDataProvider provider = this.getProvider("dataProvider1");
		EntityList list =  (EntityList)provider.getResult();
		Assert.assertEquals(10, list.size());
	}
}
