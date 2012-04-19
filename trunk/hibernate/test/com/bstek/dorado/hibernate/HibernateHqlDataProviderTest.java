package com.bstek.dorado.hibernate;

import java.util.HashMap;
import java.util.Map;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.entity.EntityList;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.hibernate.entity.Category;
import com.bstek.dorado.hibernate.entity.Product;
import com.bstek.dorado.hibernate.provider.HqlDataProvider;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class HibernateHqlDataProviderTest extends HibernateContextTestCase {
	
	private HqlDataProvider getDataProvider(String name) throws Exception {
		Context conetxt = Context.getCurrent();
		DataProviderManager dataProviderManager = (DataProviderManager) conetxt
				.getServiceBean("dataProviderManager");
		return (HqlDataProvider)dataProviderManager.getDataProvider(name);
	}

	public void testGetDataProvider1() throws Exception {
		HqlDataProvider provider = getDataProvider("testHqlProvider1");
		assertNotNull(provider);
		String hqlClause = "from Category where id = :id";
		assertEquals(provider.getHql(), hqlClause);
	}
	
	public void testQueryProvider1_map() throws Exception {
		HqlDataProvider provider = getDataProvider("testHqlProvider1");
		Map parameter = new HashMap();
		parameter.put("id", 1L);

		EntityList objList = (EntityList) provider.getResult(parameter);

		assertEquals(objList.size(), 1);
	}
	
	public void testQueryProvider1_pojo() throws Exception {
		HqlDataProvider provider = getDataProvider("testHqlProvider1");
		Category parameter = new Category();
		parameter.setId(1L);

		EntityList objList = (EntityList) provider.getResult(parameter);

		assertEquals(objList.size(), 1);
	}
	
	public void testQueryProvider1_$() throws Exception {
		HqlDataProvider provider = getDataProvider("testHqlProvider$");

		Long parameter = 1L;

		{
			EntityList objList = (EntityList) provider.getResult(parameter);
			assertEquals(objList.size(), 1);
		}
		{
			EntityList objList = (EntityList) provider.getResult();
			assertEquals(objList.size(), 0);
		}
		{
			Page page = new Page(10, 2);
			provider.getResult(parameter, page);
			assertEquals(page.getEntityCount(), 1);
			assertEquals(page.getEntities().size(), 0);
		}
		{
			Page page = new Page(5, 1);
			provider.getResult(page);
			assertEquals(page.getEntityCount(), 0);
			assertEquals(page.getEntities().size(), 0);
		}
	}

	public void testQueryProvider1_map2() throws Exception {
		HqlDataProvider provider = getDataProvider("testHqlProvider_map2");
		Map parameter = new HashMap();
		Map mm = new HashMap();
		parameter.put("mm", mm);
		mm.put("id", 1L);

		EntityList objList = (EntityList) provider.getResult(parameter);

		assertEquals(objList.size(), 1);
	}

	public void testQueryProvider1_pojo2() throws Exception {
		HqlDataProvider provider = getDataProvider("testHqlProvider_pojo2");
		Category parameter = new Category();
		Category parent = new Category();
		parent.setId(1L);
		parameter.setParent(parent);

		EntityList objList = (EntityList) provider.getResult(parameter);

		assertEquals(objList.size(), 1);
	}
	
	public void testQueryProvider1_dataType1() throws Exception {
		HqlDataProvider provider = getDataProvider("testHqlProvider_dataType1");
		Map parameter = new HashMap();
		{
			parameter = new HashMap();
			parameter.put("unitPrice1", Integer.valueOf(10));
			EntityList objList = (EntityList) provider.getResult(parameter);
			for (Product p: (Product[])objList.toArray(new Product[0])) {
				assertTrue(p.getUnitPrice() >= 10);
			}
		} 
		{
			parameter = new HashMap();
			parameter.put("unitPrice2", Integer.valueOf(20));
			EntityList objList = (EntityList) provider.getResult(parameter);
			for (Product p: (Product[])objList.toArray(new Product[0])) {
				assertTrue(p.getUnitPrice() <= 20);
			}
		}
		{
			parameter = new HashMap();
			parameter.put("unitPrice1", Integer.valueOf(10));
			parameter.put("unitPrice2", Integer.valueOf(20));
			EntityList objList = (EntityList) provider.getResult(parameter);
			for (Product p: (Product[])objList.toArray(new Product[0])) {
				assertTrue(p.getUnitPrice() >= 10);
				assertTrue(p.getUnitPrice() <= 20);
			}
		}
		{
			parameter = new HashMap();
			parameter.put("productName", "C");
			parameter.put("unitPrice1", Integer.valueOf(10));
			parameter.put("unitPrice2", Integer.valueOf(20));
			EntityList objList = (EntityList) provider.getResult(parameter);
			for (Product p: (Product[])objList.toArray(new Product[0])) {
				assertTrue(p.getUnitPrice() >= 10);
				assertTrue(p.getUnitPrice() <= 20);
				assertTrue(p.getProductName().indexOf("C") >= 0);
			}
		}
	}

	public void testQueryProvider1_vm1() throws Exception {
		HqlDataProvider provider = getDataProvider("testHqlProvider_vm1");
		Category parameter = new Category();
		Category parent = new Category();
		parent.setId(1L);
		parameter.setParent(parent);

		EntityList objList = (EntityList) provider.getResult(parameter);

		assertEquals(objList.size(), 1);
	}

	public void testQueryProvider1_vm_set1() throws Exception {
		HqlDataProvider provider = getDataProvider("testHqlProvider_set1");
		Category parameter = new Category();
		Category parent = new Category();
		parent.setId(1L);
		parameter.setParent(parent);

		provider.getResult(parameter);

		assertEquals(99, parent.getId());
	}
	
	public void testQueryProvider1_vm_set2() throws Exception {
		HqlDataProvider provider = getDataProvider("testHqlProvider_set2");
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("productName", "pn");
		provider.getResult(parameter);

		assertEquals("%pn%", parameter.get("productName"));
	} 
}
