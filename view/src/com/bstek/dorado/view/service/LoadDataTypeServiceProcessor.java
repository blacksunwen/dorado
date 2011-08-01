package com.bstek.dorado.view.service;

import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.data.JsonUtils;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jul 14, 2009
 */
public class LoadDataTypeServiceProcessor extends DataServiceProcessorSupport {

	@Override
	@SuppressWarnings("unchecked")
	protected void doExecute(Writer writer, JSONObject json,
			DoradoContext context) throws Exception {
		Collection<String> dataTypeArray = JsonUtils.getJSONArray(json,
				"dataType");
		Map<String, DataType> dataTypeMap = new HashMap<String, DataType>();
		if (dataTypeMap != null) {
			for (String dataTypeName : dataTypeArray) {
				dataTypeMap.put(dataTypeName, getDataType(dataTypeName));
			}
		}

		OutputContext outputContext = new OutputContext(writer);
		outputContext.setUsePrettyJson(Configure
				.getBoolean("view.outputPrettyJson"));
		outputDataTypes(dataTypeMap, outputContext);
	}

}
