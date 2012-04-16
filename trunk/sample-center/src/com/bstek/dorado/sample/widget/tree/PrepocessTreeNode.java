package com.bstek.dorado.sample.widget.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.sample.dao.ExampleCategoryDao;
import com.bstek.dorado.sample.entity.ExampleCategory;

@Component
public class PrepocessTreeNode {
	@Resource
	private ExampleCategoryDao exampleCategoryDao;

	@DataProvider
	public Collection<ExampleCategory> getCategories(Long parentCategoryId)
			throws Exception {
		List<Object[]> rows = exampleCategoryDao
				.find("select c, "
						+ "(select count(*) from ExampleCategory child where child.categoryId = c.id) as childNum "
						+ "from ExampleCategory c " + "where c.categoryId = "
						+ parentCategoryId + " order by c.sortFlag");

		int i = 0;
		List<ExampleCategory> categories = new ArrayList<ExampleCategory>();
		for (Object[] row : rows) {
			ExampleCategory category = EntityUtils.toEntity(row[0]);
			EntityUtils.setValue(category, "hasChild",
					((Long) row[1]).longValue() > 0);
			EntityUtils.setValue(category, "checked", (i++) % 2 == 0);
			categories.add(category);
		}
		return categories;
	}
}
