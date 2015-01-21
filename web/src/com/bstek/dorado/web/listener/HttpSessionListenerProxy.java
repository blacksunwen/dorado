package com.bstek.dorado.web.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2014年12月5日
 */
public class HttpSessionListenerProxy extends DelegatingSessionListener {

	private HttpSessionListener httpSessionListener;

	public void setListener(HttpSessionListener httpSessionListener) {
		this.httpSessionListener = httpSessionListener;
	}

	public void sessionCreated(HttpSessionEvent se) {
		httpSessionListener.sessionCreated(se);
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		httpSessionListener.sessionDestroyed(se);
	}

}
