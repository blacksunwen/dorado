package com.bstek.dorado.hibernate;

import java.util.HashMap;
import java.util.Map;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.data.type.AggregationDataType;
import com.bstek.dorado.data.type.DefaultEntityDataType;
import com.bstek.dorado.data.type.property.BasePropertyDef;
import com.bstek.dorado.hibernate.provider.HqlDataProvider;

public class HqlDataProvider_UserCriteriaTest extends HibernateContextTestCase {
	protected DataProviderManager getDataProviderManager() throws Exception {
		Context conetxt = Context.getCurrent();
		DataProviderManager dataProviderManager = (DataProviderManager) conetxt
				.getServiceBean("dataProviderManager");
		return dataProviderManager;
	}

	protected HqlDataProvider getDataProvider(String id) throws Exception {
		return (HqlDataProvider) getDataProviderManager().getDataProvider(id);
	}

	/*************************** Orders **************************/
	@SuppressWarnings("rawtypes")
	public void test_usercri_hql_1() throws Exception {
		System.out
				.println("test_usercri_hql_1()=======================================================");
		HqlDataProvider provider = this.getDataProvider("test_usercri_hql_1");

		Map parameter = new HashMap();
		this.buildOrders(parameter, new Object[][] { new Object[] {
				"productName", false } });

		DefaultEntityDataType elementDataType = new DefaultEntityDataType();
		elementDataType
				.setMatchType(com.bstek.dorado.hibernate.entity.Product.class);
		elementDataType.setAutoCreatePropertyDefs(true);
		AggregationDataType resultDataType = new AggregationDataType();
		resultDataType.setElementDataType(elementDataType);

		provider.getResult(parameter, resultDataType);
	}

	@SuppressWarnings("rawtypes")
	public void test_usercri_hql_2() throws Exception {
		System.out
				.println("test_usercri_hql_2()=======================================================");
		HqlDataProvider provider = this.getDataProvider("test_usercri_hql_2");

		DefaultEntityDataType elementDataType = new DefaultEntityDataType();
		elementDataType
				.setMatchType(com.bstek.dorado.hibernate.entity.Product.class);
		elementDataType.setAutoCreatePropertyDefs(true);

		// productName重命名为name
		BasePropertyDef pd = new BasePropertyDef();
		pd.setName("name");
		pd.setPropertyPath("productName");

		elementDataType.addPropertyDef(pd);
		AggregationDataType resultDataType = new AggregationDataType();
		resultDataType.setElementDataType(elementDataType);

		// 设置UserCriteria
		Map parameter = new HashMap();
		this.buildOrders(parameter, new Object[][] { new Object[] { "name",
				false } });
		provider.getResult(parameter, resultDataType);
	}

	@SuppressWarnings("rawtypes")
	public void test_usercri_hql_3() throws Exception {
		System.out
				.println("test_usercri_hql_3()=======================================================");
		HqlDataProvider provider = this.getDataProvider("test_usercri_hql_3");

		DefaultEntityDataType productDataType = new DefaultEntityDataType();
		productDataType
				.setMatchType(com.bstek.dorado.hibernate.entity.Product.class);
		productDataType.setAutoCreatePropertyDefs(true);

		DefaultEntityDataType categoryDataType = new DefaultEntityDataType();
		categoryDataType.setAutoCreatePropertyDefs(true);
		categoryDataType
				.setMatchType(com.bstek.dorado.hibernate.entity.Category.class);
		BasePropertyDef categoryProperty = new BasePropertyDef();
		categoryProperty.setName("category");
		categoryProperty.setDataType(categoryDataType);
		productDataType.addPropertyDef(categoryProperty);

		AggregationDataType resultDataType = new AggregationDataType();
		resultDataType.setElementDataType(productDataType);

		// 设置UserCriteria
		Map parameter = new HashMap();
		this.buildOrders(parameter, new Object[][] {
				new Object[] { "productName", true },
				new Object[] { "category.categoryName", false }, });
		provider.getResult(parameter, resultDataType);
	}

	/*************************** Where **************************/
	@SuppressWarnings("rawtypes")
	public void test_usercri_hql_w1() throws Exception {
		System.out
				.println("test_usercri_hql_w1()=======================================================");
		HqlDataProvider provider = this.getDataProvider("test_usercri_hql_w1");

		DefaultEntityDataType elementDataType = new DefaultEntityDataType();
		elementDataType
				.setMatchType(com.bstek.dorado.hibernate.entity.Product.class);
		elementDataType.setAutoCreatePropertyDefs(true);
		AggregationDataType resultDataType = new AggregationDataType();
		resultDataType.setElementDataType(elementDataType);

		Map parameter = new HashMap();
		this.buildCriterions(parameter, new String[][] { new String[] { "id",
				"4" }, });
		provider.getResult(parameter, resultDataType);
	}

