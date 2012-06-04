package com.bstek.dorado.mock;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.SpringContextSupport;
import com.bstek.dorado.core.bean.SpringBeanFactorySupport;

public class MockSpringBeanFactory extends SpringBeanFactorySupport {

	@Override
	protected org.springframework.beans.factory.BeanFactory getBeanFactory()
			throws Exception {
		Context context = Context.getCurrent();
		return ((SpringContextSupport) context).getApplicationContext();
	}

	public String getBeanNamePrefix() {
		return "spring";
	}

}
