package com.bstek.dorado.sample;

import info.bliki.wiki.model.WikiModel;

import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.context.Context;
import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.sample.dao.ExampleCategoryDao;
import com.bstek.dorado.sample.entity.ExampleCategory;
import com.bstek.dorado.web.DoradoContext;

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

		HttpServletRequest request = DoradoContext.getAttachedRequest();
		String contextPath = request.getContextPath();
		WikiModel wikiModel = new WikiModel(contextPath, contextPath);
		String summary = StringUtils.defaultIfEmpty(category.getSummary(),
				"''(no description)''");
		String html = wikiModel.render(summary);
		writer.append(html);
	}
}
