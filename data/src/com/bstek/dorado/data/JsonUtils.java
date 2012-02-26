package com.bstek.dorado.data;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.BooleanNode;
import org.codehaus.jackson.node.ContainerNode;
import org.codehaus.jackson.node.NumericNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TextNode;
import org.codehaus.jackson.node.ValueNode;
import org.codehaus.jackson.type.TypeReference;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.entity.EnhanceableMapEntityEnhancer;
import com.bstek.dorado.data.entity.EntityProxyMethodInterceptorFactory;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityWrapper;
import com.bstek.dorado.data.type.AggregationDataType;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.data.variant.VariantUtils;
import com.bstek.dorado.util.proxy.ProxyBeanUtils;

/**
 * 用于为Json数据处理提供辅助操作的工具类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Nov 11, 2008
 */
public final class JsonUtils {
	public static final String DATATYPE_PROPERTY = "$dataType";

	public static final String STATE_PROPERTY = "$state";

	public static final String ENTITY_ID_PROPERTY = "$entityId";

	public static final String OLD_DATA_PROPERTY = "$oldData";

	private static final char SYSTEM_PROPERTY_PREFIX = '$';
	private static final int DEFAULT_DATE_PATTERN_LEN = 20;
	private static final Pattern DEFAULT_DATE_PATTERN = Pattern
			.compile("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$");
	private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
			com.bstek.dorado.core.Constants.ISO_DATETIME_FORMAT1);

	private static EntityProxyMethodInterceptorFactory methodInterceptorFactory;
	private static DataTypeManager dataTypeManager;
	private static ObjectMapper objectMapper;

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

	private static DataTypeManager getDataTypeManager() throws Exception {
		if (dataTypeManager == null) {
			Context context = Context.getCurrent();
			dataTypeManager = (DataTypeManager) context
					.getServiceBean("dataTypeManager");
		}
		return dataTypeManager;
	}

	public static ObjectMapper getObjectMapper() {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		return objectMapper;
	}

	public static String getString(ObjectNode objectNode, String property,
			String defaultValue) {
		JsonNode propertyNode = objectNode.get(property);
		return (propertyNode != null) ? propertyNode.asText() : defaultValue;
	}

	public static String getString(ObjectNode objectNode, String property) {
		return getString(objectNode, property, null);
	}

	public static boolean getBoolean(ObjectNode objectNode, String property,
			boolean defaultValue) {
		JsonNode propertyNode = objectNode.get(property);
		return (propertyNode != null) ? propertyNode.asBoolean() : defaultValue;
	}

	public static boolean getBoolean(ObjectNode objectNode, String property) {
		return getBoolean(objectNode, property, false);
	}

	public static int getInt(ObjectNode objectNode, String property,
			int defaultValue) {
		JsonNode propertyNode = objectNode.get(property);
		return (propertyNode != null) ? propertyNode.asInt() : defaultValue;
	}

	public static int getInt(ObjectNode objectNode, String property) {
		return getInt(objectNode, property, 0);
	}

	public static long getLong(ObjectNode objectNode, String property,
			long defaultValue) {
		JsonNode propertyNode = objectNode.get(property);
		return (propertyNode != null) ? propertyNode.asLong() : defaultValue;
	}

	public static long getLong(ObjectNode objectNode, String property) {
		return getLong(objectNode, property, 0);
	}

	public static float getFloat(ObjectNode objectNode, String property,
			float defaultValue) {
		JsonNode propertyNode = objectNode.get(property);
		return (propertyNode != null) ? ((float) propertyNode.asDouble())
				: defaultValue;
	}

	public static float getFloat(ObjectNode objectNode, String property) {
		return getFloat(objectNode, property, 0);
	}

	public static double getDouble(ObjectNode objectNode, String property,
			double defaultValue) {
		JsonNode propertyNode = objectNode.get(property);
		return (propertyNode != null) ? propertyNode.asDouble() : defaultValue;
	}

	public static double getDouble(ObjectNode objectNode, String property) {
		return getDouble(objectNode, property, 0);
	}

	public static Date getDate(ObjectNode objectNode, String property,
			Date defaultValue) {
		JsonNode propertyNode = objectNode.get(property);
		return (propertyNode != null) ? VariantUtils.toDate(propertyNode
				.asText()) : defaultValue;
	}

	public static Date getDate(ObjectNode objectNode, String property) {
		return getDate(objectNode, property, null);
	}

	public static <T> T get(ObjectNode objectNode, String property,
			Class<T> classType) throws JsonParseException,
			JsonMappingException, IOException {
		JsonNode propertyNode = objectNode.get(property);
		return (propertyNode != null) ? getObjectMapper().readValue(
				propertyNode, classType) : null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T get(ObjectNode objectNode, String property,
			TypeReference valueTypeRef) throws JsonParseException,
			JsonMappingException, IOException {
		JsonNode propertyNode = objectNode.get(property);
		return (T) ((propertyNode != null) ? getObjectMapper().readValue(
				propertyNode, valueTypeRef) : null);
	}

	/**
	 * Produce a string in double quotes with backslash sequences in all the
	 * right places. A backslash will be inserted within </, allowing JSON text
	 * to be delivered in HTML. In JSON text, a string cannot contain a control
	 * character or an unescaped quote or backslash.<br>
	 * <strong>CAUTION:</strong> if <code>string</code> represents a javascript
	 * function, translation of characters will not take place. This will
	 * produce a non-conformant JSON text.
	 * 
	 * @param string
	 *            A String
	 * @return A String correctly formatted for insertion in a JSON text.
	 */
	public static String quote(String string) {
		if (string == null || string.length() == 0) {
			return "\"\"";
		}

		char b;
		char c = 0;
		int i;
		int len = string.length();
		StringBuffer sb = new StringBuffer(len * 2);
		String t;
		char[] chars = string.toCharArray();
		char[] buffer = new char[1030];
		int bufferIndex = 0;
		sb.append('"');
		for (i = 0; i < len; i += 1) {
			if (bufferIndex > 1024) {
				sb.append(buffer, 0, bufferIndex);
				bufferIndex = 0;
			}
			b = c;
			c = chars[i];
			switch (c) {
			case '\\':
			case '"':
				buffer[bufferIndex++] = '\\';
				buffer[bufferIndex++] = c;
				break;
			case '/':
				if (b == '<') {
					buffer[bufferIndex++] = '\\';
				}
				buffer[bufferIndex++] = c;
				break;
			case '%':
				buffer[bufferIndex++] = '\\';
				buffer[bufferIndex++] = 'u';
				buffer[bufferIndex++] = '0';
				buffer[bufferIndex++] = '0';
				buffer[bufferIndex++] = '2';
				buffer[bufferIndex++] = '5';
				break;
			default:
				if (c < ' ') {
					switch (c) {
					case '\b':
						buffer[bufferIndex++] = '\\';
						buffer[bufferIndex++] = 'b';
						break;
					case '\t':
						buffer[bufferIndex++] = '\\';
						buffer[bufferIndex++] = 't';
						break;
					case '\n':
						buffer[bufferIndex++] = '\\';
						buffer[bufferIndex++] = 'n';
						break;
					case '\f':
						buffer[bufferIndex++] = '\\';
						buffer[bufferIndex++] = 'f';
						break;
					case '\r':
						buffer[bufferIndex++] = '\\';
						buffer[bufferIndex++] = 'r';
						break;
					default:
						t = "000" + Integer.toHexString(c);
						int tLength = t.length();
						buffer[bufferIndex++] = '\\';
						buffer[bufferIndex++] = 'u';
						buffer[bufferIndex++] = t.charAt(tLength - 4);
						buffer[bufferIndex++] = t.charAt(tLength - 3);
						buffer[bufferIndex++] = t.charAt(tLength - 2);
						buffer[bufferIndex++] = t.charAt(tLength - 1);
					}
				} else {
					buffer[bufferIndex++] = c;
				}
			}
		}
		sb.append(buffer, 0, bufferIndex);
		sb.append('"');
		return sb.toString();
	}

	/**
	 * Make a JSON text of an Object value. If the object has an
	 * value.toJSONString() method, then that method will be used to produce the
	 * JSON text. The method is required to produce a strictly conforming text.
	 * If the object does not contain a toJSONString method (which is the most
	 * common case), then a text will be produced by the rules.
	 * <p>
	 * Warning: This method assumes that the data structure is acyclical.
	 * 
	 * @param value
	 *            The value to be serialized.
	 * @return a printable, displayable, transmittable representation of the
	 *         object, beginning with <code>{</code>&nbsp;<small>(left
	 *         brace)</small> and ending with <code>}</code>&nbsp;<small>(right
	 *         brace)</small>.
	 * @throws JSONException
	 *             If the value is or contains an invalid number.
	 */
	public static String valueToString(Object value) {
		if (value == null) {
			return "null";
		}

		if (value instanceof Number || value instanceof Boolean) {
			if (value instanceof Float && (Float.isNaN((Float) value))) {
				return "undefined";
			}
			if (value instanceof Double && (Double.isNaN((Double) value))) {
				return "undefined";
			}
			return value.toString();
		}
		return quote(value.toString());
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

	private static Object toJavaValue(ValueNode valueNode, DataType dataType,
			JsonConvertContext context) {
		Object value = null;
		if (valueNode != null) {
			if (valueNode instanceof TextNode) {
				value = ((TextNode) valueNode).asText();
			} else if (valueNode instanceof NumericNode) {
				value = ((NumericNode) valueNode).getNumberValue();
			} else if (valueNode instanceof BooleanNode) {
				value = ((BooleanNode) valueNode).asBoolean();
			} else {
				value = valueNode.asText();
			}
		}

		if (dataType != null) {
			value = dataType.fromObject(value);
		}
		return value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object internalToJavaEntity(ObjectNode objectNode,
			EntityDataType dataType, Class<?> targetType, boolean proxy,
			JsonConvertContext context) throws Exception {
		if (objectNode == null || objectNode.isNull()) {
			return null;
		}

		if (dataType == null && objectNode.has(DATATYPE_PROPERTY)) {
			String dataTypeName = getString(objectNode, DATATYPE_PROPERTY);
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

		Iterator<Entry<String, JsonNode>> fields = objectNode.getFields();
		while (fields.hasNext()) {
			Entry<String, JsonNode> field = fields.next();
			String property = field.getKey();
			if (property.charAt(0) == SYSTEM_PROPERTY_PREFIX) {
				continue;
			}

			Object value = null;
			JsonNode jsonNode = field.getValue();
			if (jsonNode != null) {
				Class<?> type = null;
				type = entity.getPropertyType(property);

				if (jsonNode instanceof ContainerNode) {
					PropertyDef propertyDef = (dataType != null) ? dataType
							.getPropertyDef(property) : null;
					value = toJavaObject((ContainerNode) jsonNode,
							(propertyDef != null) ? propertyDef.getDataType()
									: null, type, proxy, context);
				} else if (jsonNode instanceof ValueNode) {
					value = toJavaValue((ValueNode) jsonNode, null, null);
				} else {
					throw new IllegalArgumentException(
							"Value type mismatch. expect [JSON Value].");
				}

				if (type != null) {
					if (!type.isInstance(value)) {
						if (value instanceof String && type.isEnum()) {
							value = Enum.valueOf((Class<? extends Enum>) type,
									(String) value);
						} else {
							DataType propertyDataType = getDataTypeManager()
									.getDataType(type);
							if (propertyDataType != null) {
								value = propertyDataType.fromObject(value);
							}
						}
					}
				} else {
					if (value instanceof String) { // 处理日期字符串
						String str = (String) value;
						if (str.length() == DEFAULT_DATE_PATTERN_LEN
								&& DEFAULT_DATE_PATTERN.matcher(str).matches()) {
							value = DEFAULT_DATE_FORMAT.parse(str);
						}
					}
				}
			}
			entity.set(property, value);
		}

		if (proxy) {
			entity.setStateLocked(false);
			if (objectNode.has(STATE_PROPERTY)) {
				int state = JsonUtils.getInt(objectNode, STATE_PROPERTY);
				if (state > 0) {
					entity.setState(EntityState.fromInt(state));
				}
			}
			if (objectNode.has(ENTITY_ID_PROPERTY)) {
				int entityId = JsonUtils.getInt(objectNode, ENTITY_ID_PROPERTY);
				if (entityId > 0) {
					entity.setEntityId(entityId);
				}
			}
			if (objectNode.has(OLD_DATA_PROPERTY)) {
				ObjectNode jsonOldValues = (ObjectNode) objectNode
						.get(OLD_DATA_PROPERTY);
				if (jsonOldValues != null) {
					Map<String, Object> oldValues = entity.getOldValues(true);
					Iterator<Entry<String, JsonNode>> oldFields = jsonOldValues
							.getFields();
					while (oldFields.hasNext()) {
						Entry<String, JsonNode> entry = oldFields.next();
						String oldKey = entry.getKey();
						oldValues.put(
								oldKey,
								toJavaValue((ValueNode) entry.getValue(), null,
										null));
					}
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
	private static Collection<?> internalToJavaCollection(ArrayNode arrayNode,
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
		Iterator<JsonNode> it = arrayNode.iterator();
		while (it.hasNext()) {
			JsonNode jsonNode = it.next();
			if (jsonNode instanceof ObjectNode) {
				ObjectNode objectNode = (ObjectNode) jsonNode;
				if (isFirstElement && elementDataType == null
						&& objectNode.has(DATATYPE_PROPERTY)) {
					String dataTypeName = JsonUtils.getString(objectNode,
							DATATYPE_PROPERTY);
					if (StringUtils.isNotEmpty(dataTypeName)) {
						elementDataType = (EntityDataType) getDataType(
								dataTypeName, context);
					}
				}
				collection.add(internalToJavaEntity(objectNode,
						elementDataType, null, proxy, context));
			} else if (jsonNode instanceof ValueNode) {
				collection.add(toJavaValue(((ValueNode) jsonNode),
						elementDataType, context));
			} else {
				throw new IllegalArgumentException(
						"Value type mismatch. expect [JSON Value].");
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
	 * @param jsonNode
	 *            要转换的JSON数据。
	 * @param dataType
	 *            目标数据类型。
	 * @return 转换得到的Java POJO对象。
	 * @throws Exception
	 */
	public static Object toJavaObject(JsonNode jsonNode, DataType dataType)
			throws Exception {
		return toJavaObject(jsonNode, dataType, false);
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
	public static Object toJavaObject(JsonNode jsonNode, DataType dataType,
			boolean proxy) throws Exception {
		return toJavaObject(jsonNode, dataType, null, proxy, null);
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
	public static Object toJavaObject(JsonNode jsonNode, DataType dataType,
			Class<?> targetType, boolean proxy, JsonConvertContext context)
			throws Exception {
		if (jsonNode == null || jsonNode.isNull()) {
			return null;
		}

		if (dataType != null) {
			if (dataType instanceof EntityDataType) {
				if (jsonNode instanceof ObjectNode) {
					return internalToJavaEntity((ObjectNode) jsonNode,
							(EntityDataType) dataType, null, proxy, context);
				} else {
					throw new IllegalArgumentException(
							"Value type mismatch. expect [JSON Object].");
				}
			} else if (dataType instanceof AggregationDataType) {
				if (jsonNode instanceof ArrayNode) {
					return internalToJavaCollection((ArrayNode) jsonNode,
							(AggregationDataType) dataType, null, proxy,
							context);
				} else {
					throw new IllegalArgumentException(
							"Value type mismatch. expect [JSON Array].");
				}
			} else if (jsonNode instanceof ValueNode) {
				return toJavaValue((ValueNode) jsonNode, dataType, context);
			} else {
				throw new IllegalArgumentException(
						"Value type mismatch. expect [JSON Value].");
			}
		} else {
			if (jsonNode instanceof ObjectNode) {
				return internalToJavaEntity((ObjectNode) jsonNode, null, null,
						proxy, context);
			} else if (jsonNode instanceof ArrayNode) {
				return internalToJavaCollection((ArrayNode) jsonNode, null,
						null, proxy, context);
			} else {
				return toJavaValue((ValueNode) jsonNode, null, context);
			}
		}
	}
}
