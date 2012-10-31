/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.web.resolver;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.util.PathUtils;
import com.bstek.dorado.web.DoradoContext;

/**
 * 用于向客户端输出一个文件的处理器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 24, 2008
 */
public class WebFileResolver extends AbstractWebFileResolver {
	String baseUri;
	String resourcePrefix;

	/**
	 * 设置URI的根。
	 */
	public void setBaseUri(String baseUri) {
		if (baseUri != null) {
			if (baseUri.charAt(0) == PathUtils.PATH_DELIM) {
				baseUri = baseUri.substring(1);
			}
			if (baseUri.length() > 0
					&& baseUri.charAt(baseUri.length() - 1) != PathUtils.PATH_DELIM) {
				baseUri += PathUtils.PATH_DELIM;
			}
		}
		this.baseUri = baseUri;
	}

	/**
	 * 设置资源路径的前缀。
	 */
	public void setResourcePrefix(String resourcePrefix) {
		this.resourcePrefix = resourcePrefix;
	}

	public String getResourcePrefix() {
		return resourcePrefix;
	}

	@Override
	protected ResourcesWrapper createResourcesWrapper(
			HttpServletRequest request, DoradoContext context) throws Exception {
		String path = getRelativeRequestURI(request);
		if (StringUtils.isNotEmpty(baseUri)) {
			path = path.substring(baseUri.length());
		}

		String resourcePrefix = getResourcePrefix();
		String resourceSuffix = getUriSuffix(request);
		path = path.substring(0, path.length() - resourceSuffix.length());
		Resource[] resources = getResourcesByFileName(context, resourcePrefix,
				path, resourceSuffix);
		return new ResourcesWrapper(resources, getResourceTypeManager()
				.getResourceType(resourceSuffix));
	}

	protected Resource[] getResourcesByFileName(DoradoContext context,
			String resourcePrefix, String fileName, String resourceSuffix)
			throws Exception {
		fileName = PathUtils.concatPath(resourcePrefix, fileName);
		if (resourceSuffix != null) {
			fileName = fileName + resourceSuffix;
		}
		return context.getResources(fileName);
	}
}
