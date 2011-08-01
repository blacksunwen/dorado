package com.bstek.dorado.data;

import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.entity.EnhanceableMapEntityEnhancer;
import com.bstek.dorado.data.entity.EntityWrapper;
import com.bstek.dorado.data.entity.EntityProxyMethodInterceptorFactory;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.type.AggregationDataType;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.util.proxy.ProxyBeanUtils;

/**
 * 用于为Json数据处理提供辅助操作的工具类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Nov 11, 2008
 */
public final class JsonUtils {
	private static final char SYSTEM_PROPERTY_PREFIX = '$';

	/**
	 *
	 */
	public static final String DATATYPE_PROPERTY = "$dataType";

	/**
	 *
	 */
	public static final String STATE_PROPERTY = "$state";

	/**
	 *
	 */
	public static final String ENTITY_ID_PROPERTY = "$entityId";

	/**
	 *
	 */
	public static final String OLD_DATA_PROPERTY = "$oldData";

	private static final int DEFAULT_DATE_PATTERN_LEN = 20;
	private static final Pattern DEFAULT_DATE_PATTERN = Pattern
			.compile("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$");
	private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
			com.bstek.dorado.core.Constants.ISO_DATETIME_FORMAT1);

	private static EntityProxyMethodInterceptorFactory methodInterceptorFactory;

	static {
		String[] dateFormats = new String[] {
				com.bstek.dorado.core.Constants.ISO_DATE_FORMAT,
				com.bstek.dorado.core.Constants.ISO_DATETIME_FORMAT1,
				com.bstek.dorado.core.Constants.ISO_DATETIME_FORMAT2 };
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(dateFormats));
	}

	private JsonUtils() {
	}

	private static EntityProxyMethodInterceptorFactory getMethodInterceptorFactory()
			throws Exception {
		if (methodInterceptorFactory == null) {
			Context context = Context.getCurrent();
			methodInterceptorFactory = (EntityProxyMethodInterceptorFactory) context
					.getServiceBean("entityProxyMethodInterceptorFactory");
		}
		return methodInterceptorFactory;
	}

	/**
	 * 返回Json对象中某个属性的String值，如果该属性不存在则返回null。
	 */
	public static String getString(JSONObject json, String key) {
		return getString(json, key, null);
	}

	/**
	 * 返回Json对象中某个属性的String值，如果该属性不存在则返回defaultValue参数指定的默认值。
	 */
	public static String getString(JSONObject json, String key,
			String defaultValue) {
		if (json.get(key) != null) {
			return json.getString(key);
		} else {
			return defaultValue;
		}
	}

	/**
	 * 返回Json对象中某个属性的boolean值，如果该属性不存在或转换失败则返回false。
	 */
	public static boolean getBoolean(JSONObject json, String key) {
		return getBoolean(json, key, false);
	}

	/**
	 * 返回Json对象中某个属性的boolean值，如果该属性不存在在或转换失败则返回defaultValue参数指定的默认值。
	 */
	public static boolean getBoolean(JSONObject json, String key,
			boolean defaultValue) {
		try {
			return json.getBoolean(key);
		} catch (JSONException e) {
			return defaultValue;
		}
	}

	/**
	 * 返回Json对象中某个属性的int值，如果该属性不存在或转换失败则返回0。
	 */
	public static int getInt(JSONObject json, String key) {
		return getInt(json, key, 0);
	}

	/**
	 * 返回Json对象中某个属性的int值，如果该属性不存在在或转换失败则返回defaultValue参数指定的默认值。
	 */
	public static int getInt(JSONObject json, String key, int defaultValue) {
		try {
			return json.getInt(key);
		} catch (JSONException e) {
			return defaultValue;
		}
	}

	/**
	 * 返回Json对象中某个属性的long值，如果该属性不存在或转换失败则返回0。
	 */
	public static long getLong(JSONObject json, String key) {
		return getLong(json, key, 0);
	}

	/**
	 * 返回Json对象中某个属性的long值，如果该属性不存在在或转换失败则返回defaultValue参数指定的默认值。
	 */
	public static long getLong(JSONObject json, String key, long defaultValue) {
		try {
			return json.getLong(key);
		} catch (JSONException e) {
			return defaultValue;
		}
	}

	/**
	 * 返回Json对象中某个属性的double值，如果该属性不存在或转换失败则返回0。
	 */
	public static double getDouble(JSONObject json, String key) {
		return getDouble(json, key, 0);
	}

	/**
	 * 返回Json对象中某个属性的double值，如果该属性不存在在或转换失败则返回defaultValue参数指定的默认值。
	 */
	public static double getDouble(JSONObject json, String key,
			double defaultValue) {
		try {
			return json.getDouble(key);
		} catch (JSONException e) {
			return defaultValue;
		}
	}

	/**
	 * 返回Json对象中某个属性的JSONObject值，如果该属性不存在或转换失败则返回null。
	 */
	public static JSONObject getJSONObject(JSONObject json, String key) {
		try {
			JSONObject result = json.getJSONObject(key);
			return (result != null && result.isNullObject()) ? null : result;
		} catch (JSONException e) {
			return null;
		}
	}

	/**
	 * 返回Json对象中某个属性的JSONArray值，如果该属性不存在或转换失败则返回null。
	 */
	public static JSONArray getJSONArray(JSONObject json, String key) {
		try {
			return json.getJSONArray(key);
		} catch (JSONException e) {
			return null;
		}
	}

	private static DataTypeManager dataTypeManager;

	private static DataTypeManager getDataTypeManager() throws Exception {
		if (dataTypeManager == null) {
			Context context = Context.getCurrent();
			dataTypeManager = (DataTypeManager) context
					.getServiceBean("dataTypeManager");
		}
		return dataTypeManager;
	}

	private static DataType getDataType(String dataTypeName,
			JsonConvertContext context) throws Exception {
		DataTypeResolver dataTypeResolver = (context != null) ? dataTypeResolver = context
				.getDataTypeResolver() : null;
		if (dataTypeResolver != null) {
			return dataTypeResolver.getDataType(dataTypeName);
		} else {
			return getDataTypeManager().getDataType(dataTypeName);
		}
	}

	@SuppressWarnings("unchecked")
	private static Object internalToJavaObject(JSONObject json,
			EntityDataType dataType, Class<?> targetType, boolean proxy,
			JsonConvertContext context) throws Exception {
		if (json.isNullObject())
			return null;

		if (dataType == null && json.has(DATATYPE_PROPERTY)) {
			String dataTypeName = json.getString(DATATYPE_PROPERTY);
			if (StringUtils.isNotEmpty(dataTypeName)) {
				dataType = (EntityDataType) getDataType(dataTypeName, context);
			}
		}

		Class<?> creationType = null;
		if (dataType != null) {
			creationType = dataType.getCreationType();
		}

		Object result;
		if (creationType != null) {
			MethodInterceptor[] mis = getMethodInterceptorFactory()
					.createInterceptors(dataType, creationType, null);
			result = ProxyBeanUtils.createBean(creationType, mis);
		} else {
			Record record = new Record();
			record.setEntityEnhancer(new EnhanceableMapEntityEnhancer(dataType));
			result = record;
		}

		EntityWrapper entity = EntityWrapper.create(result);
		if (proxy) {
			entity.setStateLocked(true);
		}

		for (Map.Entry<String, Object> entry : (Set<Map.Entry<String, Object>>) json
				.entrySet()) {
			String property = entry.getKey();
			if (property.charAt(0) == SYSTEM_PROPERTY_PREFIX) {
				continue;
			}

			Object value = entry.getValue();
			if (value != null) {
				Class<?> type = null;
				type = entity.getPropertyType(property);

				if (value instanceof JSON) {
					PropertyDef propertyDef = (dataType != null) ? dataType
							.getPropertyDef(property) : null;
					value = toJavaObject((JSON) value,
							(propertyDef != null) ? propertyDef.getDataType()
									: null, type, proxy, context);
				}

				if (type != null && !type.isInstance(value)) {
					DataType propertyDataType = getDataTypeManager()
							.getDataType(type);
					if (propertyDataType != null) {
						value = propertyDataType.fromObject(value);
					}
				} else if (value instanceof String) {// 处理日期字符串
					String str = (String) value;
					if (str.length() == DEFAULT_DATE_PATTERN_LEN
							&& DEFAULT_DATE_PATTERN.matcher(str).matches()) {
						value = DEFAULT_DATE_FORMAT.parse(str);
					}
				}
			}
			entity.set(property, value);
		}

		if (proxy) {
			entity.setStateLocked(false);
			if (json.has(STATE_PROPERTY)) {
				int state = json.getInt(STATE_PROPERTY);
				if (state > 0) {
					entity.setState(EntityState.fromInt(state));
				}
			}
			if (json.has(ENTITY_ID_PROPERTY)) {
				int entityId = json.getInt(ENTITY_ID_PROPERTY);
				if (entityId > 0) {
					entity.setEntityId(entityId);
				}
			}
			if (json.has(OLD_DATA_PROPERTY)) {
				JSONObject jsonOldValues = json
						.getJSONObject(OLD_DATA_PROPERTY);
				if (jsonOldValues != null) {
					Map<String, Object> oldValues = entity.getOldValues(true);
					oldValues.putAll(jsonOldValues);
				}
			}
		}

		if (targetType != null && !targetType.isInstance(result)) {
			throw new IllegalArgumentException("Java type mismatch. expect ["
					+ targetType + "].");
		}
		if (context != null && context.getEntityCollection() != null) {
			context.getEntityCollection().add(result);
		}
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Collection<?> internalToJavaCollection(JSONArray array,
			AggregationDataType dataType, Class<Collection<?>> targetType,
			boolean proxy, JsonConvertContext context) throws Exception {
		Class<Collection> creationType = null;
		if (dataType != null) {
			creationType = (Class<Collection>) dataType.getCreationType();
		}

		Collection collection = null;
		if (creationType != null) {
			if (!Collection.class.isAssignableFrom(creationType)) {
				throw new IllegalArgumentException(
						"Java type mismatch. expect [java.util.Collection].");
			}
			if (targetType != null
					&& !targetType.isAssignableFrom(creationType)) {
				throw new IllegalArgumentException(
						"Java type mismatch. expect [" + targetType + "].");
			}
			if (!Modifier.isAbstract(creationType.getModifiers())) {
				collection = creationType.newInstance();
			}
		} else if (targetType != null) {
			if (!Modifier.isAbstract(targetType.getModifiers())) {
				collection = targetType.newInstance();
			} else if (Set.class.isAssignableFrom(targetType)) {
				collection = new HashSet<Object>();
			}
		}
		if (collection == null)
			collection = new ArrayList<Object>();

		boolean isFirstElement = true;
		EntityDataType elementDataType = (EntityDataType) ((dataType != null) ? dataType
				.getElementDataType() : null);
		for (Object element : array) {
			if (element instanceof JSONObject) {
				JSONObject json = (JSONObject) element;
				if (isFirstElement && elementDataType == null
						&& json.has(DATATYPE_PROPERTY)) {
					String dataTypeName = json.getString(DATATYPE_PROPERTY);
					if (StringUtils.isNotEmpty(dataTypeName)) {
						elementDataType = (EntityDataType) getDataType(
								dataTypeName, context);
					}
				}

				collection.add(internalToJavaObject(json, elementDataType,
						null, proxy, context));
			} else {
				collection.add((elementDataType != null) ? elementDataType
						.fromObject(element) : element);
			}
			isFirstElement = false;
		}
		if (context != null && context.getEntityListCollection() != null) {
			context.getEntityListCollection().add(collection);
		}
		return collection;
	}

	/**
	 * 尝试将一个JSON数据对象转换成Java POJO对象。<br>
	 * 如果在转换的同时还指定了目标数据类型，那么此过程会尝试把JSON数据转换成与目标数据类型相匹配的Java POJO对象。
	 * 
	 * @param json
	 *            要转换的JSON数据。
	 * @param dataType
	 *            目标数据类型。
	 * @return 转换得到的Java POJO对象。
	 * @throws Exception
	 */
	public static Object toJavaObject(JSON json, DataType dataType)
			throws Exception {
		return toJavaObject(json, dataType, false);
	}

	/**
	 * 尝试将一个JSON数据对象转换成Java POJO对象。<br>
	 * 如果在转换的同时还指定了目标数据类型，那么此过程会尝试把JSON数据转换成与目标数据类型相匹配的Java POJO对象。
	 * 
	 * @param json
	 *            要转换的JSON数据。
	 * @param dataType
	 *            目标数据类型。
	 * @param targetType
	 *            目标数据类型。
	 * @return 转换得到的Java POJO对象。
	 * @throws Exception
	 */
	public static Object toJavaObject(JSON json, DataType dataType,
			boolean proxy) throws Exception {
		return toJavaObject(json, dataType, null, proxy, null);
	}

	/**
	 * 尝试将一个JSON数据对象转换成Java POJO对象。<br>
	 * 如果在转换的同时还指定了目标数据类型，那么此过程会尝试把JSON数据转换成与目标数据类型相匹配的Java POJO对象。
	 * 
	 * @param json
	 *            要转换的JSON数据。
	 * @param dataType
	 *            目标数据类型。
	 * @param targetType
	 *            目标数据类型。
	 * @param proxy
	 *            是否对转换得到的Java POJO对象进行动态代理。
	 * @param context
	 *            上下文。
	 * @return 转换得到的Java POJO对象。
	 * @throws Exception
	 */
	public static Object toJavaObject(JSON json, DataType dataType,
			Class<?> targetType, boolean proxy, JsonConvertContext context)
			throws Exception {
		if (json == null || json instanceof JSONNull)
			return null;

		if (dataType != null) {
			if (dataType instanceof EntityDataType) {
				if (json instanceof JSONObject) {
					return internalToJavaObject((JSONObject) json,
							(EntityDataType) dataType, null, proxy, context);
				} else {
					throw new IllegalArgumentException(
							"Value type mismatch. expect [JSONObject].");
				}
			} else if (dataType instanceof AggregationDataType) {
				if (json instanceof JSONArray) {
					return internalToJavaCollection((JSONArray) json,
							(AggregationDataType) dataType, null, proxy,
							context);
				} else {
					throw new IllegalArgumentException(
							"Value type mismatch. expect [JSONArray].");
				}
			} else {
				throw new IllegalArgumentException(
						"Type mismatch. error occured during convert [JSON] to ["
								+ dataType.getName() + "].");
			}
		} else {
			if (json instanceof JSONObject) {
				return internalToJavaObject((JSONObject) json, null, null,
						proxy, context);
			} else {
				return internalToJavaCollection((JSONArray) json, null, null,
						proxy, context);
			}
		}
	}
}
