package com.bstek.dorado.config.definition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.config.ExpressionMethodInterceptor;
import com.bstek.dorado.core.bean.BeanFactoryUtils;
import com.bstek.dorado.core.bean.BeanWrapper;
import com.bstek.dorado.core.bean.Scopable;
import com.bstek.dorado.core.bean.Scope;
import com.bstek.dorado.core.el.EvaluateMode;
import com.bstek.dorado.core.el.Expression;
import com.bstek.dorado.core.io.ResourceCorrelative;
import com.bstek.dorado.util.proxy.ProxyBeanUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-30
 */
public class ObjectDefinition extends Definition {

	public static class CreationInfo {
		private String impl;
		private Scope scope;
		private Map<String, Object> properties = new HashMap<String, Object>();
		private List<Operation> initOperations = new ArrayList<Operation>();
		private Map<String, Object> userDataMap;

		public String getImpl() {
			return impl;
		}

		public void setImpl(String impl) {
			this.impl = impl;
		}

		public Scope getScope() {
			return scope;
		}

		public void setScope(Scope scope) {
			this.scope = scope;
		}

		public Map<String, Object> getProperties() {
			return properties;
		}

		public List<Operation> getInitOperations() {
			return initOperations;
		}

		public void setUserData(String key, Object userData) {
			if (userDataMap == null) {
				userDataMap = new HashMap<String, Object>();
			}
			userDataMap.put(key, userData);
		}

		public Object getUserData(String key) {
			return (userDataMap != null) ? userDataMap.get(key) : null;
		}
	}

	private String impl;
	private Scope scope;
	private String beanId;
	private DefinitionReference<? extends Definition>[] parentReferences;
	private boolean cacheCreatedObject;
	private Object objectCache;

	/**
	 * 返回对象的实现方式。
	 */
	public String getImpl() {
		return impl;
	}

	/**
	 * 设置对象的实现方式。<br>
	 * 此处的实现方式事实是指Bean的描述信息，用于描述Bean的实例化方式。<br>
	 * 请参考{@link com.bstek.dorado.core.bean.BeanFactoryUtils}关于Bean的描述信息的叙述。
	 */
	public void setImpl(String impl) {
		this.impl = impl;
	}

	/**
	 * 返回对象的作用范围。
	 */
	public Scope getScope() {
		return scope;
	}

	/**
	 * 设置对象的作用范围。
	 * 
	 * @see com.bstek.dorado.core.bean.ScopeManager
	 */
	public void setScope(Scope scope) {
		this.scope = scope;
	}

	/**
	 * 返回对象在被放置到{@link com.bstek.dorado.core.bean.ScopeManager}中进行管理时所使用的id。
	 */
	public String getBeanId() {
		return beanId;
	}

