package com.bstek.dorado.view.widget.grid;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.view.annotation.ComponentReference;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.datacontrol.DataControl;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-29
 */
@Widget(name = "DataGrid", category = "Collection", dependsPackage = "grid")
@ClientObject(prototype = "dorado.widget.DataGrid", shortTypeName = "DataGrid")
public class DataGrid extends AbstractGrid implements DataControl {
	private String dataSet;
	private String dataPath;
	private EntityDataType dataType;
	private boolean supportsPaging;
	private boolean autoCreateColumns = true;
	private FilterMode filterMode = FilterMode.clientSide;
	private SortMode sortModel = SortMode.clientSide;

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

	@XmlProperty(parser = "spring:dorado.dataTypePropertyParser")
	@ClientProperty
	public EntityDataType getDataType() {
		return dataType;
	}

	public void setDataType(EntityDataType dataType) {
		this.dataType = dataType;
	}

	public boolean isSupportsPaging() {
		return supportsPaging;
	}

	public void setSupportsPaging(boolean supportsPaging) {
		this.supportsPaging = supportsPaging;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isAutoCreateColumns() {
		return autoCreateColumns;
	}

	public void setAutoCreateColumns(boolean autoCreateColumns) {
		this.autoCreateColumns = autoCreateColumns;
	}

	@ClientProperty(escapeValue = "clientSide")
	public FilterMode getFilterMode() {
		return filterMode;
	}

	public void setFilterMode(FilterMode filterMode) {
		this.filterMode = filterMode;
	}

	@ClientProperty(escapeValue = "clientSide")
	public SortMode getSortModel() {
		return sortModel;
	}

	public void setSortModel(SortMode sortModel) {
		this.sortModel = sortModel;
	}
}
