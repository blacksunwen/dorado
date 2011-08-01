package com.bstek.dorado.web;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.SpringContextSupport;
import com.bstek.dorado.core.io.ResourceLoader;
import com.bstek.dorado.util.Assert;

/**
 * Web应用中使用的上下文对象。
 * <p>
 * HttpContext应在每次请求开始时被初始化，同时也最好在请求结束时被清除。<br>
 * 通过HttpContext用户可以取到当前线程相关的{@link javax.servlet.http.HttpServletRequest}对象。
 * </p>
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 23 Feb 2007
 */
public class DoradoContext extends SpringContextSupport {

	/**
	 * 表示Web请求的范围。
	 */
	public static final String REQUEST = "request";

	/**
	 * 表示视图的范围。
	 */
	public static final String VIEW = "view";

	/**
	 * 表示Web会话的范围。
	 */
	public static final String SESSION = "seesion";

	/**
	 * 表示整个Web应用的范围。
	 */
	public static final String APPLICATION = "application";

	private static final String RESOURCE_LOADER_PROPERTY = "core.resourceLoader";

	private static final String ATTRIBUTE_KEY = DoradoContext.class.getName();
	private static ResourceLoader resourceLoader;

	private ServletContext servletContext;
	private HttpServletRequest request;
	private Map<String, Object> viewContext;

	public static DoradoContext getCurrent() {
		return (DoradoContext) Context.getCurrent();
	}

