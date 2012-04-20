package com.bstek.dorado.view.widget.datacontrol;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-22
 */
@ClientEvents({ @ClientEvent(name = "onGetBindingData"),
		@ClientEvent(name = "onGetBindingDataType") })
public interface DataControl {

	/**
	 * @return the dataSet
	 */
	public String getDataSet();

	/**
	 * @param dataSet
	 *            the dataSet to set
	 */
	public void setDataSet(String dataSet);

	/**
	 * @return the dataPath
	 */
	public String getDataPath();

	/**
	 * @param dataPath
	 *            the dataPath to set
	 */
	public void setDataPath(String dataPath);
}
