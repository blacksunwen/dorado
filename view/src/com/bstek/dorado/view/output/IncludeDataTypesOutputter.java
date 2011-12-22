package com.bstek.dorado.view.output;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bstek.dorado.data.type.AggregationDataType;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;

/**
 * 用于输出
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Nov 20, 2008
 */
public class IncludeDataTypesOutputter extends ObjectOutputterDispatcher {
	@SuppressWarnings("unchecked")
	public void output(Object object, OutputContext context) throws Exception {
		Map<String, DataType> includeDataTypes = (Map<String, DataType>) object;
		Set<DataType> processedDataTypes = new HashSet<DataType>();
		Set<DataType> dataTypes = new HashSet<DataType>();
		for (Map.Entry<String, DataType> entry : includeDataTypes.entrySet()) {
			DataType dataType = entry.getValue();
			processedDataTypes.add(dataType);
			dataType = getOutputDataType(dataTypes, dataType);
			if (dataType != null) {
				dataTypes.add(dataType);
			}
		}

		JsonBuilder json = context.getJsonBuilder();
		json.array();
		for (DataType dataType : dataTypes) {
			json.beginValue();
			outputObject(dataType, context);
			json.endValue();
		}

		if (processedDataTypes.size() < includeDataTypes.size()) {
			Set<DataType> dataTypes2 = new HashSet<DataType>();
			for (Map.Entry<String, DataType> entry : includeDataTypes
					.entrySet()) {
				DataType dataType = entry.getValue();
				if (!processedDataTypes.contains(dataType)) {
					dataType = getOutputDataType(dataTypes, dataType);
					if (dataType != null) {
						dataTypes.add(dataType);
						dataTypes2.add(dataType);
					}
				}
			}

			for (DataType dataType : dataTypes2) {
				json.beginValue();
				outputObject(dataType, context);
				json.endValue();
			}
		}

		json.endArray();
	}

	private DataType getOutputDataType(Set<DataType> dataTypes,
			DataType dataType) {
		if (dataType instanceof AggregationDataType) {
			DataType elementDataType = ((AggregationDataType) dataType)
					.getElementDataType();
			if (elementDataType instanceof EntityDataType) {
				dataType = elementDataType;
			} else {
				dataType = null;
			}
		} else if (!(dataType instanceof EntityDataType)) {
			dataType = null;
		}
		return dataType;
	}

}
