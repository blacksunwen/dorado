package com.bstek.dorado.web.resolver;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.ToolManager;

import com.bstek.dorado.core.Constants;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-31
 */
public class ErrorPageResolver extends AbstractTextualResolver {
	public static final String EXCEPTION_ATTRIBUTE = "exception";

	private static Log logger = LogFactory.getLog(ErrorPageResolver.class);

	private VelocityEngine velocityEngine;
	private ToolManager toolManager;
	private Properties velocityProperties = new Properties();

	public ErrorPageResolver() {
		setContentType(HttpConstants.CONTENT_TYPE_HTML);

		velocityProperties.setProperty(RuntimeConstants.INPUT_ENCODING,
				Constants.DEFAULT_CHARSET);
		velocityProperties.setProperty(RuntimeConstants.OUTPUT_ENCODING,
				Constants.DEFAULT_CHARSET);
		velocityProperties
				.setProperty("file.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
	}

	private VelocityEngine getVelocityEngine() throws Exception {
		if (velocityEngine == null) {
			velocityEngine = new VelocityEngine();
			velocityEngine.init(velocityProperties);
		}
		return velocityEngine;
	}

	private ToolManager getToolManager() throws Exception {
		if (toolManager == null) {
			toolManager = new ToolManager();
		}
		return toolManager;
	}

	@Override
	protected void execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			doExcecute(request, response);
		} catch (Throwable t) {
			// 确保不会因再次抛出异常而进入死锁状态
			logger.error(t, t);
		}
	}

	private void doExcecute(HttpServletRequest request,
			HttpServletResponse response) throws Exception, IOException {
		response.setContentType(HttpConstants.CONTENT_TYPE_HTML);
		response.setCharacterEncoding(Constants.DEFAULT_CHARSET);

		Context velocityContext = getToolManager().createContext();

		Exception e = (Exception) request.getAttribute(EXCEPTION_ATTRIBUTE);
		if (e != null) {
			logger.error(e, e);

			if (e instanceof PageNotFoundException) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else if (e instanceof PageAccessDeniedException) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			}

			Throwable throwable = e;
			while (throwable.getCause() != null) {
				throwable = throwable.getCause();
			}

			String message = null;
			if (throwable != null) {
				message = throwable.getMessage();
			}
			message = StringUtils.defaultString(message, throwable.getClass()
					.getName());

			velocityContext.put("message", message);
			velocityContext.put(EXCEPTION_ATTRIBUTE, throwable);
		} else {
			velocityContext.put("message",
					"Can not gain exception information!");
		}

		Template template = getVelocityEngine().getTemplate(
				"com/bstek/dorado/web/resolver/ErrorPage.html");

		PrintWriter writer = getWriter(request, response);
		try {
			template.merge(velocityContext, writer);
		} finally {
			writer.flush();
			writer.close();
		}
	}
}
