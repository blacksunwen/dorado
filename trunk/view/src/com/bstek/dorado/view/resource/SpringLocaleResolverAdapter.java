﻿/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.view.resource;

import java.util.Locale;

import com.bstek.dorado.core.resource.LocaleResolver;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-8
 */
public class SpringLocaleResolverAdapter implements LocaleResolver {
	private org.springframework.web.servlet.LocaleResolver springLocaleResolver;

	public void setSpringLocaleResolver(
			org.springframework.web.servlet.LocaleResolver springLocaleResolver) {
		this.springLocaleResolver = springLocaleResolver;
	}

	public Locale resolveLocale() throws Exception {
		Locale locale = null;
		if (springLocaleResolver != null) {
			locale = springLocaleResolver.resolveLocale(DoradoContext
					.getAttachedRequest());
		}
		return locale;
	}

}
