package com.bstek.dorado.sample.service;

import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.bstek.dorado.sample.dao.ExampleCategoryDao;
import com.bstek.dorado.sample.dao.ExampleDao;
import com.bstek.dorado.sample.entity.Example;
import com.bstek.dorado.sample.entity.ExampleCategory;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.web.resolver.AbstractTextualResolver;
import com.bstek.dorado.web.resolver.HttpConstants;

@Component
public class ExampleListResolver extends AbstractTextualResolver {
	@Resource
	private ExampleCategoryDao exampleCategoryDao;

	@Resource
	private ExampleDao exampleDao;

	public ExampleListResolver() {
		setContentType(HttpConstants.CONTENT_TYPE_JAVASCRIPT);
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PrintWriter writer = getWriter(request, response);
		try {
			JsonBuilder jsonBuilder = new JsonBuilder(writer);
			jsonBuilder.setPrettyFormat(true);
			jsonBuilder.array();

			for (Example example : exampleDao.getAll()) {
				jsonBuilder.object();
				jsonBuilder.key("type").value("example");
				jsonBuilder.key("id").value(example.getId());
				jsonBuilder.key("label").value(example.getLabel());
				jsonBuilder.key("icon").value(example.getIcon());
				jsonBuilder.key("isNew").value(example.isNew());
				jsonBuilder.endObject();
			}

			for (ExampleCategory category : exampleCategoryDao.getAll()) {
				jsonBuilder.object();
				jsonBuilder.key("type").value("category");
				jsonBuilder.key("id").value("category" + category.getId());
				jsonBuilder.key("label").value(category.getLabel());
				jsonBuilder.key("icon").value(category.getIcon());
				jsonBuilder.key("isNew").value(category.isNew());
				jsonBuilder.endObject();
			}

			jsonBuilder.endArray();
		} finally {
			writer.flush();
			writer.close();
		}
	}

}
