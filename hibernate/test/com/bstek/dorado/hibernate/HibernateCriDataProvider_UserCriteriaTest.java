package com.bstek.dorado.hibernate;

import java.util.HashMap;
import java.util.Map;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.data.type.AggregationDataType;
import com.bstek.dorado.data.type.DefaultEntityDataType;
import com.bstek.dorado.data.type.property.BasePropertyDef;
import com.bstek.dorado.hibernate.provider.CriteriaDataProvider;

public class HibernateCriDataProvider_UserCriteriaTest extends
		HibernateContextTestCase {

	protected DataProviderManager getDataProviderManager() throws Exception {
		Context conetxt = Context.getCurrent();
		DataProviderManager dataProviderManager = (DataProviderManager) conetxt
				.getServiceBean("dataProviderManager");
		return dataProviderManager;
	}

	protected CriteriaDataProvider getDataProvider(String id) throws Exception {
		return (CriteriaDataProvider) getDataProviderManager().getDataProvider(
				id);
	}

	// lower(this_.PRODUCT_NAME) asc
	@SuppressWarnings("rawtypes")
	public void test_1() throws Exception {
		System.out
				.println("test_1()=======================================================");
		CriteriaDataProvider provider = this.getDataProvider("test_1");

		DefaultEntityDataType elementDataType = new DefaultEntityDataType();
		elementDataType
				.setMatchType(com.bstek.dorado.hibernate.entity.Product.class);
		elementDataType.setAutoCreatePropertyDefs(true);
		AggregationDataType resultDataType = new AggregationDataType();
		resultDataType.setElementDataType(elementDataType);

		Map parameter = new HashMap();
		this.buildOrders(parameter, new Object[][] { new Object[] {
				"productName", false } });

		provider.getResult(parameter, resultDataType);
	}

	// this_.UNITS_IN_STOCK desc
	@SuppressWarnings("rawtypes")
	public void test_2() throws Exception {
		System.out
				.println("test_2()=======================================================");
		CriteriaDataProvider provider = this.getDataProvider("test_2");
		Map parameter = new HashMap();
		this.buildOrders(parameter, new Object[][] { new Object[] {
				"unitsInStock", true } });

		DefaultEntityDataType elementDataType = new DefaultEntityDataType();
		elementDataType
				.setMatchType(com.bstek.dorado.hibernate.entity.Product.class);
		elementDataType.setAutoCreatePropertyDefs(true);
		AggregationDataType resultDataType = new AggregationDataType();
		resultDataType.setElementDataType(elementDataType);

		provider.getResult(parameter, resultDataType);
	}

	@SuppressWarnings("rawtypes")
	public void test_3() throws Exception {
		System.out
				.println("test_3()=======================================================");
		CriteriaDataProvider provider = this.getDataProvider("test_3");
		Map parameter = new HashMap();
		this.buildOrders(parameter, new Object[][] {
				new Object[] { "unitsInStock", true },
				new Object[] { "productName", false } });

		DefaultEntityDataType elementDataType = new DefaultEntityDataType();
		elementDataType
				.setMatchType(com.bstek.dorado.hibernate.entity.Product.class);
		elementDataType.setAutoCreatePropertyDefs(true);
		AggregationDataType resultDataType = new AggregationDataType();
		resultDataType.setElementDataType(elementDataType);

		provider.getResult(parameter, resultDataType);
	}

	@SuppressWarnings("rawtypes")
	public void test_4() throws Exception {
		System.out
				.println("test_4()=======================================================");
		CriteriaDataProvider provider = this.getDataProvider("test_4");

		Map parameter = new HashMap();
		this.buildCriterions(parameter, new String[][] {
				new String[] { "id", "4" },
				new String[] { "category.id", "4" },
				new String[] { "category.id", "(1,2,3)" }, });

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

		provider.getResult(parameter, resultDataType);
	}

	@SuppressWarnings("rawtypes")
	public void test_5() throws Exception {
		System.out
				.println("test_5()=======================================================");
		CriteriaDataProvider provider = this.getDataProvider("test_5");
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

		provider.getResult(parameter, resultDataType);
	}

	@SuppressWarnings("rawtypes")
	public void test_6() throws Exception {
		System.out
				.println("test_6()=======================================================");
		CriteriaDataProvider provider = this.getDataProvider("test_6");
		Map parameter = new HashMap();
		this.buildCriterions(parameter, new String[][] { new String[] {
				"category.categoryName", "%C" }, });

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

		provider.getResult(parameter, resultDataType);
	}

	@SuppressWarnings("rawtypes")
	public void test_7() throws Exception {
		System.out
				.println("test_7()=======================================================");
		CriteriaDataProvider provider = this.getDataProvider("test_7");

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

	@SuppressWarnings("rawtypes")
	public void test_8() throws Exception {
		System.out
				.println("test_8()=======================================================");
		CriteriaDataProvider provider = this.getDataProvider("test_8");

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

	@SuppressWarnings("rawtypes")
	public void test_9() throws Exception {
		System.out
				.println("test_9()=======================================================");
		CriteriaDataProvider provider = this.getDataProvider("test_9");

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
