package com.bstek.dorado.console;

import java.util.UUID;

import com.bstek.dorado.web.DoradoContext;

public class DefaultConsoleLogin implements ConsoleLogin {

	private final String securityToken = UUID.randomUUID().toString();
	private final static String ATTRIBUTE_KEY_SUCCESS = DefaultConsoleLogin.class
			.getName() + "_SUCCESS";

	public String getToken() {
		return securityToken;
	}

	public boolean validate(String inputToken) {
		boolean v = getToken().equals(inputToken);

		DoradoContext ctx = DoradoContext.getCurrent();
		ctx.setAttribute(DoradoContext.SESSION, ATTRIBUTE_KEY_SUCCESS,
				Boolean.valueOf(v));

		return v;
	}

	public boolean isLogin() {
		DoradoContext ctx = DoradoContext.getCurrent();

		return Boolean.TRUE.equals(ctx.getAttribute(DoradoContext.SESSION,
				ATTRIBUTE_KEY_SUCCESS));
	}

	public void onView() {
		DoradoContext ctx = DoradoContext.getCurrent();
		ctx.removeAttribute(DoradoContext.SESSION,
				ATTRIBUTE_KEY_SUCCESS);
		
		System.out.println("[loginToken] " +  getToken());
	}

}
