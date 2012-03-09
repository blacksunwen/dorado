package com.bstek.dorado.view.resolver;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;

import com.bstek.dorado.data.config.ConfigurableDataConfigManager;
import com.bstek.dorado.data.config.DataConfigManager;
import com.bstek.dorado.data.config.ReloadableDataConfigManagerSupport;
import com.bstek.dorado.util.PathUtils;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.manager.ViewConfigManager;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.resolver.AbstractTextualResolver;
import com.bstek.dorado.web.resolver.HttpConstants;
import com.bstek.dorado.web.resolver.PageAccessDeniedException;
import com.bstek.dorado.web.resolver.PageNotFoundException;

/**
 * 用于直接根据View配置文件生成客户端界面的渲染器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 26, 2008
 */
public class HtmlViewResolver extends AbstractTextualResolver {
	private final static long ONE_SECOND = 1000L;
	private final static long MIN_DATA_CONFIG_VALIDATE_SECONDS = 5;

	private DataConfigManager dataConfigManager;
	private ViewConfigManager viewConfigManager;
	private VelocityHelper velocityHelper;
	private String templateFile;
	private String uriPrefix;
	private int uriPrefixLen;
	private String uriSuffix;
	private int uriSuffixLen;

	private boolean shouldAutoLoadDataConfigResources;
	private long lastValidateTimestamp;

	public HtmlViewResolver() {
		setContentType(HttpConstants.CONTENT_TYPE_HTML);
	}

	public void setDataConfigManager(DataConfigManager dataConfigManager) {
		this.dataConfigManager = dataConfigManager;

		if (dataConfigManager instanceof ReloadableDataConfigManagerSupport) {
			final ReloadableDataConfigManagerSupport reloadableDataConfigManagerSupport = (ReloadableDataConfigManagerSupport) dataConfigManager;
			shouldAutoLoadDataConfigResources = (reloadableDataConfigManagerSupport
					.isAutoReloadEnabled() && !reloadableDataConfigManagerSupport
					.isUseAutoReloadThread());
			lastValidateTimestamp = System.currentTimeMillis();
		}
	}

	public void setViewConfigManager(ViewConfigManager viewConfigManager) {
		this.viewConfigManager = viewConfigManager;
	}

	public void setVelocityHelper(VelocityHelper velocityHelper) {
		this.velocityHelper = velocityHelper;
	}

	/**
	 * 设置Velocity模板文件。
	 */
	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

	public void setUriPrefix(String uriPrefix) {
		if (uriPrefix != null && uriPrefix.charAt(0) == PathUtils.PATH_DELIM) {
			uriPrefix = uriPrefix.substring(1);
		}
		this.uriPrefix = uriPrefix;
		uriPrefixLen = (uriPrefix != null) ? uriPrefix.length() : 0;
	}

	public void setUriSuffix(String uriSuffix) {
		this.uriSuffix = uriSuffix;
		uriSuffixLen = (uriSuffix != null) ? uriSuffix.length() : 0;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String uri = getRelativeRequestURI(request);
		if (!PathUtils.isSafePath(uri)) {
			throw new PageAccessDeniedException("[" + request.getRequestURI()
					+ "] Request forbidden.");
		}

		if (shouldAutoLoadDataConfigResources) {
			if (System.currentTimeMillis() - lastValidateTimestamp > MIN_DATA_CONFIG_VALIDATE_SECONDS
					* ONE_SECOND) {
				lastValidateTimestamp = System.currentTimeMillis();

				((ReloadableDataConfigManagerSupport) dataConfigManager)
						.validateAndReloadConfigs();

				if (dataConfigManager instanceof ConfigurableDataConfigManager) {
					((ConfigurableDataConfigManager) dataConfigManager)
							.recalcConfigLocations();
				}
			}
		}

		String viewName = extractViewName(uri);
		DoradoContext context = DoradoContext.getCurrent();
		ViewConfig viewConfig = null;
		try {
			viewConfig = viewConfigManager.getViewConfig(viewName);
		} catch (FileNotFoundException e) {
			throw new PageNotFoundException(e);
		}

		View view = viewConfig.getView();
		String pageTemplate = view.getPageTemplate();
		String pageUri = view.getPageUri();
		if (StringUtils.isNotEmpty(pageTemplate)
				&& StringUtils.isNotEmpty(pageUri)) {
			throw new IllegalArgumentException(
					"Can not set [view.pageTemplate] and [view.pageUri] at the same time.");
		}

		if (StringUtils.isNotEmpty(pageUri)) {
			ServletContext servletContext = context.getServletContext();
			RequestDispatcher requestDispatcher = servletContext
					.getRequestDispatcher(pageUri);
			request.setAttribute(View.class.getName(), view);
			requestDispatcher.include(request, response);
		} else {
			org.apache.velocity.context.Context velocityContext = velocityHelper
					.getContext(view, request, response);

			String templateFile = StringUtils.defaultIfEmpty(pageTemplate,
					this.templateFile);
			Template template = velocityHelper.getVelocityEngine().getTemplate(
					templateFile);

			PrintWriter writer = getWriter(request, response);
			try {
				template.merge(velocityContext, writer);
			} finally {
				writer.flush();
				writer.close();
			}
		}
	}

	protected String extractViewName(String uri) {
		String viewName = StringUtils.substringBefore(uri, ";");
		if (uriPrefix != null && viewName.startsWith(uriPrefix)) {
			viewName = viewName.substring(uriPrefixLen);
		}
		if (uriSuffix != null && viewName.endsWith(uriSuffix)) {
			viewName = viewName.substring(0, viewName.length() - uriSuffixLen);
		}
		return viewName;
	}
}
