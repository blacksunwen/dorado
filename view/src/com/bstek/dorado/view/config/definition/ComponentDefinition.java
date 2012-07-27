package com.bstek.dorado.view.config.definition;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.common.Identifiable;
import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.Definition;
import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.config.definition.DirectDefinitionReference;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.el.ExpressionHandler;
import com.bstek.dorado.data.config.definition.ListenableObjectDefinition;
import com.bstek.dorado.view.registry.AssembledComponentTypeRegisterInfo;
import com.bstek.dorado.view.registry.ComponentTypeRegisterInfo;
import com.bstek.dorado.view.widget.Component;

/**
 * 组件的配置声明对象。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 28, 2008
 */
public class ComponentDefinition extends ListenableObjectDefinition implements
		Identifiable {
	private ComponentTypeRegisterInfo registerInfo;
	private String id;
	private String componentType;
	private ComponentDefinition assembledComponentDefinition;
	private boolean assembledComponentParentInited;

	public ComponentDefinition(ComponentTypeRegisterInfo registerInfo) {
		this.registerInfo = registerInfo;
	}

	public ComponentTypeRegisterInfo getRegisterInfo() {
		return registerInfo;
	}

	/**
	 * 返回组件的id。
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置返回组件的id。
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public ComponentDefinition getAssembleComponentDefinition() {
		return assembledComponentDefinition;
	}

	public void setAssembleComponentDefinition(
			ComponentDefinition assembleComponentDefinition) {
		this.assembledComponentDefinition = assembleComponentDefinition;
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	protected Object doCreate(CreationContext context) throws Exception {
		JexlContext jexlContext = null;
		AssembledComponentExpressionObject originAcomp = null;
		Object originAssembledComponentVar = null;
		if (assembledComponentDefinition != null) {
			ExpressionHandler expressionHandler = (ExpressionHandler) Context
					.getCurrent().getServiceBean("expressionHandler");
			jexlContext = expressionHandler.getJexlContext();

			originAcomp = (AssembledComponentExpressionObject) jexlContext
					.get("acomp");
			AssembledComponentExpressionObject acomp = new AssembledComponentExpressionObject(
					getProperties());
			jexlContext.set("acomp", acomp);

			String id = getId();
			if (StringUtils.isEmpty(id)) {
				id = acomp.id(assembledComponentDefinition.id);
				setId(id);
			} else {
				acomp.setRealId(assembledComponentDefinition.id, id);
			}

			/* @Deprecated */
			originAssembledComponentVar = jexlContext.get("virtualProperty");
			jexlContext.set("virtualProperty", getProperties());

			if (!assembledComponentParentInited) {
				assembledComponentParentInited = true;

				DefinitionReference<? extends Definition>[] parentReferences = getParentReferences();
				if (parentReferences == null) {
					setParents(new Definition[] { assembledComponentDefinition });
				} else {
					DirectDefinitionReference<ComponentDefinition> definitionReference = new DirectDefinitionReference<ComponentDefinition>(
							assembledComponentDefinition);
					DefinitionReference<? extends Definition>[] newParentReferences = new DefinitionReference[parentReferences.length + 1];
					newParentReferences[0] = definitionReference;
					System.arraycopy(parentReferences, 0, newParentReferences,
							1, parentReferences.length);
					setParentReferences(newParentReferences);
				}
			}
		}
		try {
			return super.doCreate(context);
		} finally {
			if (assembledComponentDefinition != null) {
				jexlContext.set("acomp", originAcomp);
				jexlContext.set("virtualProperty", originAssembledComponentVar);
			}
		}
	}

	protected void doInitObject(Object object, CreationInfo creationInfo,
			CreationContext context) throws Exception {
		if (assembledComponentDefinition != null) {
			for (String property : ((AssembledComponentTypeRegisterInfo) registerInfo)
					.getVirtualProperties().keySet()) {
				creationInfo.getProperties().remove(property);
			}
		}
		Component component = (Component) object;
		component.setId(id);
		super.doInitObject(object, creationInfo, context);
	}

}
