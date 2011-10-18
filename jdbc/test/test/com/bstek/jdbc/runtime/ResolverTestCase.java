package test.com.bstek.jdbc.runtime;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import test.com.bstek.jdbc.JdbcTestUtils;

import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcDataResolverItem;

public class ResolverTestCase extends ConfigManagerTestSupport {

	public ResolverTestCase() {
		this.addExtensionContextConfigLocation("test/com/bstek/jdbc/runtime/test-context.xml");
	}
	
	public void test1() throws Exception {
		//Resolver
		JdbcDataResolver resolver = new JdbcDataResolver();
		List<JdbcDataResolverItem> items = new ArrayList<JdbcDataResolverItem>();
		resolver.setItems(items);
		{
			JdbcDataResolverItem item = new JdbcDataResolverItem();
			item.setName("emp");
			item.setDbElement("EMP_0");
			items.add(item);
		}
		
		//DataItems
		{
			DataItems dataItems = new DataItems();
			Record r = JdbcTestUtils.Radom.radomEmployee(200);
			dataItems.set("emp", r);
			{
				r.getEntityEnhancer().setState(EntityState.DELETED);
				resolver.resolve(dataItems);
			}{
				r.getEntityEnhancer().setState(EntityState.NEW);
				resolver.resolve(dataItems);
			}{
				r.getEntityEnhancer().setState(EntityState.MODIFIED);
				r.set("p_reports_to", "200");
				resolver.resolve(dataItems);
			}{
				r.getEntityEnhancer().setState(EntityState.DELETED);
				resolver.resolve(dataItems);
			}
		}
	}
	
	public void test2() throws Exception {
		//Resolver
		JdbcDataResolver resolver = new JdbcDataResolver();
		List<JdbcDataResolverItem> items = new ArrayList<JdbcDataResolverItem>();
		resolver.setItems(items);
		{
			JdbcDataResolverItem item = new JdbcDataResolverItem();
			item.setName("emp");
			item.setDbElement("EMP_0");
			items.add(item);
		}{
			JdbcDataResolverItem item = new JdbcDataResolverItem();
			item.setName("catg");
			item.setDbElement("CATEGORIES");
			items.add(item);
		}
		
		//DataItems
		DataItems dataItems = new DataItems();
		{
			dataItems.set("emp", JdbcTestUtils.Radom.radomEmployee(200));
			
			List<Record> records = new ArrayList<Record>();
			dataItems.set("catg", records);
			records.add(JdbcTestUtils.Radom.radomCategory(201));
			records.add(JdbcTestUtils.Radom.radomCategory(202));
			records.add(JdbcTestUtils.Radom.radomCategory(203));
		}
		
		{
			JdbcTestUtils.setState(dataItems, "emp", EntityState.DELETED);
			JdbcTestUtils.setState(dataItems, "catg", EntityState.DELETED);
			resolver.resolve(dataItems);
		}{
			JdbcTestUtils.setState(dataItems, "emp", EntityState.NEW);
			JdbcTestUtils.setState(dataItems, "catg", EntityState.NEW);
			resolver.resolve(dataItems);
		}{
			JdbcTestUtils.setState(dataItems, "emp", EntityState.MODIFIED);
			JdbcTestUtils.setState(dataItems, "catg", EntityState.MODIFIED);
			resolver.resolve(dataItems);
		}{
			JdbcTestUtils.setState(dataItems, "emp", EntityState.DELETED);
			JdbcTestUtils.setState(dataItems, "catg", EntityState.DELETED);
			resolver.resolve(dataItems);
		}
		
	}
	
