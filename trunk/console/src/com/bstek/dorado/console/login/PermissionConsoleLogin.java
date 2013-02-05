package com.bstek.dorado.console.login;

import javax.servlet.http.HttpServletRequest;

/**
 * 允许任何一个用户登陆Console
 * 
 * @author  Alex Tong(mailto:alex.tong@bstek.com)
 * 
 */
public class PermissionConsoleLogin implements ConsoleLogin {

	public boolean validate(String name, String password) {
		return true;
	}

	public boolean isLogin(HttpServletRequest request) {
		return true;
	}

}