	/**
	 * 设置对象在被放置到{@link com.bstek.dorado.core.bean.ScopeManager}中进行管理时所使用的id。
	 */
	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}

	/**
	 * 返回父配置声明对象的引用数组。
	 */
	public DefinitionReference<? extends Definition>[] getParentReferences() {
		return parentReferences;
	}

	/**
	 * 返回父配置声明对象的数组。
	 */
	public Definition[] getParents() {
		// TODO: 当拿不到parent时应给出更有好的异常信息
		if (parentReferences != null) {
			Definition[] parents = new Definition[parentReferences.length];
			for (int i = 0; i < parentReferences.length; i++) {
				parents[i] = parentReferences[i].getDefinition();
			}
			return parents;
		} else {
			return null;
		}
	}

	/**
	 * 设置父配置声明对象的引用数组。
	 */
	public void setParentReferences(
			DefinitionReference<? extends Definition>[] parentReferences) {
		this.parentReferences = parentReferences;
	}

	/**
	 * 设置父配置声明对象的数组。
	 */
	@SuppressWarnings("unchecked")
	public void setParents(Definition[] parents) {
		DefinitionReference<Definition>[] parentReferences = new DefinitionReference[parents.length];
		for (int i = 0; i < parents.length; i++) {
			parentReferences[i] = new DirectDefinitionReference<Definition>(
					parents[i]);
		}
		this.parentReferences = parentReferences;
	}

	public boolean isCacheCreatedObject() {
		return cacheCreatedObject;
	}

	public void setCacheCreatedObject(boolean cacheCreatedObject) {
		this.cacheCreatedObject = cacheCreatedObject;
		if (!cacheCreatedObject)
			objectCache = null;
	}

	@Override
	protected Object doCreate(CreationContext context) throws Exception {
		Object object = context.findInstance(this);
		if (object != null)
			return object;

		CreationInfo creationInfo = new CreationInfo();
		initCreationInfo(creationInfo, this, true);

		if (creationInfo.getScope() == null && cacheCreatedObject
				&& objectCache != null) {
			return objectCache;
		}

		if (StringUtils.isEmpty(creationInfo.getImpl())) {
			creationInfo.setImpl(impl);
		}

		Map<String, Expression> expressionProperties = null;
		Map<String, Object> properties = creationInfo.properties;
		if (properties != null) {
			for (Map.Entry<String, Object> entry : properties.entrySet()) {
				String property = entry.getKey();
				Object value = getFinalValueOrExpression(entry.getValue(),
						context);
				entry.setValue(value);
				if (value instanceof Expression) {
					if (expressionProperties == null) {
						expressionProperties = new HashMap<String, Expression>();
					}
					expressionProperties.put(property, (Expression) value);
				}
			}
		}

		MethodInterceptor[] methodInterceptors = getMethodInterceptors(
				creationInfo, context);
		if (expressionProperties != null && !expressionProperties.isEmpty()) {
			MethodInterceptor expressionInterceptor = createExpressionMethodInterceptor(expressionProperties);
			methodInterceptors = ProxyBeanUtils.appendMethodInterceptor(
					methodInterceptors, expressionInterceptor);
		}

		BeanWrapper wrapper = createObject(creationInfo, methodInterceptors,
				context);
		object = wrapper.getBean();
		if (creationInfo.getScope() == null && cacheCreatedObject) {
			objectCache = object;
		}

		if (wrapper.isNewInstance()) {
			context.pushInstanceStack(this, object);
			try {
				initObject(object, creationInfo, context);
			} finally {
				context.removeInstanceStack(this);
			}
		}
		return object;
	}

	protected Object getFinalValueOrExpression(Object valueDefinition,
			CreationContext creationContext) throws Exception {
		if (valueDefinition != null) {
			Definition definition = null;
			if (valueDefinition instanceof DefinitionReference<?>) {
				definition = ((DefinitionReference<?>) valueDefinition)
						.getDefinition();
			} else if (valueDefinition instanceof Definition) {
				definition = (Definition) valueDefinition;
			}

			Object value;
			if (definition != null) {
				value = definition.create(creationContext);
			} else {
				value = valueDefinition;
			}

			if (value instanceof Expression
					&& ((Expression) value).getEvaluateMode() == EvaluateMode.onInstantiate) {
				value = ((Expression) value).evaluate();
			}
			return value;
		} else {
			return null;
		}
	}

	/**
	 * 初始化对象创建信息。<br>
	 * 即将本配置声明对象中定义的各种信息包括从父对象中继承而来的信息收集到createInfo参数所指定的对象中，以便于创建创建最终对象。
	 * 
	 * @throws Exception
	 */
	protected void initCreationInfo(CreationInfo creationInfo,
			ObjectDefinition definition, boolean processConstrInfos)
			throws Exception {
		if (processConstrInfos) {
			if (creationInfo.getImpl() == null) {
				String impl = definition.getImpl();
				if (impl != null) {
					creationInfo.setImpl(impl);
				}
			}

			if (creationInfo.getScope() == null) {
				Scope scope = definition.getScope();
				if (scope != null) {
					creationInfo.setScope(scope);
				}
			}
		}

		Definition[] parents = definition.getParents();
		if (parents != null) {
			if (parents.length == 1) {
				ObjectDefinition parent = (ObjectDefinition) parents[0];
				initCreationInfo(creationInfo, parent, processConstrInfos);
			} else {
				for (int i = 0; i < parents.length; i++) {
					ObjectDefinition parent = (ObjectDefinition) parents[i];
					initCreationInfo(creationInfo, parent, (i == 0)
							&& processConstrInfos);
				}
			}
		}

		Map<String, Object> properties = definition.getProperties();
		if (!properties.isEmpty()) {
			creationInfo.getProperties().putAll(properties);
		}

		List<Operation> initOperations = definition.getInitOperations();
		if (!initOperations.isEmpty()) {
			creationInfo.getInitOperations().addAll(initOperations);
		}
	}

	/**
	 * 根据创建信息中的定义初始化一个对象。
	 * 
	 * @throws Exception
	 */
	protected void initObject(Object object, CreationInfo creationInfo,
			CreationContext context) throws Exception {
		if (object instanceof ResourceCorrelative) {
			((ResourceCorrelative) object).setResource(getResource());
		}
		if (object instanceof Scopable) {
			((Scopable) object).setScope(getScope());
		}

		Map<String, Object> valueProperties = null;
		Map<String, Object> properties = creationInfo.properties;
		if (properties != null) {
			for (Map.Entry<String, Object> entry : properties.entrySet()) {
				String property = entry.getKey();
				Object value = entry.getValue();
				if (!(value instanceof Expression)) {
					if (valueProperties == null) {
						valueProperties = new HashMap<String, Object>();
					}
					valueProperties.put(property, value);
				}
			}
		}

		if (valueProperties != null) {
			initProperties(object, valueProperties, context);
		}
		executeInitOperations(object, creationInfo.getInitOperations(), context);
	}

	/**
	 * 返回将要关联在新创建的对象（最终对象）上的拦截器的数组。
	 * 
	 * @throws Exception
	 */
	protected MethodInterceptor[] getMethodInterceptors(
			CreationInfo creationInfo, CreationContext context)
			throws Exception {
		return null;
	}

	/**
	 * 根据EL表达式的集合创建一个动态表达式拦截器。<br>
	 * 其中expressionProperties参数的键值为相应的对象属性名，值为EL表达式。
	 * 
	 * @return 新创建的方法拦截器。
	 */
	protected ExpressionMethodInterceptor createExpressionMethodInterceptor(
			Map<String, Expression> expressionProperties) {
		return new ExpressionMethodInterceptor(expressionProperties);
	}

	/**
	 * 根据给定创建信息和拦截器的数组创建一个对象实例。
	 * 
	 * @param creationInfo
	 *            对象创建信息
	 * @param methodInterceptors
	 *            拦截器的数组
	 * @param context
	 *            创建上下文
	 * @return 创建的对象的包装器
	 * @throws Exception
	 */
	protected BeanWrapper createObject(CreationInfo creationInfo,
			MethodInterceptor[] methodInterceptors, CreationContext context)
			throws Exception {
		if (StringUtils.isBlank(creationInfo.impl)) {
			throw new IllegalArgumentException("[impl] could not be empty ["
					+ getClass() + "].");
		}
		if (creationInfo.impl.equals(Map.class.getName())) {
			creationInfo.impl = HashMap.class.getName();
		}
		return BeanFactoryUtils.getBean(creationInfo.impl, methodInterceptors,
				creationInfo.scope, getBeanId());
	}
}
