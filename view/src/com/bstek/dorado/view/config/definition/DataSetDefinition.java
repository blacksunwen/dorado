package com.bstek.dorado.view.config.definition;


import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.DefinitionUtils;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.view.registry.ComponentTypeRegisterInfo;
import com.bstek.dorado.view.widget.data.DataSet;

/**
 * 数据集的配置声明对象。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 31, 2008
 */
public class DataSetDefinition extends ComponentDefinition {
	private Object dataProvider;
	private Object dataType;

	public DataSetDefinition(ComponentTypeRegisterInfo registerInfo) {
		super(registerInfo);
	}

	public Object getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(Object dataProvider) {
		this.dataProvider = dataProvider;
	}

	public Object getDataType() {
		return dataType;
	}

	public void setDataType(Object dataType) {
		this.dataType = dataType;
	}

	@Override
	protected void doInitObject(Object object, CreationInfo creationInfo,
			CreationContext context) throws Exception {
		super.doInitObject(object, creationInfo, context);

		DataSet dataSet = (DataSet) object;
		dataSet.setDataType((DataType) DefinitionUtils.getRealValue(dataType,
				context));

		dataSet.setDataProvider((DataProvider) DefinitionUtils.getRealValue(
				dataProvider, context));
		if (dataSet.getDataType() == null && dataSet.getDataProvider() != null) {
			dataSet.setDataType(dataSet.getDataProvider().getResultDataType());
		}
	}

}
