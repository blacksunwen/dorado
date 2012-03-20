package com.bstek.dorado.sample;

import java.io.PrintWriter;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.context.Context;
import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.sample.dao.ExampleCategoryDao;
import com.bstek.dorado.sample.entity.ExampleCategory;

@Component
public class CategorySummary {

	@Resource
	private ExampleCategoryDao exampleCategoryDao;

	@DataProvider
	public ExampleCategory getCategory(long id) {
		return exampleCategoryDao.get(id);
	}

	public void outputContent(PrintWriter writer, Context context)
			throws Exception {
		ExampleCategory category = (ExampleCategory) context.get("category");

		String summary = StringUtils.defaultIfEmpty(category.getSummary(),
				"''(no description)''");
		String html = BikiUtils.render(summary);
		writer.append(html);
	}
}
