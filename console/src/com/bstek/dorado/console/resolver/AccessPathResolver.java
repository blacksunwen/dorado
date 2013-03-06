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

package com.bstek.dorado.console.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.bstek.dorado.console.Setting;
import com.bstek.dorado.console.authentication.AuthenticationManager;
import com.bstek.dorado.web.resolver.WebContextSupportedController;
/**
 * @author Alex Tong(mailto:alex.tong@bstek.com)
 * @since 2010-7-15
 */
public class AccessPathResolver extends WebContextSupportedController {

	@Override
	protected ModelAndView doHandleRequest(
			HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse) throws Exception {
		AuthenticationManager consoleLogin = Setting.getAuthenticationManager();
		boolean isLogin = consoleLogin.isAuthenticated(httpservletrequest);
		String forward = isLogin ? "forward:/com.bstek.dorado.console.Main.d"
				: "forward:/com.bstek.dorado.console.Login.d";
		return new ModelAndView(forward);
	}
}
