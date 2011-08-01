/**
 * 
 */
package com.bstek.dorado.view.manager;

import java.util.HashMap;
import java.util.Map;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.common.MetaDataSupport;
import com.bstek.dorado.common.Namable;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.resolver.DataResolver;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.util.proxy.BeanExtender;
import com.bstek.dorado.view.InnerDataProviderManager;
import com.bstek.dorado.view.InnerDataResolverManager;
import com.bstek.dorado.view.InnerDataTypeManager;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewState;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-11
 */
public class ViewConfig implements Namable, MetaDataSupport {
	protected static final String VIEW_STATE_ATTRIBUTE_KEY = ViewState.class
			.getName();

	private String name;
	private Map<String, Object> metaData;
	private Map<String, Object> arguments = new HashMap<String, Object>();
	private InnerDataTypeManager innerDataTypeManager;
	private InnerDataProviderManager innerDataProviderManager;
	private InnerDataResolverManager innerDataResolverManager;
	private View view;

	@ViewAttribute(ignored = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		if (view != null) {
			view.setName(name);
		}
	}

	public ViewState getState() {
		ViewState viewState = (ViewState) Context.getCurrent().getAttribute(
				VIEW_STATE_ATTRIBUTE_KEY);
		return (viewState == null) ? ViewState.rendering : viewState;
	}

	@XmlProperty(composite = true)
	@ViewAttribute(outputter = "#ignore")
	public Map<String, Object> getMetaData() {
		return metaData;
	}

	public void setMetaData(Map<String, Object> metaData) {
		this.metaData = metaData;
	}

	public Map<String, Object> getArguments() {
		return arguments;
	}

	public void setDataTypeManager(InnerDataTypeManager innerDataTypeManager) {
		this.innerDataTypeManager = innerDataTypeManager;
	}

	public void setDataProviderManager(
			InnerDataProviderManager innerDataProviderManager) {
		this.innerDataProviderManager = innerDataProviderManager;
	}

	public void setDataResolverManager(
			InnerDataResolverManager innerDataResolverManager) {
		this.innerDataResolverManager = innerDataResolverManager;
	}

	public DataType getDataType(String name) throws Exception {
		return (innerDataTypeManager == null) ? null : innerDataTypeManager
				.getDataType(name);
	}

	public DataProvider getDataProvider(String name) throws Exception {
		return (innerDataProviderManager == null) ? null
				: innerDataProviderManager.getDataProvider(name);
	}

	public DataResolver getDataResolver(String name) throws Exception {
		return (innerDataResolverManager == null) ? null
				: innerDataResolverManager.getDataResolver(name);
	}

	public DataType createDataType(String name) throws Exception {
		return createDataType(name, "Entity");
	}

	public DataType createDataType(String name, String parent) throws Exception {
		if (innerDataTypeManager == null) {
			throw new NullPointerException("[innerDataTypeManager] undefined.");
		}
		if (innerDataTypeManager.getDataType(name) != null) {
			throw new IllegalArgumentException("DataType [" + name
					+ "] already exists.");
		}

		DataType dataType = null;
		if (parent != null) {
			dataType = innerDataTypeManager.getDataType(parent);
			if (dataType != null) {
				dataType = (DataType) BeanExtender.setUserData(dataType,
						"dorado.dynamicDataType", Boolean.TRUE);
				((Namable) dataType).setName(name);
				innerDataTypeManager.registerDataType(name, dataType);
			}
		}
		return dataType;
	}

	@ViewAttribute(ignored = true)
	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
		if (view != null) {
			view.setViewConfig(this);
		}
	}

}