	@Override
	protected ResourceLoader getResourceLoader() {
		if (resourceLoader == null) {
			try {
				String resourceLoaderClass = Configure
						.getString(RESOURCE_LOADER_PROPERTY);
				Assert.notEmpty(resourceLoaderClass, "\""
						+ RESOURCE_LOADER_PROPERTY + "\" not configured.");

				Class<?> cl = ClassUtils.forName(resourceLoaderClass,
						ClassUtils.getDefaultClassLoader());
				if (WebResourceLoader.class.isAssignableFrom(cl)) {
					WebApplicationContext wac = getWebApplicationContext();
					if (wac == null) {
						throw new IllegalStateException(
								"Can not get ResourceLoader before WebApplicationContext initialized.");
					} else {
						resourceLoader = (ResourceLoader) cl.newInstance();
						((WebResourceLoader) resourceLoader)
								.setWebApplicationContext(wac);
					}
				} else {
					resourceLoader = (ResourceLoader) cl.newInstance();
				}
			} catch (IllegalStateException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}
		return resourceLoader;
	}

	@Override
	public ApplicationContext getApplicationContext() throws Exception {
		return getWebApplicationContext();
	}

	public static WebApplicationContext getAttachedWebApplicationContext() {
		Context context = Context.getCurrent();
		if (context instanceof DoradoContext) {
			DoradoContext wc = (DoradoContext) context;
			return wc.getWebApplicationContext();
		} else {
			throw new UnsupportedOperationException(
					"Method not supported in current Thread.");
		}
	}

	public static ServletContext getAttachedServletContext() {
		Context context = Context.getCurrent();
		if (context instanceof DoradoContext) {
			DoradoContext wc = (DoradoContext) context;
			return wc.getServletContext();
		} else {
			throw new UnsupportedOperationException(
					"Method not supported in current Thread.");
		}
	}

	/**
	 * 返回当前线程相关的{@link javax.servlet.http.HttpServletRequest}对象。
	 */
	public static HttpServletRequest getAttachedRequest() {
		Context context = Context.getCurrent();
		if (context instanceof DoradoContext) {
			DoradoContext wc = (DoradoContext) context;
			return wc.getRequest();
		} else {
			throw new UnsupportedOperationException(
					"Method not supported in current Thread.");
		}
	}

	public WebApplicationContext getWebApplicationContext() {
		return WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * 根据传入的请求对象创建并初始化一个WebContext对象。<br>
	 * 如果当前线程中已经存在一个可用的WebContext对象，那么此方法会直接返回该WebContext对象。
	 * 
	 * @param request
	 *            请求对象。
	 * @param servletContext
	 *            Spring中的WebApplicationContext对象。
	 * @return 上下文对象。
	 */
	public static DoradoContext init(ServletContext servletContext,
			HttpServletRequest request) {
		DoradoContext context = (DoradoContext) request
				.getAttribute(ATTRIBUTE_KEY);
		if (context == null) {
			context = new DoradoContext();
			context.servletContext = servletContext;
			context.request = request;
			request.setAttribute(ATTRIBUTE_KEY, context);
			attachToThreadLocal(context);
		} else {
			Assert.isInstanceOf(DoradoContext.class, context);
		}
		return context;
	}

	/**
	 * 根据传入的ServletContext对象创建并初始化一个WebContext对象。
	 * 
	 * @param servletContext
	 *            Spring中的WebApplicationContext对象。
	 * @return 上下文对象。
	 */
	public static DoradoContext init(ServletContext servletContext) {
		return init(servletContext, true);
	}

	public static DoradoContext init(ServletContext servletContext,
			boolean attachToThread) {
		DoradoContext context = new DoradoContext();
		context.servletContext = servletContext;
		context.request = null;
		if (attachToThread) {
			attachToThreadLocal(context);
		}
		return context;
	}

	/**
	 * 如果当前线程中存在一个WebContext的话则清除该对象。
	 * 
	 * @param request
	 *            请求对象。
	 * @return 当前线程中的上下文对象。
	 */
	public static DoradoContext dispose(HttpServletRequest request) {
		if (request != null) {
			request.removeAttribute(ATTRIBUTE_KEY);
		}
		DoradoContext context = (DoradoContext) dettachFromThreadLocal();
		if (context != null) {
			Assert.isInstanceOf(DoradoContext.class, context);
			context.request = null;
		}
		return context;
	}

	/**
	 * 销毁本WebContext对象。
	 * 
	 * @return 当前线程中的上下文对象。
	 */
	public static DoradoContext dispose() {
		return dispose((HttpServletRequest) null);
	}

	/**
	 * 返回指定属性的值。此方法相当于从{@link #REQUEST}中获取属性值的操作。
	 * 
	 * @param key
	 *            属性名。
	 * @return 值。
	 */
	@Override
	public Object getAttribute(String key) {
		return getAttribute(REQUEST, key);
	}

	@Override
	public void removeAttribute(String key) {
		removeAttribute(REQUEST, key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		setAttribute(REQUEST, key, value);
	}

	protected void throwsViewContextNotAvailable() {
		throw new IllegalStateException(
				"The ViewContext is currently not available.");
	}

	private Object throwsInvalidScope(String scope)
			throws IllegalArgumentException {
		throw new IllegalArgumentException("Invalid scope [" + scope + "].");
	}

	/**
	 * 返回指定范围内某属性的值。
	 * 
	 * @param scope
	 *            范围。可使用的值包括{@link #REQUEST}、{@link #VIEW}、{@link #SESSION}、
	 *            {@link #APPLICATION}
	 * @param key
	 *            属性名。
	 * @return 值。
	 */
	public Object getAttribute(String scope, String key) {
		if (REQUEST.equals(scope)) {
			return (request != null) ? request.getAttribute(key) : null;
		} else if (VIEW.equals(scope)) {
			if (viewContext == null) {
				throwsViewContextNotAvailable();
			}
			return viewContext.get(key);
		} else if (SESSION.equals(scope)) {
			if (request != null) {
				HttpSession session = request.getSession(false);
				if (session != null) {
					return session.getAttribute(key);
				}
			}
			return null;
		} else if (APPLICATION.equals(scope)) {
			if (request != null) {
				HttpSession session = request.getSession(false);
				if (session != null) {
					return session.getServletContext().getAttribute(key);
				}
			}
			return null;
		} else {
			return throwsInvalidScope(scope);
		}
	}

	/**
	 * 删除指定范围内某属性。
	 * 
	 * @param scope
	 *            范围。可使用的值包括{@link #REQUEST}、{@link #VIEW}、{@link #SESSION}、
	 *            {@link #APPLICATION}
	 * @param key
	 *            属性名。
	 */
	public void removeAttribute(String scope, String key) {
		if (REQUEST.equals(scope)) {
			request.removeAttribute(key);
		} else if (VIEW.equals(scope)) {
			if (viewContext == null) {
				throwsViewContextNotAvailable();
			}
			viewContext.remove(key);
		} else if (SESSION.equals(scope)) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.removeAttribute(key);
			}
		} else if (APPLICATION.equals(scope)) {
			servletContext.removeAttribute(key);
		} else {
			throwsInvalidScope(scope);
		}
	}

	/**
	 * 设置指定范围内某属性的值。
	 * 
	 * @param scope
	 *            范围。可使用的值包括{@link #REQUEST}、{@link #VIEW}、{@link #SESSION}、
	 *            {@link #APPLICATION}
	 * @param key
	 *            属性名。
	 * @param value
	 *            值。
	 */
	public void setAttribute(String scope, String key, Object value) {
		if (REQUEST.equals(scope)) {
			request.setAttribute(key, value);
		} else if (VIEW.equals(scope)) {
			if (viewContext == null) {
				throwsViewContextNotAvailable();
			}
			viewContext.put(key, value);
		} else if (SESSION.equals(scope)) {
			request.getSession().setAttribute(key, value);
		} else if (APPLICATION.equals(scope)) {
			servletContext.setAttribute(key, value);
		} else {
			throwsInvalidScope(scope);
		}
	}

	Map<String, Object> getViewContext() {
		return viewContext;
	}

	void setViewContext(Map<String, Object> viewContext) {
		this.viewContext = viewContext;
	}
}
