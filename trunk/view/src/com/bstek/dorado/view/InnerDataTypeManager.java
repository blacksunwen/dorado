/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.view;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.jexl2.JexlContext;

import com.bstek.dorado.config.definition.Definition;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.el.ExpressionHandler;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;
import com.bstek.dorado.data.type.manager.DefaultDataTypeManager;
import com.bstek.dorado.view.config.InnerDataProviderDefinitionManager;
import com.bstek.dorado.view.config.InnerDataResolverDefinitionManager;
import com.bstek.dorado.view.config.InnerDataTypeDefinitionManager;
import com.bstek.dorado.view.config.definition.ViewConfigDefinition;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-21
 */
public class InnerDataTypeManager extends DefaultDataTypeManager {
	private DataTypeManager parent;
	private Map<String, DataType> privateDataTypeMap;
	private InnerDataTypeDefinitionManager innerDataTypeDefinitionManager;
	private InnerDataProviderDefinitionManager innerDataProviderDefinitionManager;
	private InnerDataResolverDefinitionManager innerDataResolverDefinitionManager;

	public InnerDataTypeManager(
			DataTypeManager parent,
			InnerDataTypeDefinitionManager innerDataTypeDefinitionManager,
			InnerDataProviderDefinitionManager innerDataProviderDefinitionManager,
			InnerDataResolverDefinitionManager innerDataResolverDefinitionManager) {
		this.setDataTypeDefinitionManager(innerDataTypeDefinitionManager);
		this.innerDataTypeDefinitionManager = innerDataTypeDefinitionManager;
		this.innerDataProviderDefinitionManager = innerDataProviderDefinitionManager;
		this.innerDataResolverDefinitionManager = innerDataResolverDefinitionManager;
		this.parent = parent;
	}

	public DataTypeManager getParent() {
		return parent;
	}

	@Override
	public DataType getDataType(String name) throws Exception {
		DataType dataType = null;
		if (privateDataTypeMap != null) {
			dataType = privateDataTypeMap.get(name);
		}
		if (dataType == null) {
			dataType = super.getDataType(name);
		}
		if (dataType == null && parent != null) {
			dataType = parent.getDataType(name);
		}
		return dataType;
	}

	@Override
	public DataType getDataType(Type type) throws Exception {
		// 不对View中声明的DataType提供根据classType获取DataType的功能支持。
		return (parent != null) ? parent.getDataType(type) : null;
	}

	@Override
	public Set<String> getDataTypeNames() {
		Set<String> names = new HashSet<String>();
		if (parent != null) {
			names.addAll(parent.getDataTypeNames());
		}
		names.addAll(getPrivateDataTypeNames());
		return Collections.unmodifiableSet(names);
	}

	public Set<String> getPrivateDataTypeNames() {
		Set<String> names = new HashSet<String>(super.getDataTypeNames());
		if (privateDataTypeMap != null) {
			names.addAll(privateDataTypeMap.keySet());
		}
		return Collections.unmodifiableSet(names);
	}

	/**
	 * @param name
	 * @param dataType
	 */
	public void registerDataType(String name, DataType dataType) {
		if (privateDataTypeMap == null) {
			privateDataTypeMap = new HashMap<String, DataType>();
		}
		privateDataTypeMap.put(name, dataType);
	}

	@Override
	protected DataType getDataTypeByDefinition(
			DataTypeDefinition dataTypeDefinition) throws Exception {
		ViewConfigDefinition viewConfigDefinition = innerDataTypeDefinitionManager
				.getViewConfigDefinition();

		Context context = Context.getCurrent();
		ExpressionHandler expressionHandler = (ExpressionHandler) context
				.getServiceBean("expressionHandler");
		JexlContext jexlContext = expressionHandler.getJexlContext();

		Object originArgumentsVar = jexlContext
				.get(ViewConfigDefinition.ARGUMENT);
		jexlContext.set(ViewConfigDefinition.ARGUMENT,
				viewConfigDefinition.getArguments());

		Definition resourceRelativeDefinition = (Definition) jexlContext
				.get(ViewConfigDefinition.RESOURCE_RELATIVE_DEFINITION);
		jexlContext.set(ViewConfigDefinition.RESOURCE_RELATIVE_DEFINITION,
				viewConfigDefinition);

		Object oldDtdm = context
				.getAttribute("privateDataTypeDefinitionManager");
		Object oldDpdm = context
				.getAttribute("privateDataProviderDefinitionManager");
		Object oldDrdm = context
				.getAttribute("privateDataResolverDefinitionManager");

		context.setAttribute("privateDataTypeDefinitionManager",
				innerDataTypeDefinitionManager);
		context.setAttribute("privateDataProviderDefinitionManager",
				innerDataProviderDefinitionManager);
		context.setAttribute("privateDataResolverDefinitionManager",
				innerDataResolverDefinitionManager);
		try {
			return super.getDataTypeByDefinition(dataTypeDefinition);
		} finally {
			jexlContext.set(ViewConfigDefinition.ARGUMENT, originArgumentsVar);
			jexlContext.set(ViewConfigDefinition.RESOURCE_RELATIVE_DEFINITION,
					resourceRelativeDefinition);

			context.setAttribute("privateDataTypeDefinitionManager", oldDtdm);
			context.setAttribute("privateDataProviderDefinitionManager",
					oldDpdm);
			context.setAttribute("privateDataResolverDefinitionManager",
					oldDrdm);
		}
	}
}
