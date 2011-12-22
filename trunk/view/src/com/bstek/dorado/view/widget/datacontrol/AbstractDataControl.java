package com.bstek.dorado.view.widget.datacontrol;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.view.annotation.ComponentReference;
import com.bstek.dorado.view.widget.Control;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-22
 */
@ClientEvents( { @ClientEvent(name = "onGetBindingData"),
		@ClientEvent(name = "onGetBindingDataType") })
public abstract class AbstractDataControl extends Control implements
		DataControl {
	private String dataSet;
	private String dataPath;

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

}
