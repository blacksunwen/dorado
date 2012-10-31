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
package com.bstek.dorado.view.output;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

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
			DataType outputDataType = getOutputDataType(dataTypes, dataType,
					context);
			if (outputDataType != null) {
				processedDataTypes.add(dataType);
				dataTypes.add(outputDataType);
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
			dataTypes.clear();
			for (Map.Entry<String, DataType> entry : includeDataTypes
					.entrySet()) {
				DataType dataType = entry.getValue();
				if (!processedDataTypes.contains(dataType)) {
					DataType outputDataType = getOutputDataType(dataTypes,
							dataType, context);
					if (outputDataType != null) {
						processedDataTypes.add(outputDataType);
						dataTypes.add(outputDataType);
					}
				}
			}

			for (DataType dataType : dataTypes) {
				json.beginValue();
				outputObject(dataType, context);
				json.endValue();
			}
		}

		for (DataType dataType : processedDataTypes) {
			includeDataTypes.remove(dataType.getId());
		}
		json.endArray();
	}

	private DataType getOutputDataType(Set<DataType> dataTypes,
			DataType dataType, OutputContext context) {
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

		String dataTypeIdPrefix = context.getOutputtableDataTypeIdPrefix();
		if (dataType != null && StringUtils.isNotEmpty(dataTypeIdPrefix)) {
			if (!dataType.getId().startsWith(dataTypeIdPrefix)) {
				dataType = null;
			}
		}
		return dataType;
	}

}
