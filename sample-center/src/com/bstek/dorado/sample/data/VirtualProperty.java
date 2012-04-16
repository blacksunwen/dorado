package com.bstek.dorado.sample.data;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.entity.EntityUtils;

@Component
public class VirtualProperty {

	private Collection<MockCategory> generateCategories() {
		MockCategory category;
		Collection<MockCategory> categories = new ArrayList<MockCategory>();

		category = new MockCategory();
		category.setId(1);
		categories.add(category);

		category = new MockCategory();
		category.setId(3);
		categories.add(category);

		category = new MockCategory();
		category.setId(5);
		categories.add(category);

		category = new MockCategory();
		category.setId(6);
		categories.add(category);

		category = new MockCategory();
		category.setId(8);
		categories.add(category);

		return categories;
	}

	@DataProvider
	public Collection<MockCategory> getCategories() throws Exception {
		Collection<MockCategory> categories = generateCategories();
		categories = EntityUtils.toEntity(categories);
		for (MockCategory category : categories) {
			EntityUtils.setValue(category, "categoryName", "CategoryName "
					+ category.getId());
		}
		return categories;
	}
}
