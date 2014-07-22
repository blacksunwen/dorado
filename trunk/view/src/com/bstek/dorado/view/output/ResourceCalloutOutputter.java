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

package com.bstek.dorado.view.output;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2014-7-21
 */
public class ResourceCalloutOutputter implements Outputter {

	public void output(Object object, OutputContext context) throws Exception {
		Callout callout = (Callout) object;
		Writer writer = context.getWriter();

		writer.append("<script id=\"").append(callout.getId())
				.append("\" type=\"d-template\">");

		String path = (String) callout.getObject();
		DoradoContext doradoContext = DoradoContext.getCurrent();

		Resource resource = doradoContext.getResource(path);
		InputStream in = resource.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		try {
			String s;
			while ((s = reader.readLine()) != null) {
				writer.append(s).append('\n');
			}
		} catch (IOException e) {
			// do nothing
		} finally {
			in.close();
			reader.close();
		}

		writer.append("</script>\n");
	}
}
