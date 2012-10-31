/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
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
