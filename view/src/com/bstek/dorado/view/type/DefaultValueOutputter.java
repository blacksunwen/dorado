package com.bstek.dorado.view.type;

import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.view.output.DataOutputter;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.VirtualPropertyOutputter;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-8-3
 */
public class DefaultValueOutputter extends DataOutputter implements
		VirtualPropertyOutputter {

	public void output(Object object, String property, OutputContext context)
			throws Exception {
		PropertyDef propertyDef = (PropertyDef) object;
		Object defaultValue = propertyDef.getDefaultValue();
		if (defaultValue != null) {
			DataType dataType = propertyDef.getDataType();
			if (dataType != null) {
				defaultValue = dataType.fromObject(defaultValue);
			}

			JsonBuilder jsonBuilder = context.getJsonBuilder();
			jsonBuilder.key("defaultValue");
			outputData(defaultValue, context);
		}
	}

}
