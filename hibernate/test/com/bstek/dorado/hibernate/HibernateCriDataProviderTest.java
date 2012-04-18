package com.bstek.dorado.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.entity.EntityList;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.hibernate.criteria.Alias;
import com.bstek.dorado.hibernate.criteria.FetchMode;
import com.bstek.dorado.hibernate.criteria.SubCriteria;
import com.bstek.dorado.hibernate.criteria.TopCriteria;
import com.bstek.dorado.hibernate.criteria.criterion.BaseCriterion;
import com.bstek.dorado.hibernate.criteria.criterion.SqlCriterion;
import com.bstek.dorado.hibernate.criteria.criterion.SubQueryNoValueCriterion;
import com.bstek.dorado.hibernate.criteria.criterion.SubQueryPropertyCriterion;
import com.bstek.dorado.hibernate.criteria.criterion.SubQueryValueCriterion;
import com.bstek.dorado.hibernate.criteria.order.Order;
import com.bstek.dorado.hibernate.criteria.projection.BaseProjection;
import com.bstek.dorado.hibernate.criteria.projection.SqlProjection;
import com.bstek.dorado.hibernate.criteria.projection.SqlProjection.Column;
import com.bstek.dorado.hibernate.provider.CriteriaDataProvider;

public class HibernateCriDataProviderTest extends HibernateContextTestCase {
	
