package com.bstek.dorado.view.type;

import java.io.Writer;
import java.util.Map;

import com.bstek.dorado.data.type.AggregationDataType;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.util.proxy.BeanExtender;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.ObjectOutputterDispatcher;
import com.bstek.dorado.view.output.OutputContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Oct 23, 2008
 */
public class DataTypePropertyOutputter extends ObjectOutputterDispatcher {
	private boolean useLazyDataType;

	/**
	 * @param useLazyDataType
	 *            the useLazyDataType to set
	 */
	public void setUseLazyDataType(boolean useLazyDataType) {
		this.useLazyDataType = useLazyDataType;
	}

	@Override
	public boolean isEscapeValue(Object value) {
		DataType dataType = (DataType) value;
		return (dataType == null || "String".equals(dataType.getName()));
	}

	@Override
	public void output(Object object, OutputContext context) throws Exception {
		DataType dataType = (DataType) object;
		JsonBuilder json = context.getJsonBuilder();
		json.beginValue();
		Writer writer = context.getWriter();
		if (BeanExtender.getUserData(dataType, "dorado.dynamicDataType") != null) {
			writer.write("dorado.DataTypeRepository.parseSingleDataType(");
			outputObject(dataType, context);
			writer.write(")");
		} else {
			if (useLazyDataType) {
				writer.write("dorado.LazyLoadDataType.create(v.dataTypeRepository,");
			}
			writer.write("\"" + dataType.getId() + "\"");
			if (useLazyDataType) {
				writer.write(")");
			} else {
				if (context.isShouldOutputDataTypes()) {
					if (dataType instanceof AggregationDataType) {
						dataType = ((AggregationDataType) dataType)
								.getElementDataType();
					}
					if (dataType != null) {
						Map<String, DataType> includeDataTypes = context
								.getIncludeDataTypes(true);
						if (!includeDataTypes.containsKey(dataType.getName())) {
							includeDataTypes.put(dataType.getName(), dataType);
						}
					}
				}
			}
		}
		json.endValue();
	}

}
