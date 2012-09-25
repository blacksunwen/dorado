package com.bstek.dorado.view.config.definition;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.bstek.dorado.common.Namable;
import com.bstek.dorado.common.method.MethodAutoMatchingException;
import com.bstek.dorado.common.method.MethodAutoMatchingUtils;
import com.bstek.dorado.core.bean.BeanFactoryUtils;
import com.bstek.dorado.core.bean.Scope;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.resolver.DataResolver;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.registry.ComponentTypeRegisterInfo;
import com.bstek.dorado.view.widget.Component;

/**
 * 视图的配置声明对象。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 28, 2008
 */
public class ViewDefinition extends ContainerDefinition implements Namable {
	private static final Class<?>[] CONSTRUCTOR_ARG_TYPES = new Class<?>[] { ViewConfig.class };

	private String name;
	private Map<String, ComponentDefinition> componentMap = new HashMap<String, ComponentDefinition>();

	public ViewDefinition(ComponentTypeRegisterInfo registerInfo)
			throws Exception {
		super(registerInfo);
		setConstructorArgTypes(CONSTRUCTOR_ARG_TYPES);
	}

	/**
	 * 返回视图的名称。
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置视图的名称。
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setScope(Scope scope) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 向视图声明中注册一个子组件的声明。
	 * 
	 * @param component
	 *            子组件的配置声明对象。
	 */
	public void registerComponent(ComponentDefinition component) {
		componentMap.put(component.getId(), component);
	}

	/**
	 * 根据组件id获取一个子组件的声明。
	 * 
	 * @param id
	 *            子组件的id。
	 * @return 子组件的配置声明对象。
	 */
	public ComponentDefinition getComponent(String id) {
		return componentMap.get(id);
	}

	/**
	 * 返回所有子组件的Map集合。
	 * 
	 * @return 所有子组件的Map集合。其中该Map集合的键为子组件的id，值为相应子组件的配置声明对象。
	 */
	public Map<String, ComponentDefinition> getComponents() {
		return componentMap;
	}

	@Override
	protected boolean invokePrivateListener(Object object, String listenerName,
			String methodName) throws Exception {
		View view = (View) object;
		Object interceptor = BeanFactoryUtils.getBean(listenerName);
		Method[] methods = MethodAutoMatchingUtils.getMethodsByName(
				interceptor.getClass(), methodName);
		if (methods.length == 0) {
			return true;
		}

		Object retval;
		try {
			Class<?>[] requiredTypes = null;
			Object[] requiredArgs = null;
			Class<?>[] exactTypes = null;
			Object[] exactArgs = null;
			Class<?>[] optionalTypes = new Class<?>[] { object.getClass() };
			Object[] optionalArgs = new Object[] { object };
			Class<?> returnType = void.class;
			retval = MethodAutoMatchingUtils.invokeMethod(methods, interceptor,
					requiredTypes, requiredArgs, exactTypes, exactArgs,
					optionalTypes, optionalArgs, returnType);
		} catch (MethodAutoMatchingException e1) {
			if (methods.length == 1) {
				Method method = methods[0];
				ViewConfig viewConfig = view.getViewConfig();
				String[] parameterNames = MethodAutoMatchingUtils
						.getParameterNames(method);
				Class<?>[] parameterTypes = method.getParameterTypes();
				Object[] args = new Object[parameterNames.length];
				for (int i = 0; i < parameterNames.length; i++) {
					String parameterName = parameterNames[i];
					Class<?> parameterType = parameterTypes[i];
					Object arg = null;
					if (Component.class.isAssignableFrom(parameterType)) {
						if ("view".equals(parameterName)) {
							arg = view;
						} else {
							arg = view.getComponent(parameterName);
						}
					} else if (viewConfig != null) {
						if (DataType.class.isAssignableFrom(parameterType)) {
							arg = viewConfig.getDataType(parameterName);
						} else if (DataProvider.class
								.isAssignableFrom(parameterType)) {
							arg = viewConfig.getDataProvider(parameterName);
						} else if (DataResolver.class
								.isAssignableFrom(parameterType)) {
							arg = viewConfig.getDataResolver(parameterName);
						}
					}
					args[i] = arg;
				}
				retval = method.invoke(interceptor, args);
			} else {
				throw e1;
			}
		}

		if (retval instanceof Boolean) {
			return ((Boolean) retval).booleanValue();
		} else {
			return true;
		}
	}

}
