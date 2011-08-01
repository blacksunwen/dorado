package com.bstek.dorado.view.widget.list;


import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.widget.datacontrol.DataControl;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-28
 */
@Widget(name = "DataListBox", category = "Collection", dependsPackage = "list")
@ViewObject(prototype = "dorado.widget.DataListBox", shortTypeName = "DataListBox")
@XmlNode(nodeName = "DataListBox")
public class DataListBox extends AbstractListBox implements DataControl {
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
