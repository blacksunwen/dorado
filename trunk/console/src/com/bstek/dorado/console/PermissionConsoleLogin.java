package com.bstek.dorado.console;

/**
 * 允许任何一个用户登陆Console
 * @author mark.li@bstek.com
 *
 */
public class PermissionConsoleLogin implements ConsoleLogin {

	public String getToken() {
		return "Permission";
	}

	public boolean validate(String inputToken) {
		return true;
	}

	public boolean isLogin() {
		return true;
	}

	public void onView() {
		
	}

}
