/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.web.resolver;

import java.util.Locale;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

import com.bstek.dorado.util.clazz.ClassUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2014-8-25
 */
public class ErrorPageViewResolver extends AbstractCachingViewResolver
		implements Ordered {
	private static final String ERROR_PAGE = "/dorado/ErrorPage";

	private int order;
	private Class<? extends View> viewClass;

	public void setOrder(int order) {
		this.order = order;
	}

	public int getOrder() {
		return order;
	}

	@SuppressWarnings("unchecked")
	public void setViewClass(String viewClass) throws ClassNotFoundException {
		this.viewClass = ClassUtils.forName(viewClass);
	}

	public View resolveViewName(String viewName, Locale locale)
			throws Exception {
		return new com.bstek.dorado.web.resolver.ErrorPageView();
	}

	@Override
	protected View loadView(String viewName, Locale locale) throws Exception {
		if (ERROR_PAGE.equals(viewName)) {
			return viewClass.newInstance();
		} else {
			return null;
		}
	}

}
