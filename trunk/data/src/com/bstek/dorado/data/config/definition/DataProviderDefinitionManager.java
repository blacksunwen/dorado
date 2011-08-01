package com.bstek.dorado.data.config.definition;

import org.aopalliance.intercept.MethodInterceptor;

import com.bstek.dorado.common.proxy.SortableMethodInterceptorSet;
import com.bstek.dorado.config.definition.DefaultDefinitionManager;
import com.bstek.dorado.config.definition.DefinitionManager;
import com.bstek.dorado.util.Assert;

/**
 * DataProvider配置声明管理器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 13, 2008
 */
public class DataProviderDefinitionManager extends
		DefaultDefinitionManager<DataProviderDefinition> {
	private SortableMethodInterceptorSet dataProviderMethodInterceptors = new SortableMethodInterceptorSet();

	public DataProviderDefinitionManager() {
		super();
	}

	public DataProviderDefinitionManager(
			DefinitionManager<DataProviderDefinition> parent) {
		super(parent);
	}

	@Override
	public void registerDefinition(String name,
			DataProviderDefinition definition) {
		Assert.notEmpty(name);

		if (super.getDefinition(name) != null) {
			throw new IllegalStateException("The DataProvider [" + name
					+ "] is already registered!");
		}
		super.registerDefinition(name, definition);
	}

	public void addDataProviderMethodInterceptor(
			MethodInterceptor methodInterceptor) {
		dataProviderMethodInterceptors.add(methodInterceptor);
	}

	public MethodInterceptor[] getDataProviderMethodInterceptors() {
		MethodInterceptor[] methodInterceptors = null;
		if (!dataProviderMethodInterceptors.isEmpty()) {
			methodInterceptors = dataProviderMethodInterceptors
					.toArray(new MethodInterceptor[0]);
		}
		DefinitionManager<DataProviderDefinition> parent = getParent();
		if (parent != null && parent instanceof DataProviderDefinitionManager) {
			MethodInterceptor[] parentMethodInterceptors = ((DataProviderDefinitionManager) parent)
					.getDataProviderMethodInterceptors();
			if (parentMethodInterceptors != null) {
				if (methodInterceptors != null) {
					MethodInterceptor[] finalMethodInterceptors = new MethodInterceptor[parentMethodInterceptors.length
							+ methodInterceptors.length];
					System.arraycopy(parentMethodInterceptors, 0,
							finalMethodInterceptors, 0,
							parentMethodInterceptors.length);
					System.arraycopy(methodInterceptors, 0,
							finalMethodInterceptors,
							parentMethodInterceptors.length,
							methodInterceptors.length);
					methodInterceptors = finalMethodInterceptors;
				} else {
					methodInterceptors = parentMethodInterceptors;
				}
			}
		}
		return methodInterceptors;
	}

}
