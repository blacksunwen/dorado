package com.bstek.dorado.jdbc.feature.abupd;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.test.TestTable;

/**
 * Table对象update功能
 * @author mark.li@bstek.com
 *
 */
public class AbupdTest extends AbstractJdbcTestCase {
	private TestTable t1 = new TestTable("T1")
				.addColumn("ID", "INT", "PRIMARY KEY")
				.addColumn("C1", "VARCHAR(20)")
				.addColumn("C2", "VARCHAR(20)")
				.addColumn("C3", "VARCHAR(20)")
				.addColumn("C4", "VARCHAR(20)");

	public AbupdTest() {
		super();
		this.register(t1);
	}
	
	public void test1() throws Exception {
		for (int i=1; i<=10; i++) {
			Record r = new Record();
			r.set("ID", Integer.valueOf(i));
			r.set("C1", "c1_" + i);
			r.set("C2", "c2_" + i);
			r.set("C3", "c3_" + i);
			r.set("C4", "c4_" + i);
			
			t1.insert(r);
		}
		
		DataItems dataItems = new DataItems();
		List<Record> records = new ArrayList<Record>();
		for (int i=1; i<=10; i++) {
			Record r = JdbcUtils.getRecordWithState(new Record(), EntityState.MODIFIED);
			r.set("ID", Integer.valueOf(i));
			r.set("C1", "c1_u" + i);
			r.set("C2", "c2_u" + i);
			r.set("C3", "c3_u" + i);
			r.set("C4", "c4_u" + i);
			records.add(r);
		}
		dataItems.put("t1", records);
		
		JdbcDataResolver resolver = this.getResolver("dataResolver1");
		resolver.resolve(dataItems);
		
		for (int i=1; i<=10; i++) {
			Record r = t1.get("ID", Integer.valueOf(i));
			Assert.assertEquals(Integer.valueOf(i), r.get("ID"));
			Assert.assertEquals("c1_u" + i, r.get("C1"));
			Assert.assertEquals("c2_u" + i, r.get("C2"));
			Assert.assertEquals("c3_u" + i, r.get("C3"));
			Assert.assertEquals("c4_u" + i, r.get("C4"));
		}
	}
	
}
