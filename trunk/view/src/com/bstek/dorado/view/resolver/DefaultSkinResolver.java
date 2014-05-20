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

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.common.ClientType;
import com.bstek.dorado.data.variant.VariantUtils;
import com.bstek.dorado.view.View;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.WebConfigure;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2014-3-2
 */
public class DefaultSkinResolver implements SkinResolver {
	private final static String DEFAULT_SKIN = "default";
	private final static String MSIE = "MSIE";
	private final static Pattern MSIE_VERSION_PATTERN = Pattern
			.compile("^.*?MSIE\\s+(\\d+).*$");

	private SkinSettingManager skinSettingManager;

	public void setSkinSettingManager(SkinSettingManager skinSettingManager) {
		this.skinSettingManager = skinSettingManager;
	}

	protected String doDetermineSkin(DoradoContext context, String skins)
			throws Exception {
		int currentClientType = VariantUtils.toInt(context
				.getAttribute(ClientType.CURRENT_CLIENT_TYPE_KEY));
		boolean isIE = false, isIE6 = false, isOldIE = false;
		String ieVersion = null;
		if (currentClientType == 0) {
			String ua = context.getRequest().getHeader("User-Agent");
			isIE = (ua != null && ua.indexOf(MSIE) != -1);
			if (isIE) {
				ieVersion = MSIE_VERSION_PATTERN.matcher(ua).replaceAll("$1");
				if (StringUtils.isNotEmpty(ieVersion)) {
					if ("9".compareTo(ieVersion) > 0) {
						isOldIE = true;
						if ("7".compareTo(ieVersion) > 0) {
							isIE6 = true;
						}
					}
				}
			}
		}

		if (skins != null) {
			skins += (',' + DEFAULT_SKIN);
		} else {
			skins = DEFAULT_SKIN;
		}
		String[] skinArray = StringUtils.split(skins, ',');
		for (String skin : skinArray) {
			SkinSetting skinSetting;
			String realSkin;
			if (currentClientType != 0) {
				realSkin = skin + '.' + ClientType.toString(currentClientType);

				skinSetting = skinSettingManager.getSkinSetting(context,
						realSkin);
				if (skinSetting != null) {
					if (ClientType.supports(skinSetting.getClientTypes(),
							currentClientType)) {
						return realSkin;
					}
				}
			} else if (isIE6) {
				realSkin = skin + ".ie6";

				skinSetting = skinSettingManager.getSkinSetting(context,
						realSkin);
				if (skinSetting != null) {
					if (skinSetting.getUserAgent().indexOf("-ie") < 0) {
						return realSkin;
					}
				}
			}

			skinSetting = skinSettingManager.getSkinSetting(context, skin);
			if (skinSetting != null) {
				if (isOldIE) {
					String skinUserAgent = skinSetting.getUserAgent();
					int i = skinUserAgent.indexOf("-ie");
					if (i >= 0) {
						i += 4;
						String version = "";
						int len = skinUserAgent.length();
						while (i < len) {
							char c = skinUserAgent.charAt(i);
							if (c >= '0' && c <= '9' || c == '.') {
								version += c;
							} else {
								break;
							}
						}

						if (ieVersion.compareTo(version) <= 0) {
							continue;
						}
					}
				}
				return skin;
			}
		}

		return DEFAULT_SKIN;
	}

	public String determineSkin(DoradoContext context, View view)
			throws Exception {
		String skins = view.getSkin();
		if (StringUtils.isEmpty(skins)) {
			skins = WebConfigure.getString("view.skin");
		}
		return doDetermineSkin(context, skins);
	}

}
