/**
 * 
 */
package com.bstek.dorado.view;

import java.util.Map;

import com.bstek.dorado.view.output.DataOutputter;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.VirtualPropertyOutputter;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.DoradoContextUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-5-12
 */
public class ViewContextPropertyOutputter extends DataOutputter implements
		VirtualPropertyOutputter {

	public void output(Object object, String property, OutputContext context)
			throws Exception {
		View view = (View) object;
		DoradoContext doradoContext = DoradoContext.getCurrent();
		Map<String, Object> viewContext = DoradoContextUtils
				.getViewContextByBindingObject(doradoContext,
						view.getViewConfig());
		if (viewContext != null && !viewContext.isEmpty()) {
			JsonBuilder json = context.getJsonBuilder();
			json.key(property).beginValue();
			super.output(viewContext, context);
			json.endValue();
		}
	}

}
