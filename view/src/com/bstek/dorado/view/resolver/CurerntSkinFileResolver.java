package com.bstek.dorado.view.resolver;

import com.bstek.dorado.web.WebConfigure;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-10
 */
public class CurerntSkinFileResolver extends SkinFileResolver {

	@Override
	public String getResourcePrefix() {
		return super.getResourcePrefix() + WebConfigure.getString("view.skin")
				+ '/';
	}
}
