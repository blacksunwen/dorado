package com.bstek.dorado.console.login;

import javax.servlet.http.HttpServletRequest;

/**
 * 禁止一切人员登陆Console
 * 
 * @author Alex Tong(mailto:alex.tong@bstek.com)
 * 
 */
public class ForbinddanceConsoleLogin implements ConsoleLogin {

	public boolean validate(String name, String password) {
		return false;
	}

	public boolean isLogin(HttpServletRequest request) {
		return false;
	}

}
