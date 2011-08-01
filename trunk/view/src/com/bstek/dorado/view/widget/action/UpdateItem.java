package com.bstek.dorado.view.widget.action;

import java.util.Properties;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since May 15, 2009
 */
@XmlNode(nodeName = "UpdateItem")
public class UpdateItem {

	private String dataSet;
	private String dataPath = "!DIRTY_TREE";
	private String alias;
	private RefreshMode refreshMode = RefreshMode.value;
	private boolean firstResultOnly;
	private boolean autoResetEntityState = true;
	private boolean submitOldData;
	private Properties options;

	@ViewAttribute(referenceComponentName = "DataSet")
	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	@ViewAttribute(defaultValue = "!DIRTY_TREE", enumValues = "!DIRTY_TREE,!CASCADE_DIRTY,[#dirty]")
	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@ViewAttribute(defaultValue = "value")
	public RefreshMode getRefreshMode() {
		return refreshMode;
	}

	public void setRefreshMode(RefreshMode refreshMode) {
		this.refreshMode = refreshMode;
	}

	public boolean isFirstResultOnly() {
		return firstResultOnly;
	}

	public void setFirstResultOnly(boolean firstResultOnly) {
		this.firstResultOnly = firstResultOnly;
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isAutoResetEntityState() {
		return autoResetEntityState;
	}

	public void setAutoResetEntityState(boolean autoResetEntityState) {
		this.autoResetEntityState = autoResetEntityState;
	}

	public boolean isSubmitOldData() {
		return submitOldData;
	}

	public void setSubmitOldData(boolean submitOldData) {
		this.submitOldData = submitOldData;
	}

	public Properties getOptions() {
		return options;
	}

	public void setOptions(Properties options) {
		this.options = options;
	}

}
