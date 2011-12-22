package com.bstek.dorado.web;

import org.springframework.beans.factory.BeanFactory;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.SpringContextSupport;
import com.bstek.dorado.core.bean.SpringBeanFactorySupport;

public class WebSpringBeanFactory extends SpringBeanFactorySupport {

	@Override
	protected BeanFactory getBeanFactory() throws Exception {
		Context context = Context.getCurrent();
		if (context instanceof SpringContextSupport) {
			return ((SpringContextSupport) context).getApplicationContext();
		} else {
			throw new UnsupportedOperationException(
					"Method not supported in current Thread.");
		}
	}

	public String getBeanNamePrefix() {
		return "spring";
	}

}
