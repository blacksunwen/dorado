package com.bstek.dorado.hibernate;

import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Order;
import com.bstek.dorado.data.type.DefaultEntityDataType;
import com.bstek.dorado.data.util.DataUtils;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.hibernate.provider.CriteriaDataProvider;

public class HibernateCriDataProvider_UserCriteriaTest extends
		HibernateContextTestCase {

	protected CriteriaDataProvider getDataProvider(String name) throws Exception {
		return (CriteriaDataProvider)getDataProviderManager().getDataProvider(name);
	}
	
	

	// lower(this_.PRODUCT_NAME) asc
	public void test_1() throws Exception {
		System.out.println("test_1()=======================================================");
		CriteriaDataProvider provider = getDataProvider("test_1");

		DefaultEntityDataType dataType = new DefaultEntityDataType();
		dataType.setMatchType(com.bstek.dorado.hibernate.entity.Product.class);
		dataType.setAutoCreatePropertyDefs(true);

		Criteria criteria = new Criteria();
		{
			criteria.addOrder(new Order("productName", false));
		}
		
		Record parameter = new Record();
		parameter.put("criteria", criteria);

		provider.getResult(parameter);
	}

	// this_.UNITS_IN_STOCK desc
	public void test_2() throws Exception {
		System.out.println("test_2()=======================================================");
		CriteriaDataProvider provider = getDataProvider("test_2");
		
		Criteria criteria = new Criteria();
		{
			criteria.addOrder(new Order("unitsInStock", true));
		}

		Record parameter = new Record();
		parameter.put("criteria", criteria);
		
		provider.getResult(parameter);
	}

	//this_.UNITS_IN_STOCK desc,
	//lower(this_.PRODUCT_NAME) asc,
	public void test_3() throws Exception {
		System.out.println("test_3()=======================================================");
		CriteriaDataProvider provider = getDataProvider("test_3");
		
		Criteria criteria = new Criteria();
		{
			criteria.addOrder(new Order("unitsInStock", true));
			criteria.addOrder(new Order("productName", false));
		}
		
		Record parameter = new Record();
		parameter.put("criteria", criteria);
		provider.getResult(parameter);
	}

	/*
	 this_.id=? 
     and this_.CATEGORY_ID=? 
     and this_.CATEGORY_ID in (
        ?, ?, ?
     )
	 */
	public void test_4() throws Exception {
		System.out.println("test_4()=======================================================");
		CriteriaDataProvider provider = getDataProvider("test_4");

		Criteria criteria = new Criteria();
		{
			criteria.addCriterion(createFilterCriterion("id", DataUtils.getDataType(long.class), "4"));
			criteria.addCriterion(createFilterCriterion("category.id", DataUtils.getDataType(long.class), "4"));
			criteria.addCriterion(createFilterCriterion("category.id", DataUtils.getDataType(long.class), "(1,2,3)"));
		}
		
		Record parameter = new Record();
		parameter.put("criteria", criteria);
		provider.getResult(parameter);
	}
/*
    where
        cg1_.id=? 
        and this_.id>=? 
        and cg1_.id<>? 
        and this_.UNIT_PRICE<=? 
        and this_.UNIT_PRICE<? 
        and this_.UNIT_PRICE>? 
        and this_.PRODUCT_NAME like ? 
        and cg1_.CATEGORY_NAME like ? 
        and cg1_.id between ? and ? 
        and cg1_.CATEGORY_NAME in (
            ?, ?, ?
        ) 
        and cg1_.CATEGORY_NAME like ? 
        and cg1_.id=? 
    order by
        this_.UNIT_PRICE desc,
        this_.PRODUCT_NAME desc
 */
	public void test_5() throws Exception {
		System.out.println("test_5()=======================================================");
		CriteriaDataProvider provider = getDataProvider("test_5");
		
		Criteria criteria = new Criteria();
		{
			criteria.addCriterion(createFilterCriterion("category.id", DataUtils.getDataType(long.class), "4"));
			criteria.addCriterion(createFilterCriterion("id", DataUtils.getDataType(long.class), ">=4"));
			criteria.addCriterion(createFilterCriterion("category.id", DataUtils.getDataType(long.class), "<>4"));
			criteria.addCriterion(createFilterCriterion("unitPrice", DataUtils.getDataType(float.class), "<=6.8"));
			criteria.addCriterion(createFilterCriterion("unitPrice", DataUtils.getDataType(float.class), "<6.9"));
			criteria.addCriterion(createFilterCriterion("unitPrice", DataUtils.getDataType(float.class), ">8.99"));
			criteria.addCriterion(createFilterCriterion("productName", DataUtils.getDataType(String.class), "C"));
			criteria.addCriterion(createFilterCriterion("category.categoryName", DataUtils.getDataType(String.class), "%C"));
			criteria.addCriterion(createFilterCriterion("category.id", DataUtils.getDataType(long.class), "[1,10]"));
			criteria.addCriterion(createFilterCriterion("category.categoryName", DataUtils.getDataType(String.class), "(bbb,ccc,ddd)"));
			criteria.addCriterion(createFilterCriterion("category.categoryName", DataUtils.getDataType(String.class), "C"));
			criteria.addCriterion(createFilterCriterion("category.id", DataUtils.getDataType(long.class), "11"));
		}
		{
			criteria.addOrder(new Order("unitPrice", true));
			criteria.addOrder(new Order("productName", true));
		}
		
		Record parameter = new Record();
		parameter.put("criteria", criteria);
		provider.getResult(parameter);
	}

	public void test_6() throws Exception {
		System.out.println("test_6()=======================================================");
		CriteriaDataProvider provider = getDataProvider("test_6");
		
		Criteria criteria = new Criteria();
		{
			criteria.addCriterion(createFilterCriterion("category.categoryName", DataUtils.getDataType(String.class), "%C"));
		}
		
		Record parameter = new Record();
		parameter.put("criteria", criteria);
		provider.getResult(parameter);
	}

	public void test_7() throws Exception {
		System.out.println("test_7()=======================================================");
		CriteriaDataProvider provider = getDataProvider("test_7");

		Criteria criteria = new Criteria();
		{
			criteria.addCriterion(createFilterCriterion("category.categoryName", DataUtils.getDataType(String.class), "%C"));
			criteria.addCriterion(createFilterCriterion("category.parent.categoryName", DataUtils.getDataType(String.class), "%B"));
		}
		
		Record parameter = new Record();
		parameter.put("criteria", criteria);
		provider.getResult(parameter);
	}

	public void test_8() throws Exception {
		System.out.println("test_8()=======================================================");
		CriteriaDataProvider provider = getDataProvider("test_8");

		Criteria criteria = new Criteria();
		{
			criteria.addCriterion(createFilterCriterion("category.categoryName", DataUtils.getDataType(String.class), "%C"));
			criteria.addCriterion(createFilterCriterion("category.parent.categoryName", DataUtils.getDataType(String.class), "%B"));
		}
		
		Record parameter = new Record();
		parameter.put("criteria", criteria);
		provider.getResult(parameter);
	}

	public void test_9() throws Exception {
		System.out.println("test_9()=======================================================");
		CriteriaDataProvider provider = this.getDataProvider("test_9");

		Criteria criteria = new Criteria();
		{
			criteria.addCriterion(createFilterCriterion("category.categoryName", DataUtils.getDataType(String.class), "%C"));
			criteria.addCriterion(createFilterCriterion("category.parent.categoryName", DataUtils.getDataType(String.class), "%B"));
		}
		
		Record parameter = new Record();
		parameter.put("criteria", criteria);
		provider.getResult(parameter);
	}
}
