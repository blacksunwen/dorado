package com.bstek.dorado.view.registry;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.config.Parser;
import com.bstek.dorado.util.CloneUtils;
import com.bstek.dorado.view.config.ViewConfigDefinitionFactory;
import com.bstek.dorado.view.config.ViewConfigInfo;
import com.bstek.dorado.view.config.definition.ComponentDefinition;
import com.bstek.dorado.view.config.definition.ViewConfigDefinition;
import com.bstek.dorado.view.config.definition.ViewDefinition;
import com.bstek.dorado.view.manager.ViewConfigManager;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.ComponentParser;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-5
 */
public class AssembledComponentTypeRegisterInfo extends
		ComponentTypeRegisterInfo implements
		LazyInitailizeComponentTypeRegistryInfo {
	private ViewConfigManager viewConfigManager;
	private ComponentTypeRegistry componentTypeRegistry;

	private boolean definitionLoaded;
	private String src;
	private ComponentTypeRegisterInfo superComponentTypeInfo;
	private Map<String, VirtualPropertyDescriptor> virtualProperties;

	public AssembledComponentTypeRegisterInfo(String name) {
		super(name);
	}

	public void setViewConfigManager(ViewConfigManager viewConfigManager) {
		this.viewConfigManager = viewConfigManager;
	}

	public void setComponentTypeRegistry(
			ComponentTypeRegistry componentTypeRegistry) {
		this.componentTypeRegistry = componentTypeRegistry;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public ComponentTypeRegisterInfo getSuperComponentTypeInfo() {
		return superComponentTypeInfo;
	}

	public void setSuperComponentTypeInfo(
			ComponentTypeRegisterInfo superComponentTypeInfo) {
		this.superComponentTypeInfo = superComponentTypeInfo;
	}

	public Map<String, VirtualPropertyDescriptor> getVirtualProperties() {
		return virtualProperties;
	}

	public void setVirtualProperties(
			Map<String, VirtualPropertyDescriptor> virtualProperties) {
		this.virtualProperties = virtualProperties;
	}

	public boolean isInitialized() {
		return definitionLoaded;
	}

	public void initialize() throws Exception {
		definitionLoaded = true;

		String name = getName();
		Class<? extends Component> classType = getClassType();
		if (StringUtils.isEmpty(src) && classType == null) {
			throw new IllegalArgumentException(
					"Both src and classType undefined");
		}

		if (StringUtils.isNotEmpty(src)) {
			String viewName, componentId = null;
			int i = src.lastIndexOf('#');
			if (i < 0) {
				viewName = src;
			} else {
				viewName = src.substring(0, i);
				componentId = src.substring(i + 1);
			}

			ViewConfigDefinition viewConfig = null;
			Object viewConfigFactory = viewConfigManager
					.getViewConfigFactory(viewName);
			if (viewConfigFactory != null
					&& viewConfigFactory instanceof ViewConfigDefinitionFactory) {
				ViewConfigDefinitionFactory vcdf = (ViewConfigDefinitionFactory) viewConfigFactory;
				ViewConfigInfo viewConfigInfo = vcdf
						.getViewConfigInfo(viewName);
				viewConfig = vcdf.create(viewConfigInfo);
			}
			if (viewConfig == null) {
				throw new IllegalArgumentException("AssembleComponent view ["
						+ viewName + "] not found.");
			}

			ViewDefinition view = viewConfig.getViewDefinition();
			ComponentDefinition superComponentDefinition;
			if (StringUtils.isEmpty(componentId)) {
				superComponentDefinition = view;
			} else {
				superComponentDefinition = view.getComponent(componentId);
			}
			if (superComponentDefinition == null) {
				throw new IllegalArgumentException("AssembleComponent [" + name
						+ "] not found in [" + src + "].");
			}
			ComponentTypeRegisterInfo superRegisterInfo = componentTypeRegistry
					.getRegisterInfo(superComponentDefinition
							.getComponentType());

			if (classType == null) {
				classType = superRegisterInfo.getClassType();
				setClassType(classType);
			}

			setSuperComponentTypeInfo(superRegisterInfo);
			Parser parser = getParser();
			if (parser == null) {
				parser = superRegisterInfo.getParser();
				if (parser instanceof ComponentParser) {
					parser = ((Parser) CloneUtils.clone(parser));
				}
				setParser(parser);
			}

			if (parser instanceof ComponentParser) {
				((ComponentParser) parser)
						.setAssembledComponentDefinition(superComponentDefinition);
			}
		} else { // classType一定不为空
			if (getParser() == null) {
				ComponentTypeRegisterInfo superRegisterInfo = componentTypeRegistry
						.getRegisterInfo(classType);
				setParser(superRegisterInfo.getParser());
			}
		}
	}
}
