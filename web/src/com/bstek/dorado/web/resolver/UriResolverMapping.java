package com.bstek.dorado.web.resolver;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContextException;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.util.UrlPathHelper;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-15
 */
public class UriResolverMapping extends AbstractHandlerMapping {
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	private PathMatcher pathMatcher = new AntPathMatcher();
	private boolean lazyInitHandlers = false;
	private Map<String, Object> handlerMap = new LinkedHashMap<String, Object>();

	public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
		this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
	}

	public void setUrlDecode(boolean urlDecode) {
		this.urlPathHelper.setUrlDecode(urlDecode);
	}

	public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
		this.urlPathHelper = urlPathHelper;
	}

	public void setPathMatcher(PathMatcher pathMatcher) {
		Assert.notNull(pathMatcher, "PathMatcher must not be null");
		this.pathMatcher = pathMatcher;
	}

	public void setLazyInitHandlers(boolean lazyInitHandlers) {
		this.lazyInitHandlers = lazyInitHandlers;
	}

	@Override
	protected Object getHandlerInternal(HttpServletRequest request)
			throws Exception {
		String lookupPath = urlPathHelper.getLookupPathForRequest(request);
		if (logger.isDebugEnabled()) {
			logger.debug("Looking up handler for [" + lookupPath + "]");
		}
		return lookupHandler(lookupPath);
	}

	protected Object lookupHandler(String urlPath) {
		Object handler = handlerMap.get(urlPath);
		if (handler == null) {
			for (Iterator<String> it = handlerMap.keySet().iterator(); it
					.hasNext();) {
				String registeredPath = it.next();
				if (pathMatcher.match(registeredPath, urlPath)) {
					handler = handlerMap.get(registeredPath);
					break;
				}
			}
		}
		return handler;
	}

	public void registerHandler(String urlPath, Object handler) {
		if (!urlPath.startsWith("/")) {
			urlPath = "/" + urlPath;
		}

		Object mappedHandler = handlerMap.get(urlPath);
		if (mappedHandler != null) {
			throw new ApplicationContextException("Cannot map handler ["
					+ handler + "] to URL path [" + urlPath
					+ "]: there's already handler [" + mappedHandler
					+ "] mapped");
		}

		if (!this.lazyInitHandlers && handler instanceof String) {
			String handlerName = (String) handler;
			if (getApplicationContext().isSingleton(handlerName)) {
				handler = getApplicationContext().getBean(handlerName);
			}
		}

		if (urlPath.equals("/*")) {
			setDefaultHandler(handler);
		} else {
			handlerMap.put(urlPath, handler);
			if (logger.isDebugEnabled()) {
				logger.debug("Mapped URL path [" + urlPath + "] onto handler ["
						+ handler + "]");
			}
		}
	}
}
