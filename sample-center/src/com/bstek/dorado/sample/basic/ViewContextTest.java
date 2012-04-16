package com.bstek.dorado.sample.basic;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.web.DoradoContext;

@Component
public class ViewContextTest {

	@Expose
	public void testAjax(DoradoContext context) {
		context.setAttribute(DoradoContext.VIEW, "attr1", "value2");

		String attr2 = (String) context.getAttribute(DoradoContext.VIEW,
				"attr2");
		context.setAttribute(DoradoContext.VIEW, "attr2", attr2.toUpperCase());

		context.setAttribute(DoradoContext.VIEW, "attr3",
				"New ViewContext Attribute.");
	}
}
