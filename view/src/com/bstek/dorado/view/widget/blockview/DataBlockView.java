package com.bstek.dorado.view.widget.blockview;


import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.view.annotation.ComponentReference;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.datacontrol.DataControl;

@Widget(name = "DataBlockView", category = "Collection", dependsPackage = "block-view")
@ClientObject(prototype = "dorado.widget.DataBlockView", shortTypeName = "DataBlockView")
public class DataBlockView extends AbstractBlockView implements DataControl {
	private String dataSet;
	private String dataPath;

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

}
