package com.bstek.dorado.data.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.beans.BeanMap;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.el.ExpressionHandler;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.BasePropertyDef;
import com.bstek.dorado.data.type.property.CacheMode;
import com.bstek.dorado.data.type.property.IndexedLookupData;
import com.bstek.dorado.data.type.property.LazyPropertyDef;
import com.bstek.dorado.data.type.property.Lookup;
import com.bstek.dorado.data.type.property.LookupConstraint;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.data.type.property.Reference;
import com.bstek.dorado.data.type.validator.Validator;

/*
 * 关于PropertyPath的备忘： 1.
 * PropertyPath只对BasePropertyDef有效，且只对isKey=false的BasePropertyDef有效。 2.
 * PropertyPath的计算结果不会被缓存，但是系统会确保其结果在返回时的DataType与PropertyDef中的定义一致。 3.
 * PropertyPath的第一段不能与所属的PropertyDef.name重名，每一片段的返回结果都不能与当前数据实体相同。
 */

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-19
 */
public abstract class EntityEnhancer {
	private static final Log logger = LogFactory.getLog(EntityEnhancer.class);
	private static final String THIS = "this";

	protected static final Object UNDISPOSED_VALUE = new Object();

	private static ExpressionHandler expressionHandler;
	private static Ehcache lookupIndexedDataCache;

	private static int maxEntityId = 0;
	private static long maxTimeStamp = 0;
	private static GetterInterceptionInjector injector = new GetterInterceptionInjector();

	private Set<String> propertiesHasRead;

	protected EntityDataType dataType;
	private int entityId;
	private long timeStamp;
	private EntityState state = EntityState.NONE;
	private boolean stateLocked = false;
	private Map<String, Object> exProperties;
	private Map<String, Object> oldValues;

	public EntityEnhancer(EntityDataType dataType) {
		setDataType(dataType);
	}

	public void setDataType(EntityDataType dataType) {
		this.dataType = dataType;
	}

	public EntityDataType getDataType() {
		return dataType;
	}

	private static ExpressionHandler getExpressionHandler() throws Exception {
		if (expressionHandler == null) {
			Context context = Context.getCurrent();
			expressionHandler = (ExpressionHandler) context
					.getServiceBean("expressionHandler");
		}
		return expressionHandler;
	}

	/**
	 * 禁用dorado对数据实体中属性的read方法的动态代理。<br>
	 * 注意，此方法并不禁止对EL表达式进行求值的动态代理。
	 */
	public static void disableGetterInterception() {
		injector.disableGetterInterception();
	}

	/**
	 * 启用dorado对数据实体中属性的read方法的动态代理。
	 */
	public static void enableGetterInterception() {
		injector.resetHasPropertyResultSkiped();
		injector.enableGetterInterception();
	}

	/**
	 * 是否禁用了数据实体中属性的read方法中的部分动态代理。
	 */
	public static boolean isGetterInterceptionDisabled() {
		return injector.isGetterInterceptionDisabled();
	}

	/**
	 * 返回最后一次读取实体对象属性时是否发生了属性方法拦截器被跳过的现象。
	 */
	public static boolean hasGetterResultSkiped() {
		return injector.hasPropertyResultSkiped();
	}

	/**
	 * 设置发生了属性方法拦截器被跳过的标志。
	 */
	public static void setHasGetterResultSkiped() {
		injector.setHasPropertyResultSkiped();
	}

	/**
	 * 重置用于标识是否发生了属性方法拦截器被跳过的标志。
	 */
	public static void resetHasPropertyResultSkiped() {
		injector.resetHasPropertyResultSkiped();
	}

	private Ehcache getLookupIndexedDataCache() throws Exception {
		if (lookupIndexedDataCache == null) {
			Context context = Context.getCurrent();
			lookupIndexedDataCache = (Ehcache) context
					.getServiceBean("lookupPropertyIndexedDataCache");
		}
		return lookupIndexedDataCache;
	}

