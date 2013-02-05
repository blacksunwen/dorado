package com.bstek.dorado.console.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.bstek.dorado.console.Setting;
import com.bstek.dorado.console.login.ConsoleLogin;
import com.bstek.dorado.web.resolver.WebContextSupportedController;
/**
 * @author Alex Tong(mailto:alex.tong@bstek.com)
 * @since 2010-7-15
 */
public class ConsoleViewResolver extends WebContextSupportedController {

	@Override
	protected ModelAndView doHandleRequest(
			HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse) throws Exception {
		ConsoleLogin consoleLogin = Setting.getConsoleLogin();
		boolean isLogin = consoleLogin.isLogin(httpservletrequest);
		String forward = isLogin ? "forward:/com.bstek.dorado.console.main.Main.d"
				: "forward:/com.bstek.dorado.console.login.Login.d";
		return new ModelAndView(forward);
	}
}
