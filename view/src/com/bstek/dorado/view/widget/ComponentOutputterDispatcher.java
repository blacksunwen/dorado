package com.bstek.dorado.view.widget;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.util.Assert;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.Outputter;
import com.bstek.dorado.view.registry.AssembledComponentTypeRegisterInfo;
import com.bstek.dorado.view.registry.ComponentTypeRegisterInfo;
import com.bstek.dorado.view.registry.ComponentTypeRegistry;
import com.bstek.dorado.view.registry.DefaultComponentTypeRegisterInfo;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-26
 */
public class ComponentOutputterDispatcher implements Outputter {
	private ComponentTypeRegistry componentTypeRegistry;

	public void setComponentTypeRegistry(
			ComponentTypeRegistry componentTypeRegistry) {
		this.componentTypeRegistry = componentTypeRegistry;
	}

	public void output(Object object, OutputContext context) throws Exception {
		JsonBuilder json = context.getJsonBuilder();
		if (object instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>) object;
			json.escapeableArray();
			for (Object element : collection) {
				outputComponent((Component) element, context);
			}
			json.endArray();
		}
		else {
			Component component = (Component) object;
			outputComponent(component, context);
		}
	}

	@SuppressWarnings("unchecked")
	private void outputComponent(Component component, OutputContext context)
			throws Exception {
		ComponentTypeRegisterInfo componentInfo = componentTypeRegistry
				.getRegisterInfo(component.getClass());
		DefaultComponentTypeRegisterInfo dci = null;
		if (componentInfo instanceof DefaultComponentTypeRegisterInfo) {
			dci = (DefaultComponentTypeRegisterInfo) componentInfo;
		}
		else if (componentInfo instanceof AssembledComponentTypeRegisterInfo) {
			AssembledComponentTypeRegisterInfo aci = (AssembledComponentTypeRegisterInfo) componentInfo;
			Class<? extends Component> classType = aci.getClassType();
			while (classType != null) {
				Class<?> cl = classType.getSuperclass();
				if (cl != null && Component.class.isAssignableFrom(cl)) {
					classType = (Class<? extends Component>) cl;
				}
				if (classType != null) {
					ComponentTypeRegisterInfo ci = componentTypeRegistry
							.getRegisterInfo(classType);
					if (ci != null
							&& ci instanceof DefaultComponentTypeRegisterInfo) {
						dci = (DefaultComponentTypeRegisterInfo) ci;
						break;
					}
				}
			}
		}
		Assert.notNull(dci);
		if (StringUtils.isNotEmpty(dci.getDependsPackage())) {
			context.getDependsPackages().add(dci.getDependsPackage());
		}
		dci.getOutputter().output(component, context);
	}

}
