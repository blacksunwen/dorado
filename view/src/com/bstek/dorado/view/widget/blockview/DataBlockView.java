package com.bstek.dorado.view.widget.blockview;


import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.widget.datacontrol.DataControl;

@Widget(name = "DataBlockView", category = "Collection", dependsPackage = "block-view")
@ViewObject(prototype = "dorado.widget.DataBlockView", shortTypeName = "DataBlockView")
@XmlNode(nodeName = "DataBlockView")
public class DataBlockView extends AbstractBlockView implements DataControl {
	private String dataSet;
	private String dataPath;

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

}