	public void test_3() throws Exception {
		//Resolver
		JdbcDataResolver resolver = new JdbcDataResolver();
		List<JdbcDataResolverItem> items = new ArrayList<JdbcDataResolverItem>();
		resolver.setItems(items);
		{
			JdbcDataResolverItem catgItem = new JdbcDataResolverItem();
			catgItem.setName("catg");
			catgItem.setDbElement("CATEGORIES");
			items.add(catgItem);
			
			{
				JdbcDataResolverItem productItem = new JdbcDataResolverItem();
				productItem.setName("products");
				productItem.setDbElement("PRODUCTS");
				productItem.setReferencedKeyProperties("ID");
				productItem.setForeignKeyProperties("CATEGORY_ID");
				catgItem.getItems().add(productItem);
			}
		}
		
		//DataItems
		DataItems dataItems = new DataItems();
		Record catg = JdbcTestUtils.Radom.radomCategory(301);
		List<Record> prods = new ArrayList<Record>();
		{
			dataItems.set("catg", catg);
			catg.set("products", prods);
			
			prods.add(JdbcTestUtils.Radom.radomProduct(1001, null));
			prods.add(JdbcTestUtils.Radom.radomProduct(1002, null));
		}
		
		{
			JdbcTestUtils.setState(dataItems, "catg", EntityState.DELETED);
			resolver.resolve(dataItems);
		}{
			JdbcTestUtils.setState(dataItems, "catg", EntityState.NEW);
			JdbcTestUtils.setState(prods, EntityState.NEW);
			resolver.resolve(dataItems);
			
			Assert.assertEquals(catg.get("ID"), prods.get(0).get("CATEGORY_ID"));
			Assert.assertEquals(catg.get("ID"), prods.get(1).get("CATEGORY_ID"));
		}
	}
	
	public void test_inc() throws Exception {
		//Resolver
		JdbcDataResolver resolver = new JdbcDataResolver();
		List<JdbcDataResolverItem> items = new ArrayList<JdbcDataResolverItem>();
		resolver.setItems(items);
		{
			JdbcDataResolverItem item = new JdbcDataResolverItem();
			item.setName("inc");
			item.setDbElement("T1_INCREMENT");
			items.add(item);
		}
		
		//DataItems
		{
			DataItems dataItems = new DataItems();
			Record r = JdbcTestUtils.Radom.radomInc();
			dataItems.set("inc", r);
			{
				r.getEntityEnhancer().setState(EntityState.NEW);
				resolver.resolve(dataItems);
				
				System.out.println("ID_INC: " + r.get("ID"));
				Assert.assertNotNull(r.get("ID"));
			}{
				r.getEntityEnhancer().setState(EntityState.MODIFIED);
				resolver.resolve(dataItems);
			}{
				r.getEntityEnhancer().setState(EntityState.DELETED);
				resolver.resolve(dataItems);
			}
		}
	}
	
	public void test_uuid() throws Exception {
		//Resolver
		JdbcDataResolver resolver = new JdbcDataResolver();
		List<JdbcDataResolverItem> items = new ArrayList<JdbcDataResolverItem>();
		resolver.setItems(items);
		{
			JdbcDataResolverItem item = new JdbcDataResolverItem();
			item.setName("uuid");
			item.setDbElement("T1_UUID");
			items.add(item);
		}
		
		//DataItems
		{
			DataItems dataItems = new DataItems();
			Record r = JdbcTestUtils.Radom.radomUuid();
			dataItems.set("uuid", r);
			{
				r.getEntityEnhancer().setState(EntityState.NEW);
				resolver.resolve(dataItems);
				
				System.out.println("ID_UUID: " + r.get("ID"));
				Assert.assertNotNull(r.get("ID"));
			}{
				r.getEntityEnhancer().setState(EntityState.MODIFIED);
				resolver.resolve(dataItems);
			}{
				r.getEntityEnhancer().setState(EntityState.DELETED);
				resolver.resolve(dataItems);
			}
		}
	}
	
	public void test_seq() throws Exception {
		//Resolver
		JdbcDataResolver resolver = new JdbcDataResolver();
		List<JdbcDataResolverItem> items = new ArrayList<JdbcDataResolverItem>();
		resolver.setItems(items);
		{
			JdbcDataResolverItem item = new JdbcDataResolverItem();
			item.setName("seq");
			item.setDbElement("T1_SEQUENCE");
			items.add(item);
		}
		
		//DataItems
		{
			DataItems dataItems = new DataItems();
			Record r = JdbcTestUtils.Radom.radomUuid();
			dataItems.set("seq", r);
			{
				r.getEntityEnhancer().setState(EntityState.NEW);
				resolver.resolve(dataItems);
				
				System.out.println("ID_UUID: " + r.get("ID"));
				Assert.assertNotNull(r.get("ID"));
			}{
				r.getEntityEnhancer().setState(EntityState.MODIFIED);
				resolver.resolve(dataItems);
			}{
				r.getEntityEnhancer().setState(EntityState.DELETED);
				resolver.resolve(dataItems);
			}
		}
	}
	

}
