package com.bstek.dorado.view.widget.form.trigger;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.view.annotation.ComponentReference;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.datacontrol.PropertyDataControl;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@Widget(name = "DataSetDropDown", category = "Trigger", dependsPackage = "base-widget,grid")
@ClientObject(prototype = "dorado.widget.DataSetDropDown", shortTypeName = "DataSetDropDown")
@ClientEvents( { @ClientEvent(name = "onSetFilterParameter") })
public class DataSetDropDown extends RowListDropDown implements
		PropertyDataControl {
	private String dataSet;
	private String dataPath;
	private String property;
	private boolean useDataBinding = true;
	private boolean filterOnTyping;
	private boolean reloadDataOnOpen;

	@ComponentReference("DataSet")
	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	@Override
	public String getProperty() {
		return property;
	}

	@Override
	public void setProperty(String property) {
		this.property = property;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isUseDataBinding() {
		return useDataBinding;
	}

	public void setUseDataBinding(boolean useDataBinding) {
		this.useDataBinding = useDataBinding;
	}

	@Override
	public boolean isFilterOnTyping() {
		return filterOnTyping;
	}

	@Override
	public void setFilterOnTyping(boolean filterOnTyping) {
		this.filterOnTyping = filterOnTyping;
	}

	public boolean isReloadDataOnOpen() {
		return reloadDataOnOpen;
	}

	public void setReloadDataOnOpen(boolean reloadDataOnOpen) {
		this.reloadDataOnOpen = reloadDataOnOpen;
	}

}
