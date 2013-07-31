/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.view.resolver;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.bstek.dorado.common.ClientType;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.data.config.ConfigurableDataConfigManager;
import com.bstek.dorado.data.config.DataConfigManager;
import com.bstek.dorado.data.config.ReloadableDataConfigManagerSupport;
import com.bstek.dorado.data.variant.VariantUtils;
import com.bstek.dorado.util.PathUtils;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.manager.ViewConfigManager;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.WebConfigure;
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
	private final static String INHERENT_SKIN = "inherent";
	private final static String DEFAULT_SKIN = "default";
	private final static long ONE_SECOND = 1000L;
	private final static long MIN_DATA_CONFIG_VALIDATE_SECONDS = 5;
	private final static String RESOURCE_PREFIX_DELIM = ";,\n\r";

	private DataConfigManager dataConfigManager;
	private ViewConfigManager viewConfigManager;
	private VelocityHelper velocityHelper;
	private String templateFile;
	private String validTemplateFile;
	private String uriPrefix;
	private int uriPrefixLen;
	private String uriSuffix;
	private int uriSuffixLen;

	private boolean shouldAutoLoadDataConfigResources;
	private long lastValidateTimestamp;
	private List<ViewResolverListener> listeners;

	public HtmlViewResolver() {
		setContentType(HttpConstants.CONTENT_TYPE_HTML);
		setCacheControl(HttpConstants.NO_CACHE);
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

	public VelocityHelper getVelocityHelper() {
		return velocityHelper;
	}

	/**
	 * 设置Velocity模板文件。
	 */
	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
		validTemplateFile = null;
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

	public synchronized void addViewResolverListener(
			ViewResolverListener listener) {
		if (listeners == null) {
			listeners = new Vector<ViewResolverListener>();
		}
		listeners.add(listener);
	}

	public synchronized void removeViewResolverListener(
			ViewResolverListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
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

		if (listeners != null) {
			synchronized (listeners) {
				for (ViewResolverListener listener : listeners) {
					listener.beforeResolveView(viewName);
				}
			}
		}

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
			String skin = view.getSkin();
			if (StringUtils.isEmpty(skin)) {
				skin = WebConfigure.getString("view.skin");
			}

			if (StringUtils.isBlank(skin)) {
				skin = DEFAULT_SKIN;
				WebConfigure.set(DoradoContext.REQUEST, "view.skin",
						DEFAULT_SKIN);
			} else if (INHERENT_SKIN.equals(skin)) {
				throw new IllegalArgumentException("\"" + INHERENT_SKIN
						+ "\" is not a valid dorado skin.");
			}

			DoradoContext doradoContext = DoradoContext.getCurrent();
			int currentClientType = VariantUtils.toInt(doradoContext
					.getAttribute(ClientType.CURRENT_CLIENT_TYPE_KEY));
			if (currentClientType != ClientType.DESKTOP
					&& skin.indexOf('.') < 0) {
				String tempSkin = skin + '.'
						+ ClientType.toString(currentClientType);

				String libraryRoot = WebConfigure.getString("view.libraryRoot");
				String pathSection = PathUtils.concatPath("skins", tempSkin,
						"core.css");

				if (StringUtils.indexOfAny(libraryRoot, RESOURCE_PREFIX_DELIM) >= 0) {
					String[] roots = StringUtils.split(libraryRoot,
							RESOURCE_PREFIX_DELIM);
					for (String root : roots) {
						String tempPath = PathUtils.concatPath(root,
								pathSection);
						Resource tempRes = doradoContext.getResource(tempPath);
						if (tempRes.exists()) {
							WebConfigure.set(DoradoContext.REQUEST,
									"view.skin", tempSkin);
							break;
						}
					}
				} else {
					String tempPath = PathUtils.concatPath(libraryRoot,
							pathSection);
					Resource tempRes = doradoContext.getResource(tempPath);
					if (tempRes.exists()) {
						WebConfigure.set(DoradoContext.REQUEST, "view.skin",
								tempSkin);
					}
				}
			}

			org.apache.velocity.context.Context velocityContext = velocityHelper
					.getContext(view, request, response);

			Template template = getPageTemplate(pageTemplate);
			PrintWriter writer = getWriter(request, response);
			try {
				template.merge(velocityContext, writer);
			} finally {
				writer.flush();
				writer.close();
			}
		}

		if (listeners != null) {
			synchronized (listeners) {
				for (ViewResolverListener listener : listeners) {
					listener.afterResolveView(viewName, view);
				}
			}
		}
	}

	protected Template getPageTemplate(String pageTemplate) throws Exception {
		Template template = null;
		if (StringUtils.isNotEmpty(pageTemplate)) {
			template = getValocityEngine().getTemplate(pageTemplate);
		} else if (validTemplateFile == null) {
			for (String singleTemplateFile : StringUtils.split(templateFile,
					',')) {
				try {
					template = getValocityEngine().getTemplate(
							singleTemplateFile);
					break;
				} catch (ResourceNotFoundException e) {
					// do nothing
				}
			}
			if (template == null) {
				throw new ResourceNotFoundException(
						"Could not found a page template file in such location(s): "
								+ templateFile);
			}
		} else {
			template = getValocityEngine().getTemplate(validTemplateFile);
		}
		return template;
	}

	protected VelocityEngine getValocityEngine() throws Exception {
		return velocityHelper.getVelocityEngine();
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
