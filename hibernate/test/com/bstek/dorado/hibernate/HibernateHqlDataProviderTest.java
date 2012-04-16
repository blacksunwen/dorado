package com.bstek.dorado.hibernate;

import java.util.HashMap;
import java.util.Map;

import com.bstek.dorado.data.entity.EntityList;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.hibernate.entity.Category;
import com.bstek.dorado.hibernate.provider.HqlDataProvider;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class HibernateHqlDataProviderTest extends HibernateContextTestCase {
	
	private HqlDataProvider getDataProvider(String name) throws Exception {
		return (HqlDataProvider)getDataProviderManager().getDataProvider(name);
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

	public void testQueryProvider1_vm1() throws Exception {
		HqlDataProvider provider = getDataProvider("testHqlProvider_vm1");
		Category parameter = new Category();
		Category parent = new Category();
		parent.setId(1L);
		parameter.setParent(parent);

		EntityList objList = (EntityList) provider.getResult(parameter);

		assertEquals(objList.size(), 1);
	}

	public void testQueryProvider1_vm_exset() throws Exception {
		HqlDataProvider provider = getDataProvider("testHqlProvider_vm_exset");
		Category parameter = new Category();
		Category parent = new Category();
		parent.setId(1L);
		parameter.setParent(parent);

		boolean error = false;
		try {
			provider.getResult(parameter);
		} catch (Exception e) {
			error = true;
		}

		assertEquals(error, true);
	}
}
