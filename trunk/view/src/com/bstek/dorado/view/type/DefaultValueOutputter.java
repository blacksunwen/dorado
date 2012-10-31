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
