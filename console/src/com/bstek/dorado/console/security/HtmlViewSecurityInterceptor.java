package com.bstek.dorado.console.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bstek.dorado.console.Constants;
import com.bstek.dorado.console.Setting;
import com.bstek.dorado.util.PathUtils;
import com.bstek.dorado.view.resolver.HtmlViewResolver;

/**
 * Dorado Console 全局 拦截器
 * 
 * @author Alex Tong(mailto:alex.tong@bstek.com)
 * 
 */
public class HtmlViewSecurityInterceptor extends HandlerInterceptorAdapter {
	private String interceptedNamePattern;

	public String getInterceptedNamePattern() {
		return interceptedNamePattern;
	}

	public void setInterceptedNamePattern(String interceptedNamePattern) {
		this.interceptedNamePattern = interceptedNamePattern;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String path = request.getRequestURI();
		Boolean loginStatus = Setting.getConsoleLogin().isLogin(request);
		if (handler instanceof HtmlViewResolver
				&& PathUtils.match(interceptedNamePattern,
						path.replace('/', '.'))) {
			if (!loginStatus
					&& path.indexOf(".dorado.console.login.Login") < 0) {
				response.sendRedirect(Constants.DORADO_CONSOLE_LOGIN_VIEW_PATH);
				return false;
			}
		}
		return super.preHandle(request, response, handler);
	}

}
