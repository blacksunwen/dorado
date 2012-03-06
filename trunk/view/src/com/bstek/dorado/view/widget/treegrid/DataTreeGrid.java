package com.bstek.dorado.view.widget.treegrid;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.view.annotation.ComponentReference;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.datacontrol.DataControl;
import com.bstek.dorado.view.widget.tree.BindingConfig;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@Widget(name = "DataTreeGrid", category = "Collection",
		dependsPackage = "tree-grid")
@ClientObject(prototype = "dorado.widget.DataTreeGrid",
		shortTypeName = "DataTreeGrid")
public class DataTreeGrid extends AbstractTreeGrid implements DataControl {
	private String dataSet;
	private String dataPath;
	private EntityDataType dataType;
	private String currentNodeDataPath;
	private Boolean autoCreateColumns;

	private List<BindingConfig> bindingConfigs = new ArrayList<BindingConfig>();

	@ComponentReference("DataSet")
	@IdeProperty(highlight = 1)
	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	@IdeProperty(highlight = 1)
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

	public String getCurrentNodeDataPath() {
		return currentNodeDataPath;
	}

	public void setCurrentNodeDataPath(String currentNodeDataPath) {
		this.currentNodeDataPath = currentNodeDataPath;
	}

	public Boolean getAutoCreateColumns() {
		return autoCreateColumns;
	}

	public void setAutoCreateColumns(Boolean autoCreateColumns) {
		this.autoCreateColumns = autoCreateColumns;
	}

	@XmlSubNode(wrapper = @XmlNodeWrapper(nodeName = "BindingConfigs"))
	@ClientProperty
	public List<BindingConfig> getBindingConfigs() {
		return bindingConfigs;
	}

	public void addBindingConfig(BindingConfig bindingConfig) {
		bindingConfigs.add(bindingConfig);
	}
}
