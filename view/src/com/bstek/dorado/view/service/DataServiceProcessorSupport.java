package com.bstek.dorado.view.service;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.bstek.dorado.common.StringAliasUtils;
import com.bstek.dorado.data.Constants;
import com.bstek.dorado.data.DataTypeResolver;
import com.bstek.dorado.data.JsonConvertContext;
import com.bstek.dorado.data.JsonUtils;
import com.bstek.dorado.data.config.DataTypeName;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;
import com.bstek.dorado.view.ViewState;
import com.bstek.dorado.view.config.xml.ViewXmlConstants;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.manager.ViewConfigManager;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.Outputter;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.DoradoContextUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since May 13, 2009
 */
public abstract class DataServiceProcessorSupport implements ServiceProcessor,
		DataTypeResolver {

	public static class ParsedDataObjectName {
		private String viewType;
		private String viewName;
		private String dataObject;

		public ParsedDataObjectName(String name) {
			int i = name.indexOf(ViewXmlConstants.PATH_COMPONENT_PREFIX);
			String viewSectionAlias = name.substring(
					PRIVATE_VIEW_OBJECT_PREFIX.length(), i);
			dataObject = name.substring(i + 1);

			String viewSection = StringAliasUtils
					.getOriginalString(viewSectionAlias);
			if (viewSection == null) {
				throw new IllegalArgumentException("Invalid ViewAlias ["
						+ viewSectionAlias + "].");
			}

			i = viewSection.indexOf(ViewXmlConstants.VIEW_NAME_DELIM);
			viewType = viewSection.substring(0, i);
			viewName = viewSection.substring(i + 1);
		}

		public String getViewType() {
			return viewType;
		}

		public String getViewName() {
			return viewName;
		}

		public String getDataObject() {
			return dataObject;
		}
	}

	protected static final String VIEW_STATE_ATTRIBUTE_KEY = ViewState.class
			.getName();
	protected static final String PRIVATE_VIEW_OBJECT_PREFIX = ViewXmlConstants.PATH_VIEW_SHORT_NAME
			+ Constants.PRIVATE_DATA_OBJECT_SUBFIX;

	protected ViewConfigManager viewConfigManager;
	private DataTypeManager dataTypeManager;
	private Outputter dataOutputter;
	private Outputter includeDataTypesOutputter;
	private JsonConvertContext jsonContext;

	public void setDataTypeManager(DataTypeManager dataTypeManager) {
		this.dataTypeManager = dataTypeManager;
	}

	public void setViewConfigManager(ViewConfigManager viewConfigManager) {
		this.viewConfigManager = viewConfigManager;
	}

	public void setDataOutputter(Outputter dataOutputter) {
		this.dataOutputter = dataOutputter;
	}

	public void setIncludeDataTypesOutputter(Outputter includeDataTypesOutputter) {
		this.includeDataTypesOutputter = includeDataTypesOutputter;
	}

	@SuppressWarnings("unchecked")
	public final void execute(Writer writer, JSONObject json,
			DoradoContext context) throws Exception {
		ViewState originViewState = (ViewState) context
				.getAttribute(VIEW_STATE_ATTRIBUTE_KEY);
		context.setAttribute(VIEW_STATE_ATTRIBUTE_KEY, ViewState.servcing);

		DoradoContextUtils.pushNewViewContext(context);

		JSONObject rudeContext = json.getJSONObject("context");
		if (rudeContext != null) {
			for (Map.Entry<String, Object> entry : (Set<Map.Entry<String, Object>>) rudeContext
					.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (value instanceof JSON) {
					value = JsonUtils.toJavaObject((JSON) value, null);
				}
				context.setAttribute(DoradoContext.VIEW, key, value);
			}
		}

		try {
			doExecute(writer, json, context);
		} finally {
			DoradoContextUtils.popViewContext(context);
			context.setAttribute(VIEW_STATE_ATTRIBUTE_KEY, originViewState);
		}
	}

	protected abstract void doExecute(Writer writer, JSONObject json,
			DoradoContext context) throws Exception;

	protected void outputResult(Object result, OutputContext context)
			throws Exception {
		DoradoContext doradoContext = DoradoContext.getCurrent();
		Map<String, Object> viewContext = DoradoContextUtils
				.getViewContext(doradoContext);

		if (context.isShouldOutputDataTypes() || viewContext != null) {
			JsonBuilder jsonBuilder = context.getJsonBuilder();

			jsonBuilder.object();
			jsonBuilder.key("data");
			outputData(result, context);

			if (context.isShouldOutputDataTypes()) {
				jsonBuilder.key("$dataTypeDefinitions");
				boolean hasDataTypes = false;
				Map<String, DataType> includeDataTypes = context
						.getIncludeDataTypes(false);
				if (includeDataTypes != null && !includeDataTypes.isEmpty()) {
					Map<String, DataType> outputDataTypes = new HashMap<String, DataType>();
					for (Map.Entry<String, DataType> entry : includeDataTypes
							.entrySet()) {
						String dataTypeName = entry.getKey();
						if (!context.isDataTypeLoaded(dataTypeName)) {
							outputDataTypes.put(dataTypeName, entry.getValue());
						}
					}
					if (!outputDataTypes.isEmpty()) {
						outputDataTypes(outputDataTypes, context);
						hasDataTypes = true;
					}
				}
				if (!hasDataTypes) {
					jsonBuilder.array().endArray();
				}
				jsonBuilder.endKey();
			}

			if (viewContext != null) {
				jsonBuilder.key("$context").beginValue();
				dataOutputter.output(viewContext, context);
				jsonBuilder.endValue();
			}

			jsonBuilder.endObject();
		} else {
			outputData(result, context);
		}
	}

	protected void outputDataTypes(Map<String, DataType> includeDataTypes,
			OutputContext birchContext) throws Exception {
		includeDataTypesOutputter.output(includeDataTypes, birchContext);
	}

	protected void outputData(Object result, OutputContext birchContext)
			throws Exception {
		dataOutputter.output(result, birchContext);
	}

	protected ViewConfig getViewConfig(DoradoContext context, String viewName)
			throws Exception {
		ViewConfig viewConfig = (ViewConfig) context.getAttribute(viewName);
		if (viewConfig == null) {
			viewConfig = viewConfigManager.getViewConfig(viewName);
			context.setAttribute(viewName, viewConfig);
		}
		return viewConfig;
	}

	public DataType getDataType(String dataTypeName) throws Exception {
		DataType dataType;
		// 判断是否View中的私有DataType
		if (dataTypeName.startsWith(PRIVATE_VIEW_OBJECT_PREFIX)) {
			DoradoContext context = DoradoContext.getCurrent();
			dataType = (DataType) context.getAttribute(dataTypeName);
			if (dataType == null) {
				ParsedDataObjectName parsedName = new ParsedDataObjectName(
						dataTypeName);
				String viewName = parsedName.getViewName();
				ViewConfig viewConfig = getViewConfig(context, viewName);
				String name = parsedName.getDataObject();
				DataTypeName dtn = new DataTypeName(name);
				if (dtn.getSubDataTypes().length == 1) {
					String subName = dtn.getSubDataTypes()[0];
					if (subName.startsWith(PRIVATE_VIEW_OBJECT_PREFIX)) {
						parsedName = new ParsedDataObjectName(subName);
						name = new StringBuffer()
								.append(dtn.getOriginDataType()).append('[')
								.append(parsedName.getDataObject()).append(']')
								.toString();
					}
				}
				dataType = viewConfig.getDataType(name);
				context.setAttribute(dataTypeName, dataType);
			}
		} else {
			dataType = dataTypeManager.getDataType(dataTypeName);
		}
		return dataType;
	}

	protected Object jsonToJavaObject(JSON json, DataType dataType,
			Class<?> targetType, boolean proxy) throws Exception {
		if (jsonContext == null) {
			jsonContext = new JsonConvertContextImpl(false, false, this);
		}
		return JsonUtils.toJavaObject(json, dataType, targetType, proxy,
				jsonContext);

	}

}
