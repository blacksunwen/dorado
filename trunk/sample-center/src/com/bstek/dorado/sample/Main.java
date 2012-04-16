package com.bstek.dorado.sample;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.sample.dao.ExampleCategoryDao;
import com.bstek.dorado.sample.dao.ExampleDao;
import com.bstek.dorado.sample.entity.Example;
import com.bstek.dorado.sample.entity.ExampleCategory;

@Component
public class Main {
	@Resource
	private ExampleCategoryDao exampleCategoryDao;

	@Resource
	private ExampleDao exampleDao;

	@DataProvider
	public Collection<ExampleCategory> getCategories(Long parentCategoryId) {
		return exampleCategoryDao.find("from ExampleCategory where categoryId="
				+ parentCategoryId + " order by sortFlag");
	}

	@DataProvider
	public Collection<Example> getExamplesByCategoryId(Long categoryId)
			throws Exception {
		return exampleDao.find("select e "
				+ "from Example e, CategoryExampleRelation r "
				+ "where e.id = r.exampleId and r.categoryId = " + categoryId
				+ " order by r.sortFlag");
	}
}
