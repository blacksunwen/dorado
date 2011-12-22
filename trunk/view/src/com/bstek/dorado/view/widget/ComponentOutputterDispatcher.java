package com.bstek.dorado.view.widget;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.view.output.ObjectOutputterDispatcher;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.registry.ComponentTypeRegisterInfo;
import com.bstek.dorado.view.registry.ComponentTypeRegistry;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-12-10
 */
public class ComponentOutputterDispatcher extends ObjectOutputterDispatcher {
	private ComponentTypeRegistry componentTypeRegistry;

	public void setComponentTypeRegistry(
			ComponentTypeRegistry componentTypeRegistry) {
		this.componentTypeRegistry = componentTypeRegistry;
	}

	@Override
	protected void outputObject(Object object, OutputContext context)
			throws Exception {
		super.outputObject(object, context);

		if (object instanceof Component) {
			ComponentTypeRegisterInfo registerInfo = componentTypeRegistry
					.getRegisterInfo(object.getClass());
			if (registerInfo != null
					&& StringUtils.isNotEmpty(registerInfo.getDependsPackage())) {
				context.getDependsPackages().add(
						registerInfo.getDependsPackage());
			}
		}
	}
}
