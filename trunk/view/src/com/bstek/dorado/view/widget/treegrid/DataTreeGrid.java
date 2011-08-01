package com.bstek.dorado.view.widget.treegrid;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.view.widget.datacontrol.DataControl;
import com.bstek.dorado.view.widget.tree.BindingConfig;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@Widget(name = "DataTreeGrid", category = "Collection", dependsPackage = "tree-grid")
@ViewObject(prototype = "dorado.widget.DataTreeGrid", shortTypeName = "DataTreeGrid")
@XmlNode(nodeName = "DataTreeGrid")
public class DataTreeGrid extends AbstractTreeGrid implements DataControl {
	private String dataSet;
	private String dataPath;
	private EntityDataType dataType;
	private String currentNodeDataPath;
	private boolean autoCreateColumns;

	private List<BindingConfig> bindingConfigs = new ArrayList<BindingConfig>();

	@ViewAttribute(referenceComponentName = "DataSet")
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

	@XmlProperty(parser = "dorado.dataTypePropertyParser")
	@ViewAttribute(outputter = "dorado.dataTypePropertyOutputter")
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

	@ViewAttribute(defaultValue = "true")
	public boolean isAutoCreateColumns() {
		return autoCreateColumns;
	}

	public void setAutoCreateColumns(boolean autoCreateColumns) {
		this.autoCreateColumns = autoCreateColumns;
	}

	@ViewAttribute
	@XmlSubNode(path = "BindingConfigs/*", fixed = true)
	public List<BindingConfig> getBindingConfigs() {
		return bindingConfigs;
	}

	public void addBindingConfig(BindingConfig bindingConfig) {
		bindingConfigs.add(bindingConfig);
	}
}
