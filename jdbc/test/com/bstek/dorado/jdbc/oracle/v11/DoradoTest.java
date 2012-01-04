package com.bstek.dorado.jdbc.oracle.v11;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.bstek.dorado.data.entity.EntityList;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataProvider;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcDataResolverItem;

public class DoradoTest extends Oracle11JdbcTestCase {

	@SuppressWarnings("unchecked")
	public void test01() throws Exception {
		String providerName = "ora11.provider.01";
		JdbcDataProvider provider = this.getProvider(providerName);
		
		EntityList<Record> records = (EntityList<Record>)provider.getResult();
		Assert.assertTrue(records.size() > 0);
		
		String resolverName = "ora11.resolver.01";
		JdbcDataResolver resolver = this.getResolver(resolverName);
		{
			List<JdbcDataResolverItem> items = resolver.getItems();
			Assert.assertEquals(1, items.size());
		}
		
		DataItems dataItems = new DataItems();
		{
			for (Record record: records) {
				EntityUtils.setState(record, EntityState.MODIFIED);
			}
		}
		dataItems.put("employee", records);
		
		resolver.resolve(dataItems);
	}
	
	public void testResolver01() throws Exception {
		String resolverName = "ora11.resolver.01";
		List<Record> records = new ArrayList<Record>();
		JdbcDataResolver resolver = this.getResolver(resolverName);
		DataItems dataItems = new DataItems();
		dataItems.put("employee", records);
		
		//insert
		for (int i=0; i<3; i++) {
			Record e = Employee.random();
			e = EntityUtils.toEntity(e);
			EntityUtils.setState(e, EntityState.NEW);
			records.add(e);
		}
		resolver.resolve(dataItems);
		
		//update
		for (Record e: records) {
			EntityUtils.setState(e, EntityState.MODIFIED);
		}
		resolver.resolve(dataItems);
		
		//delete
		for (Record e: records) {
			EntityUtils.setState(e, EntityState.DELETED);
		}
		resolver.resolve(dataItems);
	}
	
	public void testResolver02() throws Exception {
		String resolverName = "ora11.resolver.02";
		JdbcDataResolver resolver = this.getResolver(resolverName);
		
		Record dept = EntityUtils.toEntity(Dept.random());
		Record employee = EntityUtils.toEntity(Employee.random());
		dept.put("employee", employee);
		
		DataItems dataItems = new DataItems();
		dataItems.put("dept", dept);
		{
			EntityUtils.setState(dept, EntityState.NEW);
			EntityUtils.setState(employee, EntityState.NEW);
			resolver.resolve(dataItems);
		}
		{
			EntityUtils.setState(dept, EntityState.NONE);
			EntityUtils.setState(employee, EntityState.MODIFIED);
			resolver.resolve(dataItems);
		} 
		{
			EntityUtils.setState(dept, EntityState.DELETED);
			resolver.resolve(dataItems);
		}
	}
	
	public void testResolver03() throws Exception {
		String resolverName = "ora11.resolver.03";
		JdbcDataResolver resolver = this.getResolver(resolverName);
		
		Record dept = EntityUtils.toEntity(Dept.random());
		Record employee = EntityUtils.toEntity(Employee.random());
		dept.put("employee", employee);
		
		DataItems dataItems = new DataItems();
		dataItems.put("dept", dept);
		{
			EntityUtils.setState(dept, EntityState.NEW);
			EntityUtils.setState(employee, EntityState.NEW);
			resolver.resolve(dataItems);
		}
		{
			EntityUtils.setState(dept, EntityState.NONE);
			EntityUtils.setState(employee, EntityState.MODIFIED);
			resolver.resolve(dataItems);
		} 
		{
			EntityUtils.setState(dept, EntityState.DELETED);
			resolver.resolve(dataItems);
		}
	}
}
