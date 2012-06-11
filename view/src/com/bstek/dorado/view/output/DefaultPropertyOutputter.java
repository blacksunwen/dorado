package com.bstek.dorado.view.output;

import java.lang.reflect.Array;

import com.bstek.dorado.data.entity.EntityUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-11
 */
public class DefaultPropertyOutputter implements PropertyOutputter {
	private Outputter objectOutputter;

	public void setObjectOutputter(Outputter objectOutputter) {
		this.objectOutputter = objectOutputter;
	}

	public boolean isEscapeValue(Object value) {
		return OutputUtils.isEscapeValue(value);
	}

	public void output(Object object, OutputContext context) throws Exception {
		JsonBuilder json = context.getJsonBuilder();
		if (EntityUtils.isSimpleValue(object)) {
			json.value(object);
		} else if (object.getClass().isArray()
				&& EntityUtils.isSimpleType(object.getClass()
						.getComponentType())) {
			json.beginValue();
			json.array();
			for (int size = Array.getLength(object), i = 0; i < size; i++) {
				Object element = Array.get(object, i);
				json.value(element);
			}
			json.endArray();
			json.endValue();
		} else {
			json.beginValue();
			objectOutputter.output(object, context);
			json.endValue();
		}
	}
}
