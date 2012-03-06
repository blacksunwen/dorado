package com.bstek.dorado.view.service;

import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.TypeReference;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.data.JsonUtils;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.provider.Order;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.filter.FilterCriterion;
import com.bstek.dorado.data.provider.filter.FilterCriterionParser;
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
	private FilterCriterionParser filterCriterionParser;

	/**
	 * 设置数据提供器的管理器。
	 */
	public void setDataProviderManager(DataProviderManager dataProviderManager) {
		this.dataProviderManager = dataProviderManager;
	}

	public void setFilterCriterionParser(
			FilterCriterionParser filterCriterionParser) {
		this.filterCriterionParser = filterCriterionParser;
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

	protected Criteria getCriteria(ObjectNode rudeCriteria) throws Exception {
		Criteria criteria = new Criteria();
		if (rudeCriteria.has("criterions")) {
			ArrayNode criterions = (ArrayNode) rudeCriteria.get("criterions");
			if (criterions != null) {
				for (Iterator<JsonNode> it = criterions.iterator(); it
						.hasNext();) {
					ObjectNode rudeCriterion = (ObjectNode) it.next();

					String property = JsonUtils.getString(rudeCriterion,
							"property");
					String expression = JsonUtils.getString(rudeCriterion,
							"expression");
					String dataTypeName = JsonUtils.getString(rudeCriterion,
							"dataType");
					DataType dataType = null;
					if (StringUtils.isNotEmpty(dataTypeName)) {
						dataType = getDataType(dataTypeName);
					}

					FilterCriterion criterion = filterCriterionParser
							.createFilterCriterion(property, dataType,
									expression);
					criteria.AddCriterion(criterion);
				}
			}
		}

		if (rudeCriteria.has("orders")) {
			ArrayNode orders = (ArrayNode) rudeCriteria.get("orders");
			if (orders != null) {
				for (Iterator<JsonNode> it = orders.iterator(); it.hasNext();) {
					ObjectNode rudeCriterion = (ObjectNode) it.next();
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
	protected void doExecute(Writer writer, ObjectNode objectNode,
			DoradoContext context) throws Exception {
		String dataProviderName = JsonUtils.getString(objectNode,
				"dataProvider");
		String resultDataTypeName = null;
		if (objectNode.has("resultDataType")) {
			resultDataTypeName = JsonUtils.getString(objectNode,
					"resultDataType");
		}

		JsonNode rudeParameter = objectNode.get("parameter");
		ObjectNode rudeCriteria = null;
		if (rudeParameter instanceof ObjectNode) {
			rudeCriteria = (ObjectNode) ((ObjectNode) rudeParameter)
					.get("criteria");
			if (rudeCriteria != null) {
				((ObjectNode) rudeParameter).remove("criteria");
			}
		}

		Object parameter = jsonToJavaObject(rudeParameter, null, null, false);
		if (rudeCriteria != null) {
			Criteria criteria = getCriteria(rudeCriteria);
			((Record) parameter).put("criteria", criteria);
		}

		JsonNode rudeSysParameter = objectNode.get("sysParameter");
		Collection<DataPreloadConfig> dataPreloadConfigs = null;
		if (rudeSysParameter instanceof ObjectNode) {
			if (rudeSysParameter.has("preloadConfigs")) {
				dataPreloadConfigs = (Collection<DataPreloadConfig>) JsonUtils
						.toJavaObject(rudeSysParameter.get("preloadConfigs"),
								getDataType("[DataPreloadConfig]"));
			}
		}

		int pageSize = JsonUtils.getInt(objectNode, "pageSize");
		int pageNo = JsonUtils.getInt(objectNode, "pageNo");

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
		boolean supportsEntity = JsonUtils.getBoolean(objectNode,
				"supportsEntity");
		if (supportsEntity) {
			List<String> loadedDataTypes = JsonUtils.get(objectNode,
					"loadedDataTypes", new TypeReference<List<String>>() {
					});
			outputContext.setLoadedDataTypes(loadedDataTypes);
		}
		outputContext.setUsePrettyJson(Configure
				.getBoolean("view.outputPrettyJson"));
		outputContext.setShouldOutputDataTypes(supportsEntity);

		outputResult(result, outputContext);
	}
}