	public static synchronized int newEntityId() {
		if (maxEntityId >= (Integer.MAX_VALUE - 1))
			maxEntityId = 0;
		return ++maxEntityId;
	}

	public static long getLastTimeStamp() {
		return maxTimeStamp;
	}

	public static synchronized long newTimeStamp() {
		if (maxTimeStamp >= (Long.MAX_VALUE - 1))
			maxTimeStamp = 0;
		return ++maxTimeStamp;
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public EntityState getState() {
		return state;
	}

	public void setState(EntityState state) {
		this.state = state;
	}

	public boolean isStateLocked() {
		return stateLocked;
	}

	public void setStateLocked(boolean stateLocked) {
		this.stateLocked = stateLocked;
	}

	protected abstract Set<String> doGetPropertySet(Object entity);

	public Set<String> getPropertySet(Object entity) {
		if (dataType.isAcceptUnknownProperty()) {
			return doGetPropertySet(entity);
		} else {
			return dataType.getPropertyDefs().keySet();
		}
	}

	protected Map<String, Object> getExProperties(boolean create) {
		if (exProperties == null && create) {
			exProperties = new HashMap<String, Object>();
		}
		return exProperties;
	}

	/**
	 * 返回原始值的Map集合。该Map中的键值为属性名，值为相应的原始值。
	 * 
	 * @param create
	 *            如果原始值集合上不存在，则创建一个全新的原始值集合。
	 */
	public Map<String, Object> getOldValues(boolean create) {
		if (oldValues == null && create) {
			oldValues = new HashMap<String, Object>();
		}
		return oldValues;
	}

	/**
	 * 返回原始值的Map集合。该Map中的键值为属性名，值为相应的原始值。
	 */
	public Map<String, Object> getOldValues() {
		return oldValues;
	}

	/**
	 * 清除当前保存的所有初始值。
	 */
	public void clearOldValues() {
		oldValues = null;
	}

	protected void markPropertyHasRead(String property) {
		if (propertiesHasRead == null) {
			propertiesHasRead = new HashSet<String>();
		}
		propertiesHasRead.add(property);
	}

	protected boolean isPropertyHasRead(String property) {
		return (propertiesHasRead == null) ? false : propertiesHasRead
				.contains(property);
	}

	protected Object readValueByPropertyPath(Object entity,
			String propertyName, String propertyPath) throws Throwable {
		String[] paths = StringUtils.split(propertyPath, '.');
		Object value = entity;
		for (int i = 0; i < paths.length; i++) {
			String path = paths[i];
			if (i == 0 && path.equals(propertyName)) {
				throw new IllegalStateException(
						"[propertyPath] must not base on itself \""
								+ propertyPath + "\".");
			}

			if (EntityUtils.isEntity(value)) {
				value = EntityUtils.getValue(value, path);
			} else if (value instanceof Map<?, ?>) {
				value = ((Map<?, ?>) value).get(path);
			} else {
				value = PropertyUtils.getSimpleProperty(value, path);
			}

			if (value == null)
				break;
		}
		return value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void writeValueByPropertyPath(Object entity, String propertyName,
			String propertyPath, Object value) throws Exception {
		String[] paths = StringUtils.split(propertyPath, '.');
		Object currentEntity = entity;
		EntityDataType currentDataType = dataType;
		for (int i = 0; i < paths.length - 1; i++) {
			String path = paths[i];
			if (i == 0 && path.equals(propertyName)) {
				throw new IllegalStateException(
						"[propertyPath] must not base on itself ["
								+ propertyPath + "].");
			}

			Object tempEntity;
			boolean isMap = currentEntity instanceof Map;
			if (EntityUtils.isEntity(currentEntity)) {
				tempEntity = EntityUtils.getValue(currentEntity, path);
			} else if (currentEntity instanceof Map) {
				tempEntity = ((Map) currentEntity).get(path);
			} else {
				tempEntity = PropertyUtils.getSimpleProperty(currentEntity,
						path);
			}

			if (tempEntity == null) {
				if (currentDataType != null) {
					PropertyDef propertyDef = currentDataType
							.getPropertyDef(path);
					if (propertyDef != null) {
						DataType dataType = propertyDef.getDataType();
						if (dataType instanceof EntityDataType) {
							currentDataType = (EntityDataType) dataType;
							tempEntity = currentDataType.getCreationType()
									.newInstance();
						}
					}
				} else if (isMap) {
					tempEntity = new HashMap();
					currentDataType = null;
				} else {
					Class cl = PropertyUtils.getPropertyType(currentEntity,
							path);
					if (cl.isAssignableFrom(Map.class)) {
						tempEntity = new HashMap();
					} else if (!cl.isInterface()) {
						tempEntity = cl.newInstance();
					}
					currentDataType = null;
				}

				if (tempEntity != null) {
					if (isMap) {
						((Map) currentEntity).put(path, tempEntity);
					} else {
						PropertyUtils.setSimpleProperty(currentEntity, path,
								tempEntity);
					}
				} else {
					throw new IllegalArgumentException(
							"Can not write value to ["
									+ StringUtils.join(paths, '.') + "] on ["
									+ ObjectUtils.identityToString(entity)
									+ "].");
				}
			}
			currentEntity = tempEntity;
		}

		String path = paths[paths.length - 1];
		if (EntityUtils.isEntity(currentEntity)) {
			EntityUtils.setValue(currentEntity, path, value);
		} else if (currentEntity instanceof Map) {
			((Map) currentEntity).put(path, value);
		} else {
			PropertyUtils.setSimpleProperty(currentEntity, path, value);
		}
	}

	protected abstract Object internalReadProperty(Object entity,
			String property) throws Exception;

	protected abstract void internalWriteProperty(Object entity,
			String property, Object value) throws Exception;

	protected final Object internalReadProperty(Object entity, String property,
			boolean isExProp) throws Exception {
		if (isExProp) {
			Map<String, Object> exProperties = getExProperties(false);
			return (exProperties != null) ? exProperties.get(property) : null;
		} else {
			return internalReadProperty(entity, property);
		}
	}

	protected final void internalWriteProperty(Object entity, String property,
			Object value, boolean isExProp) throws Exception {
		if (isExProp) {
			getExProperties(true).put(property, value);
		} else {
			internalWriteProperty(entity, property, value);
		}
	}

	protected Object interceptReadMethod(Object entity, String property,
			Object originResult, boolean isExProp) throws Throwable {
		Object result = originResult;
		if (dataType != null) {
			PropertyDef propertyDef = dataType.getPropertyDef(property);
			if (propertyDef != null) {
				LazyPropertyDef lazyPropertyDef = null;
				String propertyPath = null;
				if (propertyDef instanceof LazyPropertyDef) {
					lazyPropertyDef = (LazyPropertyDef) propertyDef;
					if (result == null
							&& !isPropertyHasRead(property)
							|| lazyPropertyDef != null
							&& !CacheMode
									.isCacheableAtServerSide(lazyPropertyDef
											.getCacheMode())) {
						result = readPropertyDef(entity, propertyDef,
								originResult);
					}
				} else {
					BasePropertyDef basePropertyDef = (BasePropertyDef) propertyDef;
					propertyPath = basePropertyDef.getPropertyPath();
					if (StringUtils.isNotEmpty(propertyPath)) {
						result = readValueByPropertyPath(entity, property,
								propertyPath);
					}
				}

				if (!isGetterInterceptionDisabled()) {
					if (result != null) {
						Object proxy = EntityUtils.toEntity(result,
								propertyDef.getDataType());
						if ((originResult == null || proxy != result)
								&& StringUtils.isEmpty(propertyPath)) {
							synchronized (this) {
								boolean originLocked = isStateLocked();
								setStateLocked(true);
								try {
									internalWriteProperty(entity, property,
											proxy, isExProp);
								} finally {
									setStateLocked(originLocked);
								}
							}
						}
						result = proxy;
					}

					markPropertyHasRead(property);
				}
			}
		}
		return result;
	}

	protected boolean interceptWriteMethod(Object entity, String property,
			Object newValue, boolean isExProp) throws Exception {
		PropertyDef propertyDef = null;
		if (dataType != null) {
			propertyDef = dataType.getPropertyDef(property);
		}

		if (!isStateLocked()) {
			EntityState state = getState();
			if (propertyDef == null) {
				// TODO: 是否要在服务端检查此项？后面再做考虑
				// if (!dataType.isAcceptUnknownProperty()) {
				// throw new IllegalArgumentException("Property [" +
				// property
				// + "] undefined in DataType [" + dataType.getName()
				// + "].");
				// }
			} else {
				if (propertyDef.getValidators() != null) {
					for (Validator validator : propertyDef.getValidators()) {
						validator.validate(newValue);
					}
				}

				// TODO: 暂时不在Server端维护OldValue
				// if (state != EntityState.NEW
				// && propertyDef instanceof BasePropertyDef) {
				// Map<String, Object> oldValues = getOldValues(true);
				// if (!oldValues.containsKey(property)) {
				// Object originValue = internalReadProperty(entity,
				// property, isExProp);
				// if (originValue == null
				// || EntityUtils.isSimpleValue(originValue)) {
				// oldValues.put(property, originValue);
				// }
				// }
				// }
			}

			setTimeStamp(newTimeStamp());
			if (state == EntityState.NONE) {
				setState(EntityState.MODIFIED);
			}
		}

		if (propertyDef != null && propertyDef instanceof BasePropertyDef) {
			BasePropertyDef basePropertyDef = (BasePropertyDef) propertyDef;
			String propertyPath = basePropertyDef.getPropertyPath();
			if (StringUtils.isNotEmpty(propertyPath)) {
				writeValueByPropertyPath(entity, property, propertyPath,
						newValue);
				return false;
			}
		}
		return true;
	}

	private Object readPropertyDef(Object entity, PropertyDef propertyDef,
			Object originResult) throws Throwable {
		Object result;
		if (propertyDef instanceof Lookup) {
			result = readLookup(entity, (Lookup) propertyDef, originResult);
		} else if (propertyDef instanceof Reference) {
			result = readReference(entity, (Reference) propertyDef,
					originResult);
		} else {
			throw new IllegalArgumentException("Unknown PropertyDef type ["
					+ propertyDef + "].");
		}
		return result;
	}

	private Object readReference(Object entity, Reference referenceProperty,
			Object originResult) throws Throwable {
		if (!referenceProperty.shouldIntercept()
				|| isGetterInterceptionDisabled()) {
			setHasGetterResultSkiped();
			return originResult;
		} else {
			DataProvider dataProvider = referenceProperty.getDataProvider();

			JexlContext jexlContext = getExpressionHandler().getJexlContext();
			Object originThis = jexlContext.get(THIS);
			jexlContext.set(THIS, entity);
			try {
				return dataProvider.getResult(referenceProperty.getParameter());
			} finally {
				jexlContext.set(THIS, originThis);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object readLookup(Object entity, Lookup lookupProperty,
			Object originResult) throws Throwable {
		if (!lookupProperty.shouldIntercept() || isGetterInterceptionDisabled()) {
			setHasGetterResultSkiped();
			return originResult;
		} else {
			Object result;
			IndexedLookupData indexedLookupData;

			Ehcache cache = getLookupIndexedDataCache();
			Element element = cache.get(lookupProperty.getCacheKey());
			if (element != null) {
				indexedLookupData = (IndexedLookupData) element
						.getObjectValue();
			} else {
				indexedLookupData = new IndexedLookupData();
				indexedLookupData.setConstraints(lookupProperty
						.getConstraints());

				DataProvider dataProvider = lookupProperty.getDataProvider();
				result = dataProvider.getResult();
				if (result != null) {
					if (result instanceof Collection) {
						Collection entitys = (Collection) result;
						indexedLookupData.addAll(entitys);
					}
				}

				element = new Element(lookupProperty.getCacheKey(),
						indexedLookupData);
				if (lookupProperty.getCacheTimeToLiveSeconds() > 0) {
					element.setTimeToLive(lookupProperty
							.getCacheTimeToLiveSeconds());
				}
				if (lookupProperty.getCacheTimeToIdleSeconds() > 0) {
					element.setTimeToIdle(lookupProperty
							.getCacheTimeToIdleSeconds());
				}
				cache.put(element);
			}

			JexlContext jexlContext = getExpressionHandler().getJexlContext();
			Object originThis = jexlContext.get(THIS);
			jexlContext.set(THIS, entity);
			try {
				List<LookupConstraint> constraints = lookupProperty
						.getConstraints();
				int constraintSize = constraints.size();
				if (constraintSize == 1) {
					LookupConstraint constraint = constraints.get(0);
					Object keyValue;
					if (StringUtils.isNotEmpty(constraint.getKeyProperty())) {
						keyValue = readProperty(entity,
								constraint.getKeyProperty(), false);
					} else {
						keyValue = constraint.getKeyValue();
					}
					result = indexedLookupData.find(keyValue);
				} else {
					Object[] keyValues = new Object[constraintSize];
					for (int i = 0; i < constraintSize; i++) {
						LookupConstraint constraint = constraints.get(i);
						if (StringUtils.isNotEmpty(constraint.getKeyProperty())) {
							keyValues[i] = readProperty(entity,
									constraint.getKeyProperty(), false);
						} else {
							keyValues[i] = constraint.getKeyValue();
						}
					}
					result = indexedLookupData.find(keyValues);
				}

				if (result != null) {
					String resultProperty = lookupProperty.getLookupProperty();
					if (StringUtils.isNotEmpty(resultProperty)) {
						result = EntityWrapper.create(result).get(
								resultProperty);
					}
				}
			} finally {
				jexlContext.set(THIS, originThis);
			}
			return result;
		}
	}

	public Class<?> getPropertyType(Object entity, String property) {
		if (entity instanceof BeanMap) {
			return ((BeanMap) entity).getPropertyType(property);
		} else if (!(entity instanceof Map<?, ?>)) {
			try {
				return PropertyUtils.getPropertyType(entity, property);
			} catch (Exception e) {
				logger.warn(e, e);
			}
		}
		return null;
	}

	public abstract Object readProperty(Object entity, String property,
			boolean ignoreInterceptors) throws Throwable;

	public abstract void writeProperty(Object entity, String property,
			Object value) throws Throwable;

}

class GetterInterceptionInjectorCounter {
	boolean disabled;
	boolean hasPropertyResultSkiped;
}

class GetterInterceptionInjector extends
		ThreadLocal<GetterInterceptionInjectorCounter> {

	public GetterInterceptionInjectorCounter get(boolean autoCreate) {
		GetterInterceptionInjectorCounter counter = get();
		if (counter == null) {
			counter = new GetterInterceptionInjectorCounter();
			set(counter);
		}
		return counter;
	}

	public void disableGetterInterception() {
		get(true).disabled = true;
	}

	public void enableGetterInterception() {
		get(true).disabled = false;
	}

	public boolean isGetterInterceptionDisabled() {
		GetterInterceptionInjectorCounter counter = get();
		return (counter == null) ? false : counter.disabled;
	}

	public boolean hasPropertyResultSkiped() {
		return get(true).hasPropertyResultSkiped;
	}

	public void setHasPropertyResultSkiped() {
		get(true).hasPropertyResultSkiped = true;
	}

	public void resetHasPropertyResultSkiped() {
		get(true).hasPropertyResultSkiped = false;
	}
}
