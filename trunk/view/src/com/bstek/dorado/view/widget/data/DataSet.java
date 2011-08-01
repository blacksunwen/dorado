package com.bstek.dorado.view.widget.data;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.provider.PagingList;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.view.widget.Component;

/**
 * 数据集。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 19, 2008
 */
@Widget(name = "DataSet", category = "General", dependsPackage = "widget")
@ViewObject(prototype = "dorado.widget.DataSet", shortTypeName = "DataSet")
@XmlNode(nodeName = "DataSet")
@ClientEvents( { @ClientEvent(name = "onDataLoad") })
public class DataSet extends Component {
	private DataType dataType;
	private DataProvider dataProvider;
	private int pageSize;
	private Object parameter;
	private LoadMode loadMode = LoadMode.lazy;
	private boolean readOnly;
	private Object data;

	/**
	 * 返回装载数据时使用的分页大小，即按照每页多少条记录来进行分页装载。
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置装载数据时使用的分页大小。
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 返回装载数据时使用的参数，及传递给数据提供器的参数。
	 */
	@ViewAttribute(outputter = "dorado.doradoMapPropertyOutputter", editor = "any")
	public Object getParameter() {
		return parameter;
	}

	/**
	 * 设置装载数据时使用的参数。
	 */
	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}

	/**
	 * 返回被封装数据的数据类型。
	 */
	@ViewAttribute(outputter = "dorado.dataTypePropertyOutputter")
	public DataType getDataType() {
		return dataType;
	}

	/**
	 * 设置被封装数据的数据类型。
	 */
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	/**
	 * 返回数据集对应的数据提供器。
	 */
	@ViewAttribute(outputter = "dorado.dataProviderOutputter")
	public DataProvider getDataProvider() {
		return dataProvider;
	}

	/**
	 * 设置数据集对应的数据提供器。
	 */
	public void setDataProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	/**
	 * @return the loadMode
	 */
	@ViewAttribute(defaultValue = "lazy")
	public LoadMode getLoadMode() {
		return loadMode;
	}

	/**
	 * @param loadMode the loadMode to set
	 */
	public void setLoadMode(LoadMode loadMode) {
		this.loadMode = loadMode;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Object getData() throws Exception {
		if (data == null && dataProvider != null) {
			if (pageSize > 0) {
				data = new PagingList(dataProvider, parameter, pageSize);
			}
			else {
				data = dataProvider.getResult(parameter);
			}
		}
		if (dataType != null) {
			EntityUtils.toEntity(data, dataType);
		}
		return data;
	}

	public void clearData() {
		data = null;
	}

}
