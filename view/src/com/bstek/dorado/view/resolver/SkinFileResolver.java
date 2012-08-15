package com.bstek.dorado.view.resolver;

import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.util.PathUtils;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.WebConfigure;
import com.bstek.dorado.web.resolver.WebFileResolver;

/**
 * 用于向客户端输出一个皮肤文件的处理器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 28, 2008
 */
public class SkinFileResolver extends WebFileResolver {
	private static final String CURRENT_SKIN = "~current";
	private static final String CURRENT_SKIN_PREFIX = "skins/" + CURRENT_SKIN
			+ '/';
	private static final String DEFAULT_SKIN = "default";

	public SkinFileResolver() {
		setUseResourcesCache(true);
		setCheckResourceType(true);
	}

	/**
	 * 返回客户端使用的皮肤的名称。
	 */
	protected String getSkin() {
		return WebConfigure.getString("view.skin");
	}

	@Override
	protected Resource[] getResourcesByFileName(DoradoContext context,
			String resourcePrefix, String fileName, String resourceSuffix)
			throws Exception {
		if (!PathUtils.isSafePath(fileName)) {
			throw new IllegalAccessException("Request ["
					+ context.getRequest().getRequestURI() + "] forbidden.");
		}

		String realFileName = fileName;
		String skin = getSkin();
		boolean isSkinFile = fileName.startsWith(CURRENT_SKIN_PREFIX);
		if (isSkinFile) {
			realFileName = fileName.replace(CURRENT_SKIN, skin);
		}
		Resource[] resources = super.getResourcesByFileName(context,
				resourcePrefix, realFileName, resourceSuffix);
		if (isSkinFile && !skin.equals(DEFAULT_SKIN) && !resources[0].exists()) {
			realFileName = fileName.replace(CURRENT_SKIN, DEFAULT_SKIN);
			resources = super.getResourcesByFileName(context, resourcePrefix,
					realFileName, resourceSuffix);
		}
		return resources;
	}
}
