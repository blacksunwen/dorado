package com.bstek.dorado.view.config.definition;

import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.jexl2.JexlContext;

import com.bstek.dorado.common.Namable;
import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.DefinitionManager;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.bean.BeanWrapper;
import com.bstek.dorado.core.el.ExpressionHandler;
import com.bstek.dorado.data.config.definition.DataProviderDefinition;
import com.bstek.dorado.data.config.definition.DataProviderDefinitionManager;
import com.bstek.dorado.data.config.definition.DataResolverDefinition;
import com.bstek.dorado.data.config.definition.DataResolverDefinitionManager;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.config.definition.DataTypeDefinitionManager;
import com.bstek.dorado.data.config.definition.ListenableObjectDefinition;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.data.resolver.manager.DataResolverManager;
import com.bstek.dorado.data.type.manager.DataTypeManager;
import com.bstek.dorado.view.InnerDataProviderManager;
import com.bstek.dorado.view.InnerDataResolverManager;
import com.bstek.dorado.view.InnerDataTypeManager;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewState;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-11
 */
public class ViewConfigDefinition extends ListenableObjectDefinition implements
		Namable {
	private String name;
	private Map<String, Object> arguments;
	private Map<String, Object> viewContext;
	private ViewDefinition viewDefinition;

	private DataTypeDefinitionManager dataTypeDefinitionManager;
	private DataProviderDefinitionManager dataProviderDefinitionManager;
	private DataResolverDefinitionManager dataResolverDefinitionManager;
	private InnerDataTypeManager dataTypeManager;
	private InnerDataProviderManager dataProviderManager;
	private InnerDataResolverManager dataResolverManager;

	public ViewConfigDefinition(
			DataTypeDefinitionManager dataTypeDefinitionManager,
			DataProviderDefinitionManager dataProviderDefinitionManager,
			DataResolverDefinitionManager dataResolverDefinitionManager)
			throws Exception {
		Context context = Context.getCurrent();

		this.dataTypeDefinitionManager = dataTypeDefinitionManager;
		this.dataProviderDefinitionManager = dataProviderDefinitionManager;
		this.dataResolverDefinitionManager = dataResolverDefinitionManager;

		dataTypeManager = new InnerDataTypeManager(this,
				(DataTypeManager) context.getServiceBean("dataTypeManager"));
		dataTypeManager.setDataTypeDefinitionManager(dataTypeDefinitionManager);

		dataProviderManager = new InnerDataProviderManager(this,
				(DataProviderManager) context
						.getServiceBean("dataProviderManager"));
		dataProviderManager
				.setDataProviderDefinitionManager(dataProviderDefinitionManager);

		dataResolverManager = new InnerDataResolverManager(this,
				(DataResolverManager) context
						.getServiceBean("dataResolverManager"));
		dataResolverManager
				.setDataResolverDefinitionManager(dataResolverDefinitionManager);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		if (viewDefinition != null) {
			viewDefinition.setName(name);
		}
	}

	/**
	 * 返回用于管理视图中私有DataType的配置声明管理器。
	 */
	public DefinitionManager<DataTypeDefinition> getDataTypeDefinitionManager() {
		return dataTypeDefinitionManager;
	}

	/**
	 * 返回用于管理视图中私有DataProvider的配置声明管理器。
	 */
	public DefinitionManager<DataProviderDefinition> getDataProviderDefinitionManager() {
		return dataProviderDefinitionManager;
	}

	/**
	 * 返回用于管理视图中私有DataResolver的配置声明管理器。
	 */
	public DefinitionManager<DataResolverDefinition> getDataResolverDefinitionManager() {
		return dataResolverDefinitionManager;
	}

	/**
	 * 返回所有视图参数的Map集合。
	 * 
	 * @return 所有视图参数的Map集合。其中该Map集合的键为参数的名称，值为参数值。
	 */
	public Map<String, Object> getArguments() {
		return arguments;
	}

	public void setArguments(Map<String, Object> arguments) {
		this.arguments = arguments;
	}

	public Map<String, Object> getViewContext() {
		return viewContext;
	}

	public void setViewContext(Map<String, Object> viewContext) {
		this.viewContext = viewContext;
	}

	public ViewDefinition getViewDefinition() {
		return viewDefinition;
	}

	public void setViewDefinition(ViewDefinition viewDefinition) {
		this.viewDefinition = viewDefinition;
		if (viewDefinition != null) {
			viewDefinition.setName(name);
		}
	}

	@Override
	protected Object doCreate(CreationContext context) throws Exception {
		Context doradoContext = Context.getCurrent();
		ExpressionHandler expressionHandler = (ExpressionHandler) doradoContext
				.getServiceBean("expressionHandler");
		JexlContext jexlContext = expressionHandler.getJexlContext();
		final String ARGUMENT = "argument";
		Object originArgumentsVar = jexlContext.get(ARGUMENT);
		jexlContext.set(ARGUMENT, arguments);

		doradoContext.setAttribute("privateDataTypeDefinitionManager",
				dataTypeDefinitionManager);
		doradoContext.setAttribute("privateDataProviderDefinitionManager",
				dataProviderDefinitionManager);
		doradoContext.setAttribute("privateDataResolverDefinitionManager",
				dataResolverDefinitionManager);
		try {
			return super.doCreate(context);
		} finally {
			jexlContext.set(ARGUMENT, originArgumentsVar);
			doradoContext.removeAttribute("privateDataTypeDefinitionManager");
			doradoContext
					.removeAttribute("privateDataProviderDefinitionManager");
			doradoContext
					.removeAttribute("privateDataResolverDefinitionManager");
		}
	}

	@Override
	protected BeanWrapper createObject(CreationInfo creationInfo,
			MethodInterceptor[] methodInterceptors, CreationContext context)
			throws Exception {
		ViewState viewState = (ViewState) Context.getCurrent().getAttribute(
				ViewState.class.getName());
		if (viewState == null || viewState == ViewState.rendering) {
			if (viewContext != null && !viewContext.isEmpty()) {
				DoradoContext doradoContext = DoradoContext.getCurrent();
				for (Map.Entry<String, Object> entry : viewContext.entrySet()) {
					doradoContext.setAttribute(DoradoContext.VIEW,
							entry.getKey(), entry.getValue());
				}
			}
		}
		return super.createObject(creationInfo, methodInterceptors, context);
	}

	@Override
	protected void doInitObject(Object object, CreationInfo creationInfo,
			CreationContext context) throws Exception {
		super.doInitObject(object, creationInfo, context);

		ViewState viewState = (ViewState) Context.getCurrent().getAttribute(
				ViewState.class.getName());
		if (viewState == null || viewState == ViewState.rendering) {
			View view = (View) viewDefinition.create(context);
			ViewConfig viewConfig = (ViewConfig) object;
			viewConfig.setView(view);
			viewConfig.setDataTypeManager(dataTypeManager);
			viewConfig.setDataProviderManager(dataProviderManager);
			viewConfig.setDataResolverManager(dataResolverManager);
		}
	}

}
