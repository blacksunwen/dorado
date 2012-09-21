package com.bstek.dorado.sample.addon;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.entity.FilterType;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.hibernate.HibernateUtils;
import com.bstek.dorado.sample.dao.ExampleDao;
import com.bstek.dorado.sample.entity.Example;
import com.bstek.dorado.web.DoradoContext;

@Component
public class CriteriaBuilder {
	private final static String QUERY_SCHEMAS_KEY = "com.bstek.dorado.sample.addon.CriteriaBuilder.QuerySchemas";

	private final static Map<String, String> QUERY_SCHEMAS = new LinkedHashMap<String, String>();

	static {
		QUERY_SCHEMAS
				.put("新增示例",
						"{criterions:[{property:'new',operator:'=',value:true}],orders:[{property:'createDate',desc:true}]}");
		QUERY_SCHEMAS
				.put("新的或重点的示例",
						"{'criterions':[{'property':'author','operator':'<>','dataType':'String','value':'benny.bao@bstek.com'},{'junction':'or','criterions':[{'property':'new','operator':'=','dataType':'boolean','value':true},{'property':'hot','operator':'=','dataType':'boolean','value':true}]}],'orders':[{'property':'createDate'},{'property':'hot'}]}");
		QUERY_SCHEMAS
				.put("最近一年创建的示例",
						"{'criterions':[{'property':'createDate','operator':'>=','type':'expression','dataType':'Date','value':'${util.calculateDate(\"y-1\")}'}],'orders':[{'property':'createDate'}]}");
		QUERY_SCHEMAS
				.put("最近半年修改过的示例",
						"{'criterions':[{'property':'lastModify','operator':'>=','type':'expression','dataType':'Date','value':'${util.calculateDate(\"M-6\")}'}],'orders':[{'property':'lastModify'}]}");
		QUERY_SCHEMAS
				.put("创建与修改日期不同",
						"{'criterions':[{'property':'createDate','operator':'<>','type':'property','dataType':'Date','value':'lastModify'}],'orders':[{'property':'lastModify'}]}");
	}

	@Resource
	private ExampleDao exampleDao;

	@DataProvider
	public void queryExamples(Page<Example> page, Criteria criteria)
			throws Exception {
		if (criteria != null) {
			DetachedCriteria detachedCriteria = DetachedCriteria
					.forClass(Example.class);
			HibernateUtils.createFilter(detachedCriteria, criteria);
			exampleDao.find(page, detachedCriteria);
		} else {
			exampleDao.getAll(page);
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> doUserQuerySchemas() {
		DoradoContext context = DoradoContext.getCurrent();
		Map<String, String> querySchemas = (Map<String, String>) context
				.getAttribute(DoradoContext.SESSION, QUERY_SCHEMAS_KEY);
		if (querySchemas == null) {
			querySchemas = new LinkedHashMap<String, String>();
			querySchemas.putAll(QUERY_SCHEMAS);
			context.setAttribute(DoradoContext.SESSION, QUERY_SCHEMAS_KEY,
					querySchemas);
		}
		return querySchemas;
	}

	@DataProvider
	public List<Record> getQuerySchemas() throws Exception {
		Map<String, String> querySchemaMap = doUserQuerySchemas();
		List<Record> querySchemas = new ArrayList<Record>();
		querySchemas.add(new Record());
		for (Map.Entry<String, String> entry : querySchemaMap.entrySet()) {
			Record record = new Record();
			record.put("name", entry.getKey());
			record.put("criteria", entry.getValue());
			querySchemas.add(record);
		}
		return querySchemas;
	}

	@DataResolver
	public void saveQuerySchemas(List<Record> querySchemas) {
		Map<String, String> querySchemaMap = doUserQuerySchemas();
		for (Record querySchema : EntityUtils.getIterable(querySchemas,
				FilterType.DELETED, Record.class)) {
			querySchemaMap.remove(querySchema.getString("name"));
		}
		for (Record querySchema : EntityUtils.getIterable(querySchemas,
				FilterType.MODIFIED, Record.class)) {
			querySchemaMap.put(querySchema.getString("name"),
					querySchema.getString("criteria"));
		}
		for (Record querySchema : EntityUtils.getIterable(querySchemas,
				FilterType.NEW, Record.class)) {
			querySchemaMap.put(querySchema.getString("name"),
					querySchema.getString("criteria"));
		}
	}
}
