package com.bstek.dorado.view.widget.form;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.OutputUtils;
import com.bstek.dorado.view.output.PropertyOutputter;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-6-5
 */
public class CustomSpinnerValueOutputter implements PropertyOutputter {
	public boolean isEscapeValue(Object value) {
		return OutputUtils.isEscapeValue(value);
	}

	public void output(Object object, OutputContext context) throws Exception {
		JsonBuilder jsonBuilder = context.getJsonBuilder();
		jsonBuilder.array();
		for (String section : StringUtils.split((String) object, ";, ")) {
			int i = 0;
			try {
				i = Integer.parseInt(section);
			} catch (Exception e) {
				// do nothing
			}
			jsonBuilder.value(i);
		}
		jsonBuilder.endArray();
	}
}
