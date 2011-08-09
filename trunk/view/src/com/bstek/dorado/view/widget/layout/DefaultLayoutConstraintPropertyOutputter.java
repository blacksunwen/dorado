package com.bstek.dorado.view.widget.layout;

import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.OutputUtils;
import com.bstek.dorado.view.output.PropertyOutputter;
import com.bstek.dorado.view.output.ViewObjectOutputter;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-2-28
 */
public class DefaultLayoutConstraintPropertyOutputter extends
		ViewObjectOutputter implements PropertyOutputter {
	public boolean isEscapeValue(Object value) {
		return OutputUtils.isEscapeValue(value);
	}

	@Override
	public void output(Object object, OutputContext context) throws Exception {
		if (object == Layout.NON_LAYOUT_CONSTRAINT) {
			JsonBuilder json = context.getJsonBuilder();
			json.beginValue();
			context.getWriter().append(
					"dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT");
			json.endValue();
		} else {
			super.output(object, context);
		}
	}

}
