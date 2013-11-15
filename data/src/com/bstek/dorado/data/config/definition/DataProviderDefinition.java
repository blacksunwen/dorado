/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.data.config.definition;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.common.Namable;
import com.bstek.dorado.common.proxy.PatternMethodInterceptorFilter;
import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.Definition;
import com.bstek.dorado.config.definition.DefinitionManagerAware;
import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.config.definition.DefinitionUtils;
import com.bstek.dorado.data.Constants;
import com.bstek.dorado.data.DataModelObject;
import com.bstek.dorado.data.config.xml.DataXmlConstants;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.provider.DataProviderGetResultMethodInterceptor;
import com.bstek.dorado.data.provider.manager.DataProviderInterceptorInvoker;
import com.bstek.dorado.util.SingletonBeanFactory;
import com.bstek.dorado.util.proxy.BaseMethodInterceptorDispatcher;
import com.bstek.dorado.util.proxy.MethodInterceptorFilter;

/**
 * DataProvider的配置声明对象。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 13, 2008
 * @see com.bstek.dorado.data.provider.DataProvider
 */
public class DataProviderDefinition extends InterceptableDefinition implements
		DefinitionManagerAware<DataProviderDefinitionManager> {
	private DataProviderDefinitionManager dataProviderDefinitionManager;
	private String name;
	private String id;

	private boolean resultDataTypeParentAssigned;

	public DataProviderDefinition() {
		setCacheCreatedObject(true);
	}

	public DataProviderDefinition(String name) {
		this();
		setName(name);
	}

	public void setDefinitionManager(
			DataProviderDefinitionManager definitionManager) {
		dataProviderDefinitionManager = definitionManager;
	}

	/**
	 * 返回DataProvider的名称。
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置DataProvider的名称。
	 */
	public void setName(String name) {
		this.name = name;
		if (StringUtils.isNotEmpty(name) && StringUtils.isEmpty(getBeanId())) {
			setBeanId(Constants.SCOPE_DATA_PROVIDER_PREFIX + name);
		}
	}

	public String getId() {
		return id;
	}

	void setId(String id) {
		this.id = id;
	}

	@Deprecated
	public void setGlobal(boolean global) {
	}

	@Override
	protected MethodInterceptor[] getMethodInterceptors(
			CreationInfo creationInfo, CreationContext context)
			throws Exception {
		MethodInterceptor[] interceptors;
		
		MethodInterceptor getResultMethodInterceptor = (MethodInterceptor) SingletonBeanFactory
				.getInstance(DataProviderGetResultMethodInterceptor.class);
		
		MethodInterceptor[] customMethodInterceptors = dataProviderDefinitionManager
				.getDataProviderMethodInterceptors();
		if (customMethodInterceptors != null) {
			interceptors = new MethodInterceptor[] {
					getResultMethodInterceptor,
					new RootCustomDataProviderInterceptor(
							customMethodInterceptors) };
		} else {
			interceptors = new MethodInterceptor[] { getResultMethodInterceptor };
		}

		MethodInterceptor[] superInterceptors = super.getMethodInterceptors(
				creationInfo, context);
		if (superInterceptors != null) {
			MethodInterceptor[] oldInterceptors = interceptors;
			interceptors = new MethodInterceptor[oldInterceptors.length
					+ superInterceptors.length];
			System.arraycopy(oldInterceptors, 0, interceptors, 0,
					oldInterceptors.length);
			System.arraycopy(superInterceptors, 0, interceptors,
					oldInterceptors.length, superInterceptors.length);
		}
		return interceptors;
	}

	@Override
	protected MethodInterceptor getInterceptorInvoker(String interceptor) {
		return new DataProviderInterceptorInvoker(interceptor);
	}

	@Override
	protected Object doCreate(CreationContext context, Object[] constructorArgs)
			throws Exception {
		// 当DataProvider继承自另一个DataProvider时，指定其中的ResultDataType也从父DataProvider的ResultDataType继承。
		if (!resultDataTypeParentAssigned) {
			DataTypeDefinition resultDataType = (DataTypeDefinition) DefinitionUtils
					.getDefinition(getProperties().get(
							DataXmlConstants.ATTRIBUTE_RESULT_DATA_TYPE));
			if (resultDataType != null && resultDataType.isInner()) {
				DefinitionReference<? extends Definition>[] parentReferences = getParentReferences();
				if (parentReferences != null) {
					for (DefinitionReference<?> parentReference : parentReferences) {
						Definition parent = parentReference.getDefinition();
						if (!(parent instanceof DataProviderDefinition)) {
							continue;
						}

						DataTypeDefinition parentResultDataType = (DataTypeDefinition) DefinitionUtils
								.getDefinition(parent
										.getProperties()
										.get(DataXmlConstants.ATTRIBUTE_RESULT_DATA_TYPE));
						if (parentResultDataType != null) {
							Definition[] originParents = resultDataType
									.getParents();

							Definition[] newParents;
							if (originParents == null) {
								newParents = new Definition[] { parentResultDataType };
							} else {
								newParents = new Definition[originParents.length + 1];
								newParents[0] = parentResultDataType;
								System.arraycopy(originParents, 0, newParents,
										1, originParents.length);
							}
							resultDataType.setParents(newParents);
						}
					}
				}
			}
			resultDataTypeParentAssigned = true;
		}

		return super.doCreate(context, constructorArgs);
	}

	@Override
	protected void doInitObject(Object object, CreationInfo creationInfo,
			CreationContext context) throws Exception {
		((Namable) object).setName(name);
		if (StringUtils.isNotEmpty(id)) {
			((DataModelObject) object).setId(id);
		}
		super.doInitObject(object, creationInfo, context);
	}
}

class RootCustomDataProviderInterceptor extends BaseMethodInterceptorDispatcher {
	private static final String METHOD_NAME = "getResult";
	private static final String PAGING_METHOD_NAME = "getPagingResult";

	public RootCustomDataProviderInterceptor(
			MethodInterceptor[] subMethodInterceptors) {
		super(subMethodInterceptors);
	}

	@Override
	protected boolean filterMethod(Method method) {
		String methodName = method.getName();
		return methodName.equals(METHOD_NAME)
				|| methodName.equals(PAGING_METHOD_NAME);
	}

	@Override
	public MethodInterceptorFilter getMethodInterceptorFilter(Object object,
			Method method, Object[] args) {
		String name = ((DataProvider) object).getName();
		return new PatternMethodInterceptorFilter(name);
	}

};
