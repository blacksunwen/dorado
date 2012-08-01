package com.bstek.dorado.sample;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.sample.dao.ExampleCategoryDao;
import com.bstek.dorado.sample.dao.ExampleDao;
import com.bstek.dorado.sample.entity.Example;
import com.bstek.dorado.sample.entity.ExampleCategory;

@Component
public class Main {
	private static final Long RECENT_EXAMPLE_CATEGORY = -100L;

	@Resource
	private ExampleCategoryDao exampleCategoryDao;

	@Resource
	private ExampleDao exampleDao;

	@DataProvider
	public Collection<ExampleCategory> getCategories(Long parentCategoryId) {
		List<ExampleCategory> categories = exampleCategoryDao
				.find("from ExampleCategory where categoryId="
						+ parentCategoryId + " order by sortFlag");
		if (parentCategoryId == null) {
			ExampleCategory recentExamplesCategory = new ExampleCategory();
			recentExamplesCategory.setId(RECENT_EXAMPLE_CATEGORY);
			recentExamplesCategory.setLabel("最近更新");
			recentExamplesCategory.setIcon(">images/recent.gif");
			categories.add(0, recentExamplesCategory);
		}
		return categories;
	}

	@DataProvider
	public Collection<Example> getExamplesByCategoryId(Long categoryId)
			throws Exception {
		if (RECENT_EXAMPLE_CATEGORY.equals(categoryId)) {
			Page<Example> page = new Page<Example>(15, 1);
			exampleDao.find(page, "select e " + "from Example e "
					+ "order by e.lastModify desc");
			return page.getEntities();
		} else {
			return exampleDao.find("select e "
					+ "from Example e, CategoryExampleRelation r "
					+ "where e.id = r.exampleId and r.categoryId = "
					+ categoryId + " order by r.sortFlag");
		}
	}
}
