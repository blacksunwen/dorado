package com.bstek.dorado.view.widget.data;

import com.bstek.dorado.view.output.DataPropertyOutputter;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.VirtualPropertyOutputter;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-9-3
 */
public class DataSetDataPropertyOutputter extends DataPropertyOutputter
		implements VirtualPropertyOutputter {

	public void output(Object object, String property, OutputContext context)
			throws Exception {
		DataSet dataSet = (DataSet) object;
		LoadMode loadMode = dataSet.getLoadMode();
		if (LoadMode.preload.equals(loadMode)) {
			Object data = dataSet.getData();
			JsonBuilder json = context.getJsonBuilder();
			json.key(property);
			outputData(data, context);
		}
		else if (LoadMode.manual.equals(loadMode)) {
			JsonBuilder json = context.getJsonBuilder();
			json.key(property).value(null);
		}
	}

}
