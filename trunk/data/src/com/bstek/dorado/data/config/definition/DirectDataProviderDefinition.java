package com.bstek.dorado.data.config.definition;

import org.aopalliance.intercept.MethodInterceptor;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.Definition;
import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.config.definition.DirectDefinitionReference;
import com.bstek.dorado.core.bean.BeanWrapper;
import com.bstek.dorado.data.config.xml.DataXmlConstants;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-13
 */
public class DirectDataProviderDefinition extends DataProviderDefinition {
	private Definition dataDefinition;

	public Definition getDataDefinition() {
		return dataDefinition;
	}

	public void setDataDefinition(Definition dataDefinition) {
		this.dataDefinition = dataDefinition;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected BeanWrapper createObject(CreationInfo creationInfo,
			Object[] constructorArgs, MethodInterceptor[] methodInterceptors,
			CreationContext context) throws Exception {
		DataCreationContext createContext = (DataCreationContext) context;
		Object dataType = creationInfo.getProperties().get(
				DataXmlConstants.ATTRIBUTE_DATA_TYPE);
		if (dataType instanceof DataTypeDefinition) {
			dataType = new DirectDefinitionReference<DataTypeDefinition>(
					(DataTypeDefinition) dataType);
		}
		createContext
				.setCurrentDataTypeDefinition((DefinitionReference<DataTypeDefinition>) dataType);
		try {
			return super.createObject(creationInfo, constructorArgs,
					methodInterceptors, context);
		} finally {
			createContext.setCurrentDataTypeDefinition(null);
		}
	}

}
