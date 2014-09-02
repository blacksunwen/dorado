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

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2013-1-15
 */
public class DefaultClientSettingsOutputter extends ClientSettingsOutputter {

	@Override
	public void output(Writer writer) throws IOException {
		DoradoContext context = DoradoContext.getCurrent();

		String contextPath = Configure.getString("web.contextPath");
		if (StringUtils.isEmpty(contextPath)) {
			contextPath = context.getRequest().getContextPath();
		}
		writer.append("\"common.contextPath\":\"")
				.append(StringEscapeUtils.escapeJavaScript(contextPath))
				.append("\"");

		if (Configure.getBoolean("view.debugEnabled")) {
			writeSetting(writer, "common.debugEnabled", true, false);
		}
		if (Configure.getBoolean("view.showExceptionStackTrace")) {
			writeSetting(writer, "common.showExceptionStackTrace", true, false);
		}
		if (Configure.getBoolean("view.enterAsTab")) {
			writeSetting(writer, "common.enterAsTab", true, false);
		}
		if (Configure.getBoolean("view.preventBackspace")) {
			writeSetting(writer, "common.preventBackspace", true, true);
		}
		if (Configure.getBoolean("view.abortAsyncLoadingOnSyncLoading")) {
			writeSetting(writer, "common.abortAsyncLoadingOnSyncLoading", true,
					false);
		}

		writeSetting(writer, "widget.skinRoot", ">dorado/client/skins/", true);
		writeSetting(writer, "widget.skin", context.getAttribute("view.skin"),
				true);

		if (Configure.getBoolean("view.lazyInitFloatControl")) {
			writeSetting(writer, "widget.lazyInitFloatControl", true, false);
		}
		if (Configure.getBoolean("view.javaScript.bindAfterChildrenCreate")) {
			writeSetting(writer, "widget.bindControllerAfterChildrenCreate", true, false);
		}
		if (Configure.getBoolean("view.javaScript.fireViewOnCreateForOldController")) {
			writeSetting(writer, "widget.fireViewOnCreateForOldController", true, false);
		}
	}
}