	@SuppressWarnings("rawtypes")
	public void test_usercri_hql_w2() throws Exception {
		System.out
				.println("test_usercri_hql_w2()=======================================================");
		HqlDataProvider provider = this.getDataProvider("test_usercri_hql_w2");

		// DataType
		DefaultEntityDataType productDataType = new DefaultEntityDataType();
		productDataType
				.setMatchType(com.bstek.dorado.hibernate.entity.Product.class);
		productDataType.setAutoCreatePropertyDefs(true);

		DefaultEntityDataType categoryDataType = new DefaultEntityDataType();
		categoryDataType.setAutoCreatePropertyDefs(true);
		categoryDataType
				.setMatchType(com.bstek.dorado.hibernate.entity.Category.class);
		BasePropertyDef categoryProperty = new BasePropertyDef();
		categoryProperty.setName("category");
		categoryProperty.setDataType(categoryDataType);
		productDataType.addPropertyDef(categoryProperty);

		AggregationDataType resultDataType = new AggregationDataType();
		resultDataType.setElementDataType(productDataType);

		// UserCriteria
		Map parameter = new HashMap();
		this.buildCriterions(parameter, new String[][] {
				new String[] { "id", "4" },
				new String[] { "category.id", "4" },
				new String[] { "category.id", "(1,2,3)" }, });

		provider.getResult(parameter, resultDataType);
	}

	@SuppressWarnings("rawtypes")
	public void test_usercri_hql_w3() throws Exception {
		System.out
				.println("test_usercri_hql_w3()=======================================================");
		HqlDataProvider provider = this.getDataProvider("test_usercri_hql_w3");

		// DataType
		DefaultEntityDataType productDataType = new DefaultEntityDataType();
		productDataType.setAutoCreatePropertyDefs(true);
		productDataType
				.setMatchType(com.bstek.dorado.hibernate.entity.Product.class);
		AggregationDataType resultDataType = new AggregationDataType();
		resultDataType.setElementDataType(productDataType);

		DefaultEntityDataType categoryDataType = new DefaultEntityDataType();
		categoryDataType.setAutoCreatePropertyDefs(true);
		categoryDataType
				.setMatchType(com.bstek.dorado.hibernate.entity.Category.class);
		BasePropertyDef categoryProperty = new BasePropertyDef();
		categoryProperty.setName("category");
		categoryProperty.setDataType(categoryDataType);
		productDataType.addPropertyDef(categoryProperty);

		Map parameter = new HashMap();
		this.buildCriterions(parameter, new String[][] {
				new String[] { "category.id", "4" },
				new String[] { "id", ">=4" },
				new String[] { "category.id", "<>4" },
				new String[] { "unitPrice", "<=6.8" },
				new String[] { "unitPrice", "<6.9" },
				new String[] { "unitPrice", ">8.99" },
				new String[] { "productName", "%C" },
				new String[] { "category.categoryName", "C%" },
				new String[] { "category.id", "[1,10]" },
				new String[] { "category.categoryName", "(bbb,ccc,ddd)" },
				new String[] { "category.categoryName", "C" },
				new String[] { "category.id", "11" }, });
		this.buildOrders(parameter, new Object[][] {
				new Object[] { "unitPrice", true },
				new Object[] { "productName", true }, });

		provider.getResult(parameter, resultDataType);
	}

	@SuppressWarnings("rawtypes")
	public void test_usercri_hql_w4() throws Exception {
		System.out
				.println("test_usercri_hql_w4()=======================================================");
		HqlDataProvider provider = this.getDataProvider("test_usercri_hql_w4");

		Map parameter = new HashMap();
		this.buildCriterions(parameter, new String[][] {
				new String[] { "category.categoryName", "%C" },
				new String[] { "category.parent.categoryName", "%B" }, });

		DefaultEntityDataType productDataType = new DefaultEntityDataType();
		productDataType.setAutoCreatePropertyDefs(true);
		productDataType
				.setMatchType(com.bstek.dorado.hibernate.entity.Product.class);
		AggregationDataType resultDataType = new AggregationDataType();
		resultDataType.setElementDataType(productDataType);

		DefaultEntityDataType categoryDataType = new DefaultEntityDataType();
		categoryDataType.setAutoCreatePropertyDefs(true);
		categoryDataType
				.setMatchType(com.bstek.dorado.hibernate.entity.Category.class);
		BasePropertyDef categoryProperty = new BasePropertyDef();
		categoryProperty.setName("category");
		categoryProperty.setDataType(categoryDataType);

		DefaultEntityDataType pcategoryDataType = new DefaultEntityDataType();
		pcategoryDataType.setAutoCreatePropertyDefs(true);
		pcategoryDataType
				.setMatchType(com.bstek.dorado.hibernate.entity.Category.class);
		BasePropertyDef pcategoryProperty = new BasePropertyDef();
		pcategoryProperty.setName("parent");
		pcategoryProperty.setDataType(pcategoryDataType);

		categoryDataType.addPropertyDef(pcategoryProperty);
		productDataType.addPropertyDef(categoryProperty);

		provider.getResult(parameter, resultDataType);
	}
}
