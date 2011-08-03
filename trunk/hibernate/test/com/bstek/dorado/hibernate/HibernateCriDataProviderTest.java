package com.bstek.dorado.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.entity.EntityList;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.hibernate.criteria.TopCriteria;
import com.bstek.dorado.hibernate.criteria.criterion.BaseCriterion;
import com.bstek.dorado.hibernate.criteria.order.Order;
import com.bstek.dorado.hibernate.provider.CriteriaDataProvider;

public class HibernateCriDataProviderTest extends HibernateContextTestCase {
	protected DataProviderManager getDataProviderManager() throws Exception {
		Context conetxt = Context.getCurrent();
		DataProviderManager dataProviderManager = (DataProviderManager) conetxt
				.getServiceBean("dataProviderManager");
		return dataProviderManager;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testGetDataProvider1() throws Exception {
		CriteriaDataProvider provider = (CriteriaDataProvider) getDataProviderManager()
				.getDataProvider("testCriProvider1");
		assertNotNull(provider);

		TopCriteria criteria = provider.getCriteria();
		assertNotNull(criteria);
		assertEquals("com.bstek.dorado.hibernate.entity.Product",
				criteria.getEntityName());
		assertEquals("product", criteria.getAlias());

		List<BaseCriterion> criterions = criteria.getCriterions();
		assertFalse(criterions.isEmpty());

		List<Order> orders = criteria.getOrders();
		assertFalse(orders.isEmpty());

		Map parameter = new HashMap();
		parameter.put("id1", 6);
		parameter.put("id2", 60);

		Object result = provider.getResult(parameter);
		// Page page = new Page(10, 1);
		// provider.getResult(page);
		// Object result = page.getEntities();
		if (result instanceof EntityList) {
			EntityList objList = (EntityList) result;
			System.out.println("SIZE:: " + objList.size());
		} else {
			System.out.println("RESULT:: " + result);
		}
		
		Page page = new Page(10, 2);
		provider.getResult(parameter, page);
		result = page.getEntities();
		 
		if (result instanceof EntityList) {
			EntityList objList = (EntityList) result;
			System.out.println("SIZE:: " + objList.size());
		} else {
			System.out.println("RESULT:: " + result);
		}
	}
	
	public void testGetDataProvider2() throws Exception {
		CriteriaDataProvider provider = (CriteriaDataProvider) getDataProviderManager()
				.getDataProvider("testCriProvider2");
		assertNotNull(provider);

		TopCriteria criteria = provider.getCriteria();
		assertNotNull(criteria);
		assertEquals("com.bstek.dorado.hibernate.entity.Category",
				criteria.getEntityName());
		assertEquals("category", criteria.getAlias());
		
		provider.getResult();
	}
	
	public void testGetDataProvider_projections() throws Exception {
		CriteriaDataProvider provider = (CriteriaDataProvider) getDataProviderManager()
		.getDataProvider("testCriProvider_projections");
		
		provider.getResult();
	}
	
	public void testGetDataProvider_aliases() throws Exception {
		CriteriaDataProvider provider = (CriteriaDataProvider) getDataProviderManager()
		.getDataProvider("testCriProvider_aliases");
		
		provider.getResult();
	}
	
	public void testGetDataProvider_fetchMode() throws Exception {
		CriteriaDataProvider provider = (CriteriaDataProvider) getDataProviderManager()
		.getDataProvider("testCriProvider_fetchmode");
		
		provider.getResult();
	}
	
	public void testGetDataProvider_SubCriteria() throws Exception {
		CriteriaDataProvider provider = (CriteriaDataProvider) getDataProviderManager()
			.getDataProvider("testCriProvider_SubCriteria");
		
		provider.getResult();
	}
	
	public void testGetDataProvider_sqlProjection() throws Exception {
		CriteriaDataProvider provider = (CriteriaDataProvider) getDataProviderManager()
		.getDataProvider("testCriProvider_sqlProjection");
	
		provider.getResult();
	}
	
	public void testGetDataProvider_sqlCriteria() throws Exception {
		CriteriaDataProvider provider = (CriteriaDataProvider) getDataProviderManager()
			.getDataProvider("testCriProvider_sqlCriteria");
	
		provider.getResult();
	}
	
	public void testGetDataProvider_SubQueryPropertyCriterion() throws Exception {
		CriteriaDataProvider provider = (CriteriaDataProvider) getDataProviderManager()
		.getDataProvider("testCriProvider_SubQueryPropertyCriterion");
	
		provider.getResult();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testGetDataProvider_SubQueryValueCriterion() throws Exception {
		CriteriaDataProvider provider = (CriteriaDataProvider) getDataProviderManager()
		.getDataProvider("testCriProvider_SubQueryValueCriterion");
	
		Map parameter = new HashMap();
		parameter.put("pce", 33);
		provider.getResult(parameter);
	}
	
	public void testGetDataProvider_SubQueryNoValueCriterion() throws Exception {
		CriteriaDataProvider provider = (CriteriaDataProvider) getDataProviderManager()
			.getDataProvider("testCriProvider_SubQueryNoValueCriterion");
	
		provider.getResult();
	}
}
