package com.bstek.dorado.sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.util.Version;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.sample.dao.ExampleCategoryDao;
import com.bstek.dorado.sample.dao.ExampleDao;
import com.bstek.dorado.sample.entity.Example;
import com.bstek.dorado.sample.entity.ExampleCategory;

@Component
public class Main {
	private static final Long QUICK_START_CATEGORY = 37000L;
	private static final Long RECENT_EXAMPLE_CATEGORY = -100L;
	private static final int RECENT_EXAMPLE_COUNT = 15;

	@Resource
	private ExampleCategoryDao exampleCategoryDao;

	@Resource
	private ExampleDao exampleDao;

	private static boolean indexRebuilded = false;

	@DataProvider
	public Collection<ExampleCategory> getCategories(Long parentCategoryId)
			throws Exception {
		List<ExampleCategory> categories = new ArrayList<ExampleCategory>();

		List<Object[]> results = exampleCategoryDao
				.find("select c, "
						+ "(select count(*) from ExampleCategory sc where sc.categoryId=c.id) as subCategoryCount, "
						+ "(select count(e) from Example e, CategoryExampleRelation r where e.id = r.exampleId and r.categoryId=c.id) as subExampleCount "
						+ "from ExampleCategory c where c.categoryId="
						+ parentCategoryId + " order by c.sortFlag");
		for (Object[] result : results) {
			ExampleCategory category = EntityUtils
					.toEntity((ExampleCategory) result[0]);
			int childCount = (int) ((Long) result[1] + (Long) result[2]);
			EntityUtils.setValue(category, "childCount", childCount);
			EntityUtils.setValue(category, "hasChild", childCount > 0);
			categories.add(category);
		}

		if (parentCategoryId == null) {
			ExampleCategory category = new ExampleCategory();
			category = EntityUtils.toEntity(category);
			category.setId(RECENT_EXAMPLE_CATEGORY);
			category.setLabel("最近更新");
			category.setIcon(">images/recent.gif");
			EntityUtils.setValue(category, "childCount", RECENT_EXAMPLE_COUNT);
			EntityUtils.setValue(category, "hasChild", true);
			categories.add(0, category);
		}
		return categories;
	}

	@DataProvider
	public Collection<Example> getExamplesByCategoryId(Long categoryId)
			throws Exception {
		if (RECENT_EXAMPLE_CATEGORY.equals(categoryId)) {
			Page<Example> page = new Page<Example>(RECENT_EXAMPLE_COUNT, 1);
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

	@Expose
	public Long[] getPath(String type, long id) {
		List<Long> path = new ArrayList<Long>();
		Long categoryId = null;

		if ("example".equals(type)) {
			List<Long> categoryIds = exampleDao
					.find("select categoryId from CategoryExampleRelation where exampleId="
							+ id + " and categoryId <> " + QUICK_START_CATEGORY);
			if (!categoryIds.isEmpty()) {
				categoryId = categoryIds.get(0);
			}
		} else if ("category".equals(type)) {
			categoryId = exampleCategoryDao
					.findUnique("select categoryId from ExampleCategory where id="
							+ id);
		}

		while (categoryId != null && categoryId > 0) {
			path.add(categoryId);
			categoryId = exampleCategoryDao
					.findUnique("select categoryId from ExampleCategory where id="
							+ categoryId);
		}

		Long[] pathArray = path.toArray(new Long[0]);
		ArrayUtils.reverse(pathArray);
		return pathArray;
	}

	@SuppressWarnings("unchecked")
	@DataProvider
	public List<SearchResult> search(String searchText) throws Exception {
		FullTextSession fullTextSession = Search.getFullTextSession(exampleDao
				.getSession());

		if (!indexRebuilded) {
			indexRebuilded = true;
			fullTextSession.createIndexer().startAndWait();
		}

		MultiFieldQueryParser parser = new MultiFieldQueryParser(
				Version.LUCENE_31, new String[] { "label", "tags", "url",
						"summary" }, new StandardAnalyzer(Version.LUCENE_31));
		org.apache.lucene.search.Query luceneQuery = parser.parse(searchText);
		FullTextQuery query = fullTextSession.createFullTextQuery(luceneQuery,
				Example.class);
		query.setFirstResult(0);
		query.setMaxResults(100);

		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		for (Example example : (List<Example>) query.list()) {
			SearchResult searchResult = new SearchResult();
			PropertyUtils.copyProperties(searchResult, example);
			searchResults.add(searchResult);
		}
		return searchResults;
	}
}
