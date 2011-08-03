package com.bstek.dorado.sample.basic;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.provider.manager.DataProviderManager;

@Component
public class DynaExpression {

	@Expose
	@SuppressWarnings("rawtypes")
	public String test() throws Exception {
		Context context = Context.getCurrent();
		StringBuffer buf = new StringBuffer();
		buf.append("exp1 : ${context.getAttribute(\"stage\")}\n");
		buf.append("exp2 : $${context.getAttribute(\"stage\")}\n");

		context.setAttribute("stage", "stage 0");

		DataProviderManager dataProviderManager = (DataProviderManager) context
				.getServiceBean("dataProviderManager");
		DataProvider dataProvider = dataProviderManager
				.getDataProvider("DynaExpressionProvider");

		context.setAttribute("stage", "stage 1");

		Map result = (Map) dataProvider.getResult();

		buf.append("\n=== Evaluation Result 1 ===\n");
		buf.append("exp1 = ").append(result.get("exp1")).append('\n');
		buf.append("exp2 = ").append(result.get("exp2")).append('\n');

		context.setAttribute("stage", "stage 2");

		buf.append("\n=== Evaluation Result 2 ===\n");
		buf.append("exp1 = ").append(result.get("exp1")).append('\n');
		buf.append("exp2 = ").append(result.get("exp2")).append('\n');

		context.setAttribute("stage", "stage 3");

		buf.append("\n=== Evaluation Result 3 ===\n");
		buf.append("exp1 = ").append(result.get("exp1")).append('\n');
		buf.append("exp2 = ").append(result.get("exp2"));

		context.setAttribute("stage", "stage 4");

		return buf.toString();
	}
}
