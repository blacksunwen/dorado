package com.bstek.dorado.jdbc.feature.ardfi;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.test.TestTable;

/**
 * JdbcDataResolver支持delete语句优先执行
 * @author mark.li@bstek.com
 *
 */
public class ArdfiTest extends AbstractJdbcTestCase {
	private TestTable t1 = new TestTable("T1")
			.addColumn("ID", "INT", "PRIMARY KEY")
			.addColumn("C1", "VARCHAR(20)")
			.addColumn("C2", "VARCHAR(20)")
			.addColumn("C3", "VARCHAR(20)")
			.addColumn("C4", "VARCHAR(20)");
	
	public ArdfiTest(){
		super();
		this.register(t1);
	}
	
	public void test1() throws Exception {
		for(int i=1; i<=10; i++) {
			Record r = new Record();
			
			r.set("ID", Integer.valueOf(i));
			r.set("C1", "a");
			r.set("C2", "b");
			r.set("C3", "c");
			r.set("C4", "d");
			
			t1.insert(r);
		}
		
		JdbcDataResolver resolver = this.getResolver("dataResolver1");
		DataItems dataItems = new DataItems();
		List<Record> records = new ArrayList<Record>();
		dataItems.put("t1", records);
		
		EntityDataType dataType = this.getDataType("T1");
		
		Record rnew = JdbcUtils.getRecordWithState(new Record(), EntityState.NEW);
		rnew.getEntityEnhancer().setDataType(dataType);
		rnew.set("ID", Integer.valueOf(100));
		rnew.set("C1", "a");
		rnew.set("C2", "b");
		rnew.set("C3", "c");
		rnew.set("C4", "d");
		records.add(rnew);

		Record rmod = JdbcUtils.getRecordWithState(new Record(), EntityState.MODIFIED);
		rmod.getEntityEnhancer().setDataType(dataType);
		rmod.set("ID", Integer.valueOf(1));
		rmod.set("C1", "aa");
		rmod.set("C2", "bb");
		rmod.set("C3", "cc");
		rmod.set("C4", "dd");
		records.add(rmod);
		
		Record rdel = JdbcUtils.getRecordWithState(new Record(), EntityState.DELETED);
		rdel.getEntityEnhancer().setDataType(dataType);
		rdel.set("ID", Integer.valueOf(2));
		rdel.set("C1", "aa");
		rdel.set("C2", "bb");
		rdel.set("C3", "cc");
		rdel.set("C4", "dd");
		records.add(rdel);
		
		resolver.resolve(dataItems);
		
		Assert.assertEquals(1, rdel.get("index"));
		Assert.assertEquals(2, rnew.get("index"));
		Assert.assertEquals(3, rmod.get("index"));
	}
}
