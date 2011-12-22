/**
 * 
 */
package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.view.annotation.ComponentReference;
import com.bstek.dorado.view.widget.datacontrol.PropertyDataControl;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-5-25
 */
public abstract class AbstractDataEditor extends AbstractEditor implements
		PropertyDataControl {
	private String dataSet;
	private String dataPath;
	private String property;

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

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

}
