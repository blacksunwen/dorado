package com.bstek.dorado.view.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.set.UnmodifiableSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.util.clazz.ClassTypeRegistry;
import com.bstek.dorado.view.widget.Component;

/**
 * 默认的组件类型信息注册管理器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 21, 2008
 */
public class DefaultComponentTypeRegistry implements ComponentTypeRegistry {
	private static final Log logger = LogFactory
			.getLog(DefaultComponentTypeRegistry.class);

	private ClassTypeRegistry classTypeRegistry = new ClassTypeRegistry();
	private Map<String, ComponentTypeRegisterInfo> registerInfoMap = new HashMap<String, ComponentTypeRegisterInfo>();
	private Set<ComponentTypeRegisterInfo> registerInfoSet = new LinkedHashSet<ComponentTypeRegisterInfo>();

	public void registerType(ComponentTypeRegisterInfo registerInfo) {
		registerInfoMap.put(registerInfo.getName(), registerInfo);
		Class<? extends Component> classType = registerInfo.getClassType();
		if (classType != null) {
			classTypeRegistry.registerType(classType, registerInfo);
		}
		registerInfoSet.add(registerInfo);
	}

	private ComponentTypeRegisterInfo initializeRefisterInfo(
			ComponentTypeRegisterInfo registerInfo) {
		try {
			if (registerInfo instanceof LazyInitailizeComponentTypeRegistryInfo) {
				LazyInitailizeComponentTypeRegistryInfo lazyRegistryInfo = (LazyInitailizeComponentTypeRegistryInfo) registerInfo;
				if (!lazyRegistryInfo.isInitialized()) {
					lazyRegistryInfo.initialize();
				}
			}
			return registerInfo;
		} catch (Exception e) {
			logger.error(e, e);
			return null;
		}
	}

	public ComponentTypeRegisterInfo getRegisterInfo(String componentName) {
		ComponentTypeRegisterInfo registerInfo = registerInfoMap
				.get(componentName);
		return initializeRefisterInfo(registerInfo);
	}

	public ComponentTypeRegisterInfo getRegisterInfo(Class<?> componentType) {
		ComponentTypeRegisterInfo registerInfo = (ComponentTypeRegisterInfo) classTypeRegistry
				.getMatchingValue(componentType);
		return initializeRefisterInfo(registerInfo);
	}

	@SuppressWarnings("unchecked")
	public Collection<ComponentTypeRegisterInfo> getRegisterInfos() {
		for (ComponentTypeRegisterInfo registerInfo : registerInfoSet) {
			initializeRefisterInfo(registerInfo);
		}
		return UnmodifiableSet.decorate(registerInfoSet);
	}
}