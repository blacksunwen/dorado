package com.bstek.dorado.view;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;
import com.bstek.dorado.data.type.manager.DefaultDataTypeManager;
import com.bstek.dorado.view.config.definition.ViewConfigDefinition;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-21
 */
public class InnerDataTypeManager extends DefaultDataTypeManager {
	private DataTypeManager parent;
	private Map<String, DataType> privateDataTypeMap;
	private ViewConfigDefinition viewConfigDefinition;

	public InnerDataTypeManager(ViewConfigDefinition viewConfigDefinition,
			DataTypeManager parent) {
		this.viewConfigDefinition = viewConfigDefinition;
		this.parent = parent;
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
		Context context = Context.getCurrent();
		context.setAttribute("privateDataTypeDefinitionManager",
				viewConfigDefinition.getDataTypeDefinitionManager());
		context.setAttribute("privateDataProviderDefinitionManager",
				viewConfigDefinition.getDataProviderDefinitionManager());
		context.setAttribute("privateDataResolverDefinitionManager",
				viewConfigDefinition.getDataResolverDefinitionManager());
		try {
			return super.getDataTypeByDefinition(dataTypeDefinition);
		} finally {
			context.removeAttribute("privateDataTypeDefinitionManager");
			context.removeAttribute("privateDataProviderDefinitionManager");
			context.removeAttribute("privateDataResolverDefinitionManager");
		}
	}
}
