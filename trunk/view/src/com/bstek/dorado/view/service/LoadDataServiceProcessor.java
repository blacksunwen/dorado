package com.bstek.dorado.view.service;

import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.data.JsonUtils;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Criterion;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.provider.FilterCriterion;
import com.bstek.dorado.data.provider.Order;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.web.DoradoContext;

/**
 * 提供Ajax数据装载服务的处理器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Nov 7, 2008
 */
public class LoadDataServiceProcessor extends DataServiceProcessorSupport {
	private DataProviderManager dataProviderManager;

	/**
	 * 设置数据提供器的管理器。
	 */
	public void setDataProviderManager(DataProviderManager dataProviderManager) {
		this.dataProviderManager = dataProviderManager;
	}

	protected DataProvider getDataProvider(String dataProviderName)
			throws Exception {
		DataProvider dataProvider;
		// 判断是否View中的私有DataProvider
		if (dataProviderName.startsWith(PRIVATE_VIEW_OBJECT_PREFIX)) {
			ParsedDataObjectName parsedName = new ParsedDataObjectName(
					dataProviderName);
			ViewConfig viewConfig = getViewConfig(DoradoContext.getCurrent(),
					parsedName.getViewName());
			dataProvider = viewConfig.getDataProvider(parsedName
					.getDataObject());
		} else {
			dataProvider = dataProviderManager
					.getDataProvider(dataProviderName);
		}
		return dataProvider;
	}

	private void processPreload(Object data,
			DataPreloadConfig dataPreloadConfig, int depth) throws Exception {
		if (data instanceof Collection) {
			for (Object entity : (Collection<?>) data) {
				if (entity != null && EntityUtils.isEntity(entity)) {
					processEntityPreload(entity, dataPreloadConfig, depth);
				}
			}
		} else if (EntityUtils.isEntity(data)) {
			processEntityPreload(data, dataPreloadConfig, depth);
		}
	}

	private void processEntityPreload(Object entity,
			DataPreloadConfig dataPreloadConfig, int depth) throws Exception {
		Object value = EntityUtils.getValue(entity,
				dataPreloadConfig.getProperty());
		if (value != null) {
			if (dataPreloadConfig.getRecursiveLevel() >= depth) {
				processPreload(value, dataPreloadConfig, depth + 1);
			}
			if (dataPreloadConfig.getChildPreloadConfigs() != null) {
				for (DataPreloadConfig childDataPreloadConfig : dataPreloadConfig
						.getChildPreloadConfigs()) {
					processPreload(value, childDataPreloadConfig, 0);
				}
			}
		}
	}

	protected void preload(Object data,
			Collection<DataPreloadConfig> dataPreloadConfigs) throws Exception {
		for (DataPreloadConfig dataPreloadConfig : dataPreloadConfigs) {
			processPreload(data, dataPreloadConfig, 0);
		}
	}

	protected Criteria getCriteria(JSONObject rudeCriteria) {
		Criteria criteria = new Criteria();
		if (rudeCriteria.has("criterions")) {
			JSONArray criterions = JsonUtils.getJSONArray(rudeCriteria,
					"criterions");
			if (criterions != null) {
				for (Iterator<?> it = criterions.iterator(); it.hasNext();) {
					JSONObject rudeCriterion = (JSONObject) it.next();
					Criterion criterion = new FilterCriterion(
							JsonUtils.getString(rudeCriterion, "property"),
							JsonUtils.getString(rudeCriterion, "expression"));
					criteria.AddCriterion(criterion);
				}
			}
		}

		if (rudeCriteria.has("orders")) {
			JSONArray orders = JsonUtils.getJSONArray(rudeCriteria, "orders");
			if (orders != null) {
				for (Iterator<?> it = orders.iterator(); it.hasNext();) {
					JSONObject rudeCriterion = (JSONObject) it.next();
					Order order = new Order(JsonUtils.getString(rudeCriterion,
							"property"), JsonUtils.getBoolean(rudeCriterion,
							"desc"));
					criteria.AddOrder(order);
				}
			}
		}
		return criteria;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void doExecute(Writer writer, JSONObject json,
			DoradoContext context) throws Exception {
		String dataProviderName = json.getString("dataProvider");
		String resultDataTypeName = null;
		if (json.containsKey("resultDataType")) {
			resultDataTypeName = json.getString("resultDataType");
		}

		Object parameter = json.get("parameter");
		if (parameter instanceof JSON) {
			JSONObject rudeCriteria = null;
			if (parameter instanceof JSONObject) {
				rudeCriteria = JsonUtils.getJSONObject((JSONObject) parameter,
						"criteria");
				if (rudeCriteria != null) {
					((JSONObject) parameter).remove("criteria");
				}
			}

			parameter = jsonToJavaObject((JSON) parameter, null, null, false);

			if (rudeCriteria != null) {
				Criteria criteria = getCriteria(rudeCriteria);
				((Record) parameter).put("criteria", criteria);
			}
		}

		Object sysParameter = json.get("sysParameter");
		Collection<DataPreloadConfig> dataPreloadConfigs = null;
		if (sysParameter instanceof JSONObject) {
			JSONObject jsonSysParameter = ((JSONObject) sysParameter);
			if (jsonSysParameter.has("preloadConfigs")) {
				dataPreloadConfigs = (Collection<DataPreloadConfig>) JsonUtils
						.toJavaObject(
								jsonSysParameter.getJSONArray("preloadConfigs"),
								getDataType("[DataPreloadConfig]"));
			}
		}

		int pageSize = JsonUtils.getInt(json, "pageSize");
		int pageNo = JsonUtils.getInt(json, "pageNo");

		DataProvider dataProvider = getDataProvider(dataProviderName);
		if (dataProvider == null) {
			throw new IllegalArgumentException("Unknown DataProvider ["
					+ dataProviderName + "].");
		}

		DataType resultDataType = null;
		if (StringUtils.isNotEmpty(resultDataTypeName)) {
			resultDataType = getDataType(resultDataTypeName);
		}

		Object result;
		if (pageSize > 0) {
			Page page = new Page(pageSize, pageNo);
			dataProvider.getResult(parameter, page, resultDataType);
			result = page;
		} else {
			result = dataProvider.getResult(parameter, resultDataType);
		}

		if (dataPreloadConfigs != null) {
			preload(result, dataPreloadConfigs);
		}

		OutputContext outputContext = new OutputContext(writer);
		boolean supportsEntity = JsonUtils.getBoolean(json, "supportsEntity");
		if (supportsEntity) {
			outputContext.setLoadedDataTypes(JsonUtils.getJSONArray(json,
					"loadedDataTypes"));
		}
		outputContext.setUsePrettyJson(Configure
				.getBoolean("view.outputPrettyJson"));
		outputContext.setShouldOutputDataTypes(supportsEntity);
		outputContext.setShouldOutputEntityState(supportsEntity);

		outputResult(result, outputContext);
	}
}