	protected CriteriaDataProvider getDataProvider(String name) throws Exception {
		Context conetxt = Context.getCurrent();
		DataProviderManager dataProviderManager = (DataProviderManager) conetxt
				.getServiceBean("dataProviderManager");
		return (CriteriaDataProvider)dataProviderManager.getDataProvider(name);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testGetDataProvider1() throws Exception {
		CriteriaDataProvider provider = getDataProvider("testCriProvider1");
		assertNotNull(provider);

		TopCriteria criteria = provider.getCriteria();
		assertNotNull(criteria);
		assertEquals("com.bstek.dorado.hibernate.entity.Product", criteria.getEntityName());
		assertEquals("product", criteria.getAlias());

		List<BaseCriterion> criterions = criteria.getCriterions();
		assertFalse(criterions.isEmpty());

		List<Order> orders = criteria.getOrders();
		assertFalse(orders.isEmpty());

		Map parameter = new HashMap();
		parameter.put("id1", 6);
		parameter.put("id2", 60);

		Object result = provider.getResult(parameter);
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
		CriteriaDataProvider provider = getDataProvider("testCriProvider2");
		assertNotNull(provider);

		TopCriteria criteria = provider.getCriteria();
		assertNotNull(criteria);
		assertEquals("com.bstek.dorado.hibernate.entity.Category", criteria.getEntityName());
		assertEquals("category", criteria.getAlias());
		
		List<BaseCriterion> criterionList = criteria.getCriterions();
		assertEquals(1, criterionList.size());
		
		provider.getResult();
	}
	
	public void testGetDataProvider_projections() throws Exception {
		CriteriaDataProvider provider = getDataProvider("testCriProvider_projections");
		assertNotNull(provider);

		TopCriteria criteria = provider.getCriteria();
		assertNotNull(criteria);
		
		List<BaseProjection> projectionList = criteria.getProjections();
		assertEquals(8, projectionList.size());
		
		provider.getResult();
	}

	public void testGetDataProvider_aliases() throws Exception {
		CriteriaDataProvider provider = getDataProvider("testCriProvider_aliases");
		assertNotNull(provider);

		TopCriteria criteria = provider.getCriteria();
		assertNotNull(criteria);
		
		List<Alias> aliasList = criteria.getAliases();
		assertEquals(1, aliasList.size());
		{
			Alias alias = aliasList.get(0);
			assertEquals("cc2", alias.getAlias());
			assertEquals("category", alias.getAssociationPath());
			List<BaseCriterion> criterionList = alias.getCriterions();
			assertEquals(1, criterionList.size());
		}
		
		provider.getResult();
	}
	
	public void testGetDataProvider_fetchMode() throws Exception {
		CriteriaDataProvider provider = getDataProvider("testCriProvider_fetchmode");
		assertNotNull(provider);

		TopCriteria criteria = provider.getCriteria();
		assertNotNull(criteria);
		
		List<FetchMode> fetchModeList = criteria.getFetchModes();
		assertEquals(1, fetchModeList.size());
		
		provider.getResult();
	}
	
	public void testGetDataProvider_SubCriteria() throws Exception {
		CriteriaDataProvider provider = getDataProvider("testCriProvider_SubCriteria");
		assertNotNull(provider);

		TopCriteria criteria = provider.getCriteria();
		assertNotNull(criteria);
		
		List<SubCriteria> subcriteriaList = criteria.getSubCriterias();
		assertEquals(1, subcriteriaList.size());
		
		provider.getResult();
	}
	
	public void testGetDataProvider_sqlProjection() throws Exception {
		CriteriaDataProvider provider = getDataProvider("testCriProvider_sqlProjection");
		assertNotNull(provider);

		TopCriteria criteria = provider.getCriteria();
		assertNotNull(criteria);
		
		List<BaseProjection> projectionList = criteria.getProjections();
		assertEquals(1, projectionList.size());
		{
			SqlProjection proj = (SqlProjection) projectionList.get(0);
			assertNotNull(proj.getClause());
			
			List<Column> columnList = proj.getColumns();
			assertEquals(2, columnList.size());
		}
		
		provider.getResult();
	}
	
	public void testGetDataProvider_sqlCriteria() throws Exception {
		CriteriaDataProvider provider = getDataProvider("testCriProvider_sqlCriteria");
		assertNotNull(provider);

		TopCriteria criteria = provider.getCriteria();
		assertNotNull(criteria);
		
		List<BaseCriterion> criterionList = criteria.getCriterions();
		assertEquals(1, criterionList.size());
		{
			SqlCriterion criterion = (SqlCriterion)criterionList.get(0);
			assertNotNull(criterion.getClause());
			assertEquals(1, criterion.getParameters().size());
		}
		
		provider.getResult();
	}
	
	public void testGetDataProvider_SubQueryPropertyCriterion() throws Exception {
		CriteriaDataProvider provider = getDataProvider("testCriProvider_SubQueryPropertyCriterion");
		assertNotNull(provider);

		TopCriteria criteria = provider.getCriteria();
		assertNotNull(criteria);
		
		List<BaseCriterion> criterionList = criteria.getCriterions();
		assertEquals(1, criterionList.size());
		{
			SubQueryPropertyCriterion criterion = (SubQueryPropertyCriterion)criterionList.get(0);
			TopCriteria innerCriteria = criterion.getCriteria();
			assertNotNull(innerCriteria);
		}
		
		provider.getResult();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testGetDataProvider_SubQueryValueCriterion() throws Exception {
		CriteriaDataProvider provider = getDataProvider("testCriProvider_SubQueryValueCriterion");
		assertNotNull(provider);

		TopCriteria criteria = provider.getCriteria();
		assertNotNull(criteria);
		
		List<BaseCriterion> criterionList = criteria.getCriterions();
		assertEquals(1, criterionList.size());
		{
			SubQueryValueCriterion criterion = (SubQueryValueCriterion)criterionList.get(0);
			TopCriteria innerCriteria = criterion.getCriteria();
			assertNotNull(innerCriteria);
		}
		
		Map parameter = new HashMap();
		parameter.put("pce", 33);
		provider.getResult(parameter);
	}
	
	public void testGetDataProvider_SubQueryNoValueCriterion() throws Exception {
		CriteriaDataProvider provider = getDataProvider("testCriProvider_SubQueryNoValueCriterion");
		assertNotNull(provider);

		TopCriteria criteria = provider.getCriteria();
		assertNotNull(criteria);
		
		List<BaseCriterion> criterionList = criteria.getCriterions();
		assertEquals(1, criterionList.size());
		{
			SubQueryNoValueCriterion criterion = (SubQueryNoValueCriterion)criterionList.get(0);
			TopCriteria innerCriteria = criterion.getCriteria();
			assertNotNull(innerCriteria);
		}
		
		provider.getResult();
	}
	
	public void testGetDataProvider_SubCriteria2() throws Exception {
		CriteriaDataProvider provider = getDataProvider("testCriProvider_SubCriteria2");
		assertNotNull(provider);

		TopCriteria criteria = provider.getCriteria();
		assertNotNull(criteria);
		
		List<SubCriteria> subcriteriaList = criteria.getSubCriterias();
		assertEquals(1, subcriteriaList.size());
		{
			SubCriteria sc = subcriteriaList.get(0);
			assertEquals("cg", sc.getAlias());
			assertEquals(1, sc.getSubCriterias().size());
		}
		provider.getResult();
	}
}
