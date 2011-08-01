package com.bstek.dorado.view.type;

import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.ViewObjectOutputter;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Oct 23, 2008
 */
public class AggregationDataTypeOutputter extends ViewObjectOutputter {

	@Override
	protected void outputObjectProperties(Object object, OutputContext context)
			throws Exception {
		context.getJsonBuilder().key("$type").value("Aggregation");
		super.outputObjectProperties(object, context);
	}
}
